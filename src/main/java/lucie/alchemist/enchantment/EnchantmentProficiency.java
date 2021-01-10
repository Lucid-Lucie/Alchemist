package lucie.alchemist.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class EnchantmentProficiency extends Enchantment
{
    public EnchantmentProficiency()
    {
        super(Rarity.RARE, EnchantmentType.BREAKABLE, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
        setRegistryName("proficiency");
    }

    @Override
    public boolean canApply(ItemStack stack)
    {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return canApply(stack);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMaxEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public int getMinLevel()
    {
        return 1;
    }

    @Override
    public int getMaxLevel()
    {
        return 3;
    }

    @Override
    public boolean canVillagerTrade()
    {
        return true;
    }

    @Override
    public boolean canGenerateInLoot()
    {
        return true;
    }
}
