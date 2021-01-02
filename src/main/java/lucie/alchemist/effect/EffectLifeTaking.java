package lucie.alchemist.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.InstantEffect;
import net.minecraft.util.DamageSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EffectLifeTaking extends InstantEffect
{
    public EffectLifeTaking()
    {
        super(EffectType.HARMFUL, 0xd2443f);
        setRegistryName("life_taking");
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, (float)(6 << amplifier));
    }

    @Override
    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, @Nonnull LivingEntity entityLivingBaseIn, int amplifier, double health)
    {
        int j = (int)(health * (double)(6 << amplifier) + 0.5D);

        if (source == null)
        {
            entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, (float)j);
        }
        else
        {
            entityLivingBaseIn.attackEntityFrom(DamageSource.causeIndirectMagicDamage(source, indirectSource), (float)j);
        }
    }
}
