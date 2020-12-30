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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class ItemMixture extends Item
{
    private String name;

    private Mixture getMixture()
    {
        return new Gson().fromJson(new JsonParser().parse(new InputStreamReader((Objects.requireNonNull(Mixture.class.getClassLoader().getResourceAsStream("data/alchemist/mixtures/" + this.name + ".json"))), StandardCharsets.UTF_8)).toString(), Mixture.class);
    }

    public ItemMixture(String name)
    {
        super(new Item.Properties().rarity(Alchemist.RARITY).group(Alchemist.GROUP).maxStackSize(1));
        this.setRegistryName(name + "_ingredients");
        this.name = name;
    }

    // Text and Information.

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        Mixture m = getMixture();
        String type = compoundCheck(stack) ? "item.alchemist.mixture" : "item.alchemist.ingredient";
        return new StringTextComponent(I18n.format(type, I18n.format("effect." + m.getResourceLocation().getNamespace() + "." + m.getResourceLocation().getPath())));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (compoundCheck(stack))
        {
            String campfire = stack.getTag().getCompound("mixture").getBoolean("soulfire") ? "block.minecraft.soul_campfire" : "block.minecraft.campfire";
            boolean instant = stack.getTag().getCompound("mixture").getBoolean("instant");

            UtilityTooltip utility = new UtilityTooltip(tooltip);
            utility.color(new String[]{I18n.format("tooltip.alchemist.requires") + ": ", I18n.format(campfire)}, new TextFormatting[]{TextFormatting.GRAY, TextFormatting.WHITE});
            utility.space();
            utility.color(new String[]{I18n.format("potion.whenDrank")+ " ", "[", String.valueOf(stack.getTag().getCompound("mixture").getInt("uses")),"]"}, new TextFormatting[]{this.getRarity(stack).color, TextFormatting.GRAY, TextFormatting.WHITE, TextFormatting.GRAY});
            if (instant) utility.effect(UtilityGetter.getEffectInstance(new ResourceLocation(stack.getTag().getCompound("mixture").getString("effect")), 1, stack.getTag().getCompound("mixture").getInt("amplifier")), TextFormatting.WHITE);
            else utility.effect(UtilityGetter.getEffectInstance(new ResourceLocation(stack.getTag().getCompound("mixture").getString("effect")), stack.getTag().getCompound("mixture").getInt("duration"), stack.getTag().getCompound("mixture").getInt("amplifier")), TextFormatting.WHITE);
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    // Functions

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!compoundCheck(playerIn.getHeldItem(handIn)))
        {
            return new ActionResult<>(ActionResultType.SUCCESS, compoundWrite(playerIn.getHeldItem(handIn), 1));
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }


    // Tools

    private int roll(int times, int current, int[] values)
    {
        int rnd; // gets better values.

        for (int i = 0; i < times; i++)
        {
            rnd = values[random.nextInt(values.length)];

            if (rnd > current) current = rnd;
        }

        return current;
    }

    // Compound

    private ItemStack compoundWrite(ItemStack stack, int level)
    {
        CompoundNBT nbt = new CompoundNBT();
        Mixture mixture = this.getMixture();

        int amplifier = mixture.getAmplifiers()[0];

        // Get random amplifier if there are more than one.
        if (mixture.getAmplifiers().length > 1)
        {
            // Levels are used for extra rolls.
            if (level > 0) amplifier = roll(level, amplifier, mixture.getAmplifiers());
        }

        int uses = mixture.getUses()[0];

        // Get random amplifier if there are more than one.
        if (mixture.getUses().length > 1)
        {
            // Levels are used for extra rolls.
            if (level > 0) uses = roll(level, uses, mixture.getUses());
        }

        // Get random duration.
        if (!mixture.isInstant())
        {
            int duration = mixture.getDurations()[0];

            if (mixture.getDurations().length > 1)
            {
                // Levels are used for extra rolls.
                if (level > 0) duration = roll(level, duration, mixture.getDurations());
            }

            nbt.putInt("duration", duration);
        }

        nbt.putString("effect", mixture.getEffect());
        nbt.putBoolean("instant", mixture.isInstant());
        nbt.putBoolean("soulfire", mixture.isSoulfire());
        nbt.putInt("amplifier", amplifier);
        nbt.putInt("uses", uses);

        if (stack.getTag() == null) stack.setTag(new CompoundNBT());
        stack.getTag().put("mixture", nbt);

        return stack;
    }

    public static boolean compoundCheck(ItemStack stack)
    {
        return stack.getTag() != null && stack.getTag().contains("mixture");
    }
}
