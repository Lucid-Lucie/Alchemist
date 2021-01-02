package lucie.alchemist.enchantment;

import lucie.alchemist.function.FunctionTools;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class EnchantmentKnowledge extends Enchantment
{
    public EnchantmentKnowledge()
    {
        super(Rarity.VERY_RARE, EnchantmentType.create("knowledge", null), new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
        setRegistryName("knowledge");
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
    public boolean canApply(ItemStack stack)
    {
        return FunctionTools.getItems().contains(stack.getItem());
    }
}
