package lucie.alchemist.effect;

import lucie.alchemist.Alchemist;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class EffectThieving extends Effect
{
    public static HashMap<ResourceLocation, ResourceLocation> THIEVING = new HashMap<>();

    public EffectThieving()
    {
        super(EffectType.HARMFUL, 0xedc835);
        setRegistryName("thieving");
    }

    @Override
    public void performEffect(@Nonnull LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn.world.isRemote || EffectThieving.THIEVING.isEmpty()) return;
        EntityType<?> type = entityLivingBaseIn.getType();
        ITag<EntityType<?>> tag;

        for (Map.Entry<ResourceLocation, ResourceLocation> map : EffectThieving.THIEVING.entrySet())
        {
            tag = EntityTypeTags.getCollection().get(map.getKey());
            if (tag == null)
            {
                Alchemist.LOGGER.warn("Couldn't find entity tag '" + map.getKey() + "'");
                continue;
            }

            if (tag.contains(type))
            {
                LootTable table = ServerLifecycleHooks.getCurrentServer().getLootTableManager().getLootTableFromLocation(map.getValue());
                LootContext.Builder builder = new LootContext.Builder((ServerWorld) entityLivingBaseIn.world);
                ItemEntity item;

                for (ItemStack stack : table.generate(builder.build(LootParameterSets.EMPTY)))
                {
                    double angle = Math.random()*Math.PI*2;

                    item = new ItemEntity(entityLivingBaseIn.getEntityWorld(), entityLivingBaseIn.getPosX(), entityLivingBaseIn.getPosY(), entityLivingBaseIn.getPosZ(), stack);
                    item.setMotion(Math.cos(angle)*0.15, 0.3, Math.sin(angle)*0.15);
                    item.setPickupDelay(20);

                    entityLivingBaseIn.world.addEntity(item);
                }

                entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, 1.0F);
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int j = 100 >> amplifier;

        return j <= 0 || duration % j == 0;
    }
}

