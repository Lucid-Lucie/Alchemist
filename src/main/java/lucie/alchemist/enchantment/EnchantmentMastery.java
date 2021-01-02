package lucie.alchemist.enchantment;

import lucie.alchemist.item.AlchemicalItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class EnchantmentMastery extends Enchantment
{
    public EnchantmentMastery()
    {
        super(Rarity.RARE, EnchantmentType.create("mastery", item -> item.equals(AlchemicalItems.PESTLE)), new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
        setRegistryName("mastery");
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean isTreasureEnchantment()
    {
        return true;
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

    @Override
    public int getMinLevel()
    {
        return 1;
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench)
    {
        return !ench.equals(AlchemicalEnchantments.PROFICIENCY);
    }

    @Override
    public boolean canApply(ItemStack stack)
    {
        return stack.getItem().equals(AlchemicalItems.PESTLE);
    }
}
