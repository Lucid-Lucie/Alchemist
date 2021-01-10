package lucie.alchemist.utility;

import lucie.alchemist.enchantment.EnchantmentBrewing;
import lucie.alchemist.enchantment.EnchantmentProficiency;
import net.minecraftforge.registries.ObjectHolder;

public class UtilityGetter
{
    @ObjectHolder("alchemist")
    public static class Enchantment
    {
        @ObjectHolder("brewing")
        public static EnchantmentBrewing BREWING;

        @ObjectHolder("proficiency")
        public static EnchantmentProficiency PROFICIENCY;
    }
}
