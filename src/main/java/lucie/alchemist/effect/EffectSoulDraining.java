package lucie.alchemist.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class EffectSoulDraining extends Effect
{
    public EffectSoulDraining()
    {
        super(EffectType.HARMFUL, 0x7cf2f5);
        setRegistryName("soul_draining");
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn.getHealth() > 1.0F)
        {
            entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, 1.0F);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int j = 25 >> amplifier;

        return j <= 0 || duration % j == 0;
    }
}
