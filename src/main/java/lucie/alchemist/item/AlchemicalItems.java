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

    /* Materials */

    @ObjectHolder("stick")
    public static ItemMaterial STICK;

    @ObjectHolder("essence")
    public static ItemMaterial ESSENCE;

    @ObjectHolder("leather")
    public static ItemMaterial LEATHER;

    @ObjectHolder("seeds")
    public static ItemMaterial SEEDS;

    /* Mixtures */

    @ObjectHolder("slowness_ingredients")
    public static ItemMixture SLOWNESS;

    @ObjectHolder("displacement_ingredients")
    public static ItemMixture DISPLACEMENT;

    @ObjectHolder("levitation_ingredients")
    public static ItemMixture LEVITATION;

    @ObjectHolder("thieving_ingredients")
    public static ItemMixture THIEVING;

    @ObjectHolder("soul_draining_ingredients")
    public static ItemMixture SOUL_DRAINING;

    @ObjectHolder("life_taking_ingredients")
    public static ItemMixture LIFE_TAKING;
}
