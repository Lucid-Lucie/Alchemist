package lucie.alchemist.utility;

import lucie.alchemist.Alchemist;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilityGetter
{
    public static Effect getEffect(ResourceLocation effect)
    {
        Effect e = ForgeRegistries.POTIONS.getValue(effect);

        if (e == null)
        {
            Alchemist.LOGGER.error("Effect '" + effect + "' isn't a potion effect or doesn't exist in registries, returning hunger.");
            e = Effects.HUNGER;
        }

        return e;
    }

    public static EffectInstance getEffectInstance(ResourceLocation effect, int duration, int amplifier)
    {
        Effect e = getEffect(effect);

        return new EffectInstance(e, duration, amplifier);
    }

}
