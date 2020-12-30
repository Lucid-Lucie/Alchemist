package lucie.alchemist.enchantment;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lucie.alchemist.feature.FeatureMixture;
import lucie.alchemist.item.AlchemicalItems;
import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnchantmentKnowledge extends Enchantment
{
    private static final Usable USABLE = new Gson().fromJson(new JsonParser().parse(new InputStreamReader((Objects.requireNonNull(Usable.class.getClassLoader().getResourceAsStream("data/alchemist/enchantments/knowledge.json"))), StandardCharsets.UTF_8)).toString(), Usable.class);


    public EnchantmentKnowledge()
    {
        super(Rarity.VERY_RARE, EnchantmentType.create("knowledge", null), new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
        this.setRegistryName("knowledge");
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
        return USABLE.getItems().contains(stack.getItem());
    }

    private static class Usable
    {
        private String[] enchantments;

        public Usable(String[] enchantments)
        {
            this.enchantments = enchantments;
        }

        public List<Item> getItems()
        {
            List<Item> list = new ArrayList<>();

            for (String s : enchantments)
            {
                list.add(UtilityGetter.getItem(new ResourceLocation(s)));
            }

            return list;
        }
    }
}
