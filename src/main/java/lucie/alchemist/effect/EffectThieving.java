package lucie.alchemist.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EffectThieving extends Effect
{
    private static final List<EntityType<?>> EMERALD = new ArrayList<>(Arrays.asList(EntityType.VILLAGER, EntityType.PILLAGER, EntityType.WITCH, EntityType.EVOKER, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.WANDERING_TRADER, EntityType.ZOMBIE_VILLAGER));

    private static final List<EntityType<?>> GOLD = new ArrayList<>(Arrays.asList(EntityType.PIGLIN, EntityType.ZOMBIFIED_PIGLIN));

    private static final List<Item> ITEMS = new ArrayList<>(Arrays.asList(Items.GOLDEN_CARROT, Items.GOLD_NUGGET, Items.GOLD_INGOT, Items.GOLDEN_HORSE_ARMOR, Items.GOLDEN_APPLE));

    public EffectThieving()
    {
        super(EffectType.HARMFUL, 0xedc835);
        setRegistryName("thieving");
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        ItemStack stack = null;

        if (EMERALD.contains(entityLivingBaseIn.getType()))
        {
            stack = new ItemStack(Items.EMERALD);
        }

        if (GOLD.contains(entityLivingBaseIn.getType()))
        {
            stack = new ItemStack(ITEMS.get(new Random().nextInt(ITEMS.size())));
        }

        if (!entityLivingBaseIn.getEntityWorld().isRemote && stack != null)
        {
            ItemEntity item = new ItemEntity(entityLivingBaseIn.getEntityWorld(), entityLivingBaseIn.getPosX(), entityLivingBaseIn.getPosY(), entityLivingBaseIn.getPosZ(), stack);

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

