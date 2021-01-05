package lucie.alchemist.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

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
        // Tools that can have knowledge are contained as an item tag.
        ITag<Item> tools = ItemTags.getCollection().get(new ResourceLocation("alchemist", "tools"));
        if (tools == null) return false;

        return tools.contains(stack.getItem());
    }
}
