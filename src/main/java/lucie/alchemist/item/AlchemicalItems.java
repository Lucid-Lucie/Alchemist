package lucie.alchemist.item;

import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("alchemist")
public class AlchemicalItems
{
    /* Tools */

    @ObjectHolder("pouch")
    public static ItemPouch POUCH;

    @ObjectHolder("pestle")
    public static ItemPestle PESTLE;

    /* Ingredients */

    @ObjectHolder("stick")
    public static ItemIngredient STICK;

    @ObjectHolder("compost")
    public static ItemIngredient COMPOST;

    @ObjectHolder("leather")
    public static ItemIngredient LEATHER;

    @ObjectHolder("seeds")
    public static ItemIngredient SEEDS;

    /* Mixtures */

    @ObjectHolder("slowness_ingredients")
    public static ItemMixture SLOWNESS;

    @ObjectHolder("armor_teleportation_ingredients")
    public static ItemMixture ARMOR_TELEPORTATION;

    @ObjectHolder("levitation_ingredients")
    public static ItemMixture LEVITATION;

    @ObjectHolder("cursed_greed_ingredients")
    public static ItemMixture CURSED_GREED;

    @ObjectHolder("soul_draining_ingredients")
    public static ItemMixture SOUL_DRAINING;

    @ObjectHolder("life_taking_ingredients")
    public static ItemMixture LIFE_TAKING;
}
