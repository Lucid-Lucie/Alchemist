package lucie.alchemist.potion;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionBase extends Potion
{
    public PotionBase(String name, EffectInstance... effectsIn)
    {
        super(name, effectsIn);
        setRegistryName(new ResourceLocation("alchemist", name));
    }
}
