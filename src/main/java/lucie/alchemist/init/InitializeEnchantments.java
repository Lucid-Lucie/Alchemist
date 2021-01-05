package lucie.alchemist.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("alchemist")
public class InitializeEnchantments
{
    /* Pestle*/

    @ObjectHolder("proficiency")
    public static Enchantment PROFICIENCY;

    @ObjectHolder("mastery")
    public static Enchantment MASTERY;

    /* Tools */

    @ObjectHolder("knowledge")
    public static Enchantment KNOWLEDGE;
}
