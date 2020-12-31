package lucie.alchemist.item;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lucie.alchemist.Alchemist;
import lucie.alchemist.enchantment.AlchemicalEnchantments;
import lucie.alchemist.feature.FeatureMixture.Mixture;
import lucie.alchemist.feature.FeatureTools;
import lucie.alchemist.utility.UtilityGetter;
import lucie.alchemist.utility.UtilityTooltip;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
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

import static lucie.alchemist.utility.UtilityCompound.Pouch;
import static lucie.alchemist.utility.UtilityCompound.Tool;

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
        Pouch pouch = Pouch.convert(stack);

        // Check if compound exist
        if (pouch.doesExist())
        {
            String campfire = pouch.isSoulfire() ? "soul_campfire" : "campfire";

            // Put info on tooltip
            utility.color(new String[]{I18n.format("tooltip.alchemist.requires") + ": ", I18n.format("block.minecraft." + campfire)}, new TextFormatting[]{TextFormatting.GRAY, TextFormatting.WHITE});
            utility.space();
            utility.color(new String[]{I18n.format("potion.whenDrank")+ " ", "[", String.valueOf(pouch.getUses()),"]"}, new TextFormatting[]{this.getRarity(stack).color, TextFormatting.GRAY, TextFormatting.WHITE, TextFormatting.GRAY});
            utility.effect(UtilityGetter.getEffectInstance(new ResourceLocation(pouch.getEffect()), pouch.getDuration(), pouch.getAmplifier()), TextFormatting.WHITE);
        }
        else
        {
            utility.trim(I18n.format("description.alchemist.ingredients"), 20);
        }
    }

    /* Functions */

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        if (context.getPlayer() == null) return super.onItemUse(context);

        ItemStack hand_right = context.getPlayer().getHeldItem(Hand.MAIN_HAND);
        ItemStack hand_left = context.getPlayer().getHeldItem(Hand.OFF_HAND);
        BlockState state = context.getWorld().getBlockState(context.getPos());

        // Needs block to be campfire and campfire to be lit.
        if ((!state.getBlock().equals(Blocks.CAMPFIRE) && !state.getBlock().equals(Blocks.SOUL_CAMPFIRE)) || !state.get(CampfireBlock.LIT)) return super.onItemUse(context);

        // Main hand is used to remove mixture, offhand is used to apply mixture.
        if (context.getHand().equals(Hand.MAIN_HAND))
        {
            // Can't have a tool in offhand when removing mixture. (Prevents clumsy people)
            if (FeatureTools.getItems().contains(hand_left.getItem())) return super.onItemUse(context);

            // Removes mixture and turns off the campfire.
            context.getWorld().setBlockState(context.getPos(), state.with(CampfireBlock.LIT, true));
            context.getPlayer().setHeldItem(Hand.MAIN_HAND, new ItemStack(AlchemicalItems.POUCH));

            return ActionResultType.SUCCESS;
        }
        else
        {
            // Needs left hand to be this and right hand to be tool.
            if (!hand_left.getItem().equals(this) || !FeatureTools.getItems().contains(hand_right.getItem())) return super.onItemUse(context);

            Pouch pouch = Pouch.convert(hand_left);
            Tool tool_primary = Tool.convert(hand_right, true);
            Tool tool_secondary = Tool.convert(hand_right, false);

            // Pouch needs to be ready.
            if (!pouch.doesExist()) return super.onItemUse(context);

            // Check if soulfire is needed.
            if (pouch.isSoulfire() && !state.getBlock().equals(Blocks.SOUL_CAMPFIRE)) return super.onItemUse(context);

            // Tool needs to be able to put on a new mixture
            if (tool_primary.getUses() != 0 && (tool_secondary.getUses() != 0 || EnchantmentHelper.getEnchantmentLevel(AlchemicalEnchantments.KNOWLEDGE, hand_right) == 0)) return super.onItemUse(context);

            // Tool can't have same mixtures.
            if ((pouch.getEffect().equals(tool_primary.getEffect()) && tool_primary.getUses() > 0) || (pouch.getEffect().equals(tool_secondary.getEffect()) && tool_secondary.getUses() > 0)) return super.onItemUse(context);

            // Make campfire unlit, add mixture to tool, empty pouch.
            context.getWorld().setBlockState(context.getPos(), state.with(CampfireBlock.LIT, false));
            context.getPlayer().setHeldItem(Hand.MAIN_HAND, compoundInject(hand_left, hand_right));
            context.getPlayer().setHeldItem(Hand.OFF_HAND, new ItemStack(AlchemicalItems.POUCH));

            Alchemist.LOGGER.debug(context.getPlayer().getHeldItem(Hand.MAIN_HAND).getTag());

            return ActionResultType.SUCCESS;
        }
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
        nbt.putBoolean("soulfire", mixture.isSoulfire());
        nbt.putInt("amplifier", amplifier);
        nbt.putInt("uses", uses);

        // Create parent compound if it doesn't exist.
        if (stack.getTag() == null) stack.setTag(new CompoundNBT());
        stack.getTag().put("mixture", nbt);

        return stack;
    }

    public ItemStack compoundInject(ItemStack mixture, ItemStack tool)
    {
        // Knowledge gives an extra slot to put mixtures in.
        boolean hasKnowledge = EnchantmentHelper.getEnchantmentLevel(AlchemicalEnchantments.KNOWLEDGE, tool) > 0;

        Pouch pouch = Pouch.convert(mixture);
        Tool tool_primary = Tool.convert(tool, true);

        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("uses", pouch.getUses());
        nbt.putInt("duration", pouch.getDuration());
        nbt.putInt("amplifier", pouch.getAmplifier());
        nbt.putString("effect", pouch.getEffect());

        if (tool.getTag() == null) tool.setTag(new CompoundNBT());

        // Add mixture to primary if it hasn't any.
        if (!tool_primary.doesExist() || tool_primary.getUses() == 0)
        {
            if (!tool.getTag().contains("mixture")) tool.getTag().put("mixture", new CompoundNBT());

            tool.getTag().getCompound("mixture").put("primary", nbt);
        }
        else if (hasKnowledge)
        {
            // Add mixture on secondary.
            tool.getTag().getCompound("mixture").put("secondary", nbt);
        }

        return tool;
    }

    public static boolean compoundCheck(ItemStack stack)
    {
        // Checks for parent compound 'mixture' found both on tools and pouches.
        return stack.getTag() != null && stack.getTag().contains("mixture");
    }
}
