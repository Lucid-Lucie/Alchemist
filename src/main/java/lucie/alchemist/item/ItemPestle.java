package lucie.alchemist.item;

import lucie.alchemist.Alchemist;
import lucie.alchemist.enchantment.AlchemicalEnchantments;
import lucie.alchemist.item.AlchemicalItems.ItemType;
import lucie.alchemist.utility.UtilityTooltip;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPestle extends Item
{
    public ItemPestle()
    {
        super(new Item.Properties().maxStackSize(1).rarity(ItemType.TOOL.getRarity()).group(Alchemist.GROUP).maxDamage(32));
        setRegistryName("pestle");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        new UtilityTooltip(tooltip).journal(ItemType.TOOL, "pestle", I18n.format("journal.alchemist.pestle").length());
    }

    @Nonnull
    @Override
    public Rarity getRarity(ItemStack stack)
    {
        // Fixes enchantment color not showing.
        return stack.isEnchanted() ? Rarity.RARE : super.getRarity(stack);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn)
    {
        // Pulverizing Mechanic.
        ItemStack pestle = playerIn.getHeldItem(Hand.MAIN_HAND);
        ItemStack pouch = playerIn.getHeldItem(Hand.OFF_HAND);

        // Check if right items are on right positions.
        if (!pestle.getItem().equals(AlchemicalItems.PESTLE) || !(pouch.getItem() instanceof ItemMixture)) return super.onItemRightClick(worldIn, playerIn, handIn);

        if (EnchantmentHelper.getEnchantmentLevel(AlchemicalEnchantments.MASTERY, pestle) > 0)
        {
            // Mastery always gives best values.
            pouch = ((ItemMixture)pouch.getItem()).compoundWrite(pouch, 0, true);
        }
        else
        {
            // Give value based on proficiency.
            pouch = ((ItemMixture)pouch.getItem()).compoundWrite(pouch, EnchantmentHelper.getEnchantmentLevel(AlchemicalEnchantments.PROFICIENCY, pestle), false);
        }

        // Damage item and update hands.
        playerIn.setHeldItem(Hand.OFF_HAND, pouch);
        playerIn.getHeldItem(Hand.MAIN_HAND).damageItem(1, playerIn, e -> e.sendBreakAnimation(handIn));

        return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
