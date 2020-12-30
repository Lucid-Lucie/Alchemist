package lucie.alchemist.item;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lucie.alchemist.Alchemist;
import lucie.alchemist.feature.FeatureMixture.Mixture;
import lucie.alchemist.utility.UtilityGetter;
import lucie.alchemist.utility.UtilityTooltip;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class ItemMixture extends Item
{
    private Mixture mixture;

    public ItemMixture(String name)
    {
        super(new Item.Properties().rarity(Alchemist.RARITY).maxStackSize(1));
        this.setRegistryName(name + "_ingredients");
        this.mixture =  new Gson().fromJson(new JsonParser().parse(new InputStreamReader((Objects.requireNonNull(Mixture.class.getClassLoader().getResourceAsStream("data/alchemist/mixtures/" + name + ".json"))), StandardCharsets.UTF_8)).toString(), Mixture.class);
    }

    /* Text and Information. */

    @Nonnull
    @Override
    public ITextComponent getDisplayName(@Nonnull ItemStack stack)
    {
        String type = compoundCheck(stack) ? "item.alchemist.mixture" : "item.alchemist.ingredient";
        return new StringTextComponent(I18n.format(type, I18n.format("effect." + mixture.getResourceLocation().getNamespace() + "." + mixture.getResourceLocation().getPath())));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        UtilityTooltip utility = new UtilityTooltip(tooltip);

        // Mixture
        if (compoundCheck(stack))
        {
            // Compound is checked by "compoundCheck"
            if (stack.getTag() == null) return;
            String campfire = stack.getTag().getCompound("mixture").getBoolean("soulfire") ? "block.minecraft.soul_campfire" : "block.minecraft.campfire";

            // Put info on tooltip
            utility.color(new String[]{I18n.format("tooltip.alchemist.requires") + ": ", I18n.format(campfire)}, new TextFormatting[]{TextFormatting.GRAY, TextFormatting.WHITE});
            utility.space();
            utility.color(new String[]{I18n.format("potion.whenDrank")+ " ", "[", String.valueOf(stack.getTag().getCompound("mixture").getInt("uses")),"]"}, new TextFormatting[]{this.getRarity(stack).color, TextFormatting.GRAY, TextFormatting.WHITE, TextFormatting.GRAY});
            utility.effect(UtilityGetter.getEffectInstance(new ResourceLocation(stack.getTag().getCompound("mixture").getString("effect")), stack.getTag().getCompound("mixture").getInt("duration"), stack.getTag().getCompound("mixture").getInt("amplifier")), TextFormatting.WHITE);
        }
        else // Ingredient
        {
            utility.trim(I18n.format("description.alchemist.ingredients"), 20);
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    /* Functions */

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn)
    {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        // Adds ingredient pouch and a mixture pouch with maxed stats.
        if (group.equals(Alchemist.GROUP_MIXTURES) || group.equals(ItemGroup.SEARCH))
        {
            items.add(new ItemStack(this));
            items.add(compoundWrite(new ItemStack(this), 0, true));
        }
    }

    /* Tools */

    private int roll(int times, int current, int[] values)
    {
        // Rolls for better values if values are more than one.
        if (values.length == 1) return current;

        int rnd;

        for (int i = 0; i < times; i++)
        {
            rnd = values[random.nextInt(values.length)];

            if (rnd > current && (times >= 3 || random.nextInt(3 - times) == 0)) current = rnd;
        }

        return current;
    }

    /* Compound */

    public ItemStack compoundWrite(ItemStack stack, int level, boolean master)
    {
        // Writes Mixture information to child compound.
        CompoundNBT nbt = new CompoundNBT();

        // Amplifier, Uses and Duration are assigned lowest possible numbers.
        int amplifier = master ? mixture.getAmplifiers()[mixture.getAmplifiers().length - 1] : mixture.getAmplifiers()[0];
        int uses = master ? mixture.getUses()[mixture.getUses().length - 1] : mixture.getUses()[0];
        int duration = master ? mixture.getDurations()[mixture.getDurations().length -1] : mixture.getDurations()[0];

        if (level > 0 && !master)
        {
            // If level is above zero then roll for better values.
            amplifier = roll(level, amplifier, mixture.getAmplifiers());
            uses = roll(level, uses, mixture.getUses());
            duration = roll(level, duration, mixture.getDurations());
        }

        // Bake the big fucking compound.
        nbt.putInt("duration", duration);
        nbt.putString("effect", mixture.getEffect());
        nbt.putBoolean("instant", mixture.isInstant());
        nbt.putBoolean("soulfire", mixture.isSoulfire());
        nbt.putInt("amplifier", amplifier);
        nbt.putInt("uses", uses);

        // Create parent compound if it doesn't exist.
        if (stack.getTag() == null) stack.setTag(new CompoundNBT());
        stack.getTag().put("mixture", nbt);

        return stack;
    }

    public static boolean compoundCheck(ItemStack stack)
    {
        // Checks for parent compound 'mixture' found both on tools and pouches.
        return stack.getTag() != null && stack.getTag().contains("mixture");
    }
}
