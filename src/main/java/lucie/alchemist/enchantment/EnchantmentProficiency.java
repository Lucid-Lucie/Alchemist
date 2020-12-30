package lucie.alchemist.enchantment;

import lucie.alchemist.item.AlchemicalItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class EnchantmentProficiency extends Enchantment
{
    public EnchantmentProficiency()
    {
        super(Rarity.UNCOMMON, EnchantmentType.create("proficiency", item -> item.equals(AlchemicalItems.PESTLE)), new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
        this.setRegistryName("proficiency");
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
    protected boolean canApplyTogether(Enchantment ench)
    {
        return !ench.equals(AlchemicalEnchantments.MASTERY);
    }

    @Override
    public boolean canApply(ItemStack stack)
    {
        return stack.getItem().equals(AlchemicalItems.PESTLE);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return canApply(stack);
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
