package lucie.alchemist.utility;

import lucie.alchemist.effect.EffectDisplacement;
import lucie.alchemist.effect.EffectLifeTaking;
import lucie.alchemist.effect.EffectSoulDraining;
import lucie.alchemist.effect.EffectThieving;
import lucie.alchemist.enchantment.EnchantmentBrewing;
import lucie.alchemist.enchantment.EnchantmentProficiency;
import lucie.alchemist.potion.PotionBase;
import net.minecraftforge.registries.ObjectHolder;

public class UtilityGetter
{
    @ObjectHolder("alchemist")
    public static class Enchantments
    {
        @ObjectHolder("brewing")
        public static EnchantmentBrewing BREWING;

        @ObjectHolder("proficiency")
        public static EnchantmentProficiency PROFICIENCY;
    }

    @ObjectHolder("alchemist")
    public static class Effects
    {
        @ObjectHolder("displacement")
        public static EffectDisplacement DISPLACEMENT;

        @ObjectHolder("thieving")
        public static EffectThieving THIEVING;

        @ObjectHolder("soul_draining")
        public static EffectSoulDraining SOUL_DRAINING;

        @ObjectHolder("life_taking")
        public static EffectLifeTaking LIFE_TAKING;
    }

    @ObjectHolder("alchemist")
    public static class Potions
    {
        /* Thieving */

        @ObjectHolder("thieving")
        public static PotionBase THIEVING;

        @ObjectHolder("thieving_long")
        public static PotionBase THIEVING_LONG;

        @ObjectHolder("thieving_strong")
        public static PotionBase THIEVING_STRONG;

        /* Soul Draining */

        @ObjectHolder("soul_draining")
        public static PotionBase SOUL_DRAINING;

        @ObjectHolder("soul_draining_long")
        public static PotionBase SOUL_DRAINING_LONG;

        @ObjectHolder("soul_draining_strong")
        public static PotionBase SOUL_DRAINING_STRONG;

        /* Life Taking */

        @ObjectHolder("life_taking")
        public static PotionBase LIFE_TAKING;

        @ObjectHolder("life_taking_strong")
        public static PotionBase LIFE_TAKING_STRONG;

        /* Displacement */

        @ObjectHolder("displacement")
        public static PotionBase DISPLACEMENT;

        @ObjectHolder("displacement_strong")
        public static PotionBase DISPLACEMENT_STRONG;

        /* Levitation */

        @ObjectHolder("levitation")
        public static PotionBase LEVITATION;

        @ObjectHolder("levitation_long")
        public static PotionBase LEVITATION_LONG;
    }
}
