package lucie.alchemist.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class EffectCursedGreed extends Effect
{
    public EffectCursedGreed()
    {
        super(EffectType.HARMFUL, 0xedc835);
        this.setRegistryName("cursed_greed");
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (!entityLivingBaseIn.getEntityWorld().isRemote)
        {
            ItemEntity item = new ItemEntity(entityLivingBaseIn.getEntityWorld(), entityLivingBaseIn.getPosX(), entityLivingBaseIn.getPosY(), entityLivingBaseIn.getPosZ(), new ItemStack(Items.EMERALD));

            double angle = Math.random()*Math.PI*2;

            item.setVelocity(Math.cos(angle)*0.15, 0.3, Math.sin(angle)*0.15);

            item.setPickupDelay(20);

            entityLivingBaseIn.world.addEntity(item);

            entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, 1.0F);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int j = 100 >> amplifier;

        return j <= 0 || duration % j == 0;
    }
}
