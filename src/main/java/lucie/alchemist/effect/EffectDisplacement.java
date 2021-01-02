package lucie.alchemist.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.InstantEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static net.minecraft.inventory.EquipmentSlotType.*;

public class EffectDisplacement extends InstantEffect
{
    protected static final Random random = new Random();

    public EffectDisplacement()
    {
        super(EffectType.HARMFUL, 0xcfa0f3);
        setRegistryName("displacement");
    }

    @Override
    public void performEffect(@Nonnull LivingEntity entityLivingBaseIn, int amplifier)
    {
        displace(entityLivingBaseIn, null, null, amplifier);
    }

    @Override
    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, @Nonnull LivingEntity entityLivingBaseIn, int amplifier, double health)
    {
        displace(entityLivingBaseIn, source, indirectSource, amplifier);
    }

    private void displace(@Nonnull LivingEntity entityLivingBaseIn, @Nullable Entity source, @Nullable Entity indirectSource, int amplifier)
    {
        if ((amplifier >= 5 || random.nextInt(5 - amplifier) == 0))
        {
            List<EquipmentSlotType> list = new ArrayList<>();
            if (entityLivingBaseIn.hasItemInSlot(HEAD)) list.add(HEAD);
            if (entityLivingBaseIn.hasItemInSlot(CHEST)) list.add(CHEST);
            if (entityLivingBaseIn.hasItemInSlot(LEGS)) list.add(LEGS);
            if (entityLivingBaseIn.hasItemInSlot(FEET)) list.add(FEET);

            if (list.isEmpty()) return;

            Collections.shuffle(list);

            ItemStack stack = entityLivingBaseIn.getItemStackFromSlot(list.get(0));

            ItemEntity item = new ItemEntity(entityLivingBaseIn.getEntityWorld(), entityLivingBaseIn.getPosX(), entityLivingBaseIn.getPosY(), entityLivingBaseIn.getPosZ(), stack);

            double angle = Math.random()*Math.PI*2;

            item.setVelocity(Math.cos(angle)*0.15, 0.3, Math.sin(angle)*0.15);

            item.setPickupDelay(60);

            entityLivingBaseIn.world.addEntity(item);

            if (source == null)
            {
                entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, 1.0F);
            }
            else
            {
                entityLivingBaseIn.attackEntityFrom(DamageSource.causeIndirectMagicDamage(source, indirectSource), 1.0F);
            }

            entityLivingBaseIn.setItemStackToSlot(list.get(0), ItemStack.EMPTY);
        }
    }
}
