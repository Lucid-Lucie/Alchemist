package lucie.alchemist.item;

import net.minecraft.item.Rarity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ObjectHolder;

public class AlchemicalItems
{
    /* Rarity */

    public static enum  ItemType
    {
        TOOL(Rarity.create("alchemy_tool", TextFormatting.YELLOW), "alchemy"),
        MATERIAL(Rarity.create("alchemy_material", TextFormatting.YELLOW), "alchemy"),
        INGREDIENT(Rarity.create("alchemy_ingredient", TextFormatting.BLUE), "herbology");

        private Rarity rarity;
        private String journal;

        ItemType(Rarity rarity, String type)
        {
            this.rarity = rarity;
            journal = type;
        }

        public Rarity getRarity()
        {
            return rarity;
        }

        public String getJournal()
        {
            return journal;
        }
    }

    /* Tools */

    @ObjectHolder("alchemist:pouch")
    public static ItemPouch POUCH;

    @ObjectHolder("alchemist:pestle")
    public static ItemPestle PESTLE;

    /* Materials */

    @ObjectHolder("alchemist:stick")
    public static ItemBasic STICK;

    @ObjectHolder("alchemist:essence")
    public static ItemBasic ESSENCE;

    @ObjectHolder("alchemist:leather")
    public static ItemBasic LEATHER;

    @ObjectHolder("alchemist:seeds")
    public static ItemBasic SEEDS;

    /* Ingredients */

    @ObjectHolder("alchemist:ash")
    public static ItemBasic ASH;

    /* Mixtures */

    @ObjectHolder("alchemist:slowness_ingredients")
    public static ItemMixture SLOWNESS;

    @ObjectHolder("alchemist:displacement_ingredients")
    public static ItemMixture DISPLACEMENT;

    @ObjectHolder("alchemist:levitation_ingredients")
    public static ItemMixture LEVITATION;

    @ObjectHolder("alchemist:thieving_ingredients")
    public static ItemMixture THIEVING;

    @ObjectHolder("alchemist:soul_draining_ingredients")
    public static ItemMixture SOUL_DRAINING;

    @ObjectHolder("alchemist:life_taking_ingredients")
    public static ItemMixture LIFE_TAKING;
}
