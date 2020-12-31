package lucie.alchemist.function;

import com.google.gson.JsonObject;
import lucie.alchemist.item.AlchemicalItems;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid = "alchemist", bus = Mod.EventBusSubscriber.Bus.MOD)
public class FunctionDrops
{
    @SubscribeEvent
    public static void registerModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event)
    {
        event.getRegistry().register(new SeedDropSerializer().setRegistryName("alchemist", "seeds_drops"));
        event.getRegistry().register(new StickDropSerializer().setRegistryName("alchemist", "stick_drops"));
    }

    public static class StickDropSerializer extends GlobalLootModifierSerializer<StickDropModifier>
    {
        @Override
        public StickDropModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition)
        {
            return new StickDropModifier(ailootcondition);
        }

        @Override
        public JsonObject write(StickDropModifier instance)
        {
            return new JsonObject();
        }
    }

    private static class StickDropModifier extends LootModifier
    {
        protected StickDropModifier(ILootCondition[] conditionsIn)
        {
            super(conditionsIn);
        }

        @Nonnull
        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
        {
            generatedLoot.add(new ItemStack(AlchemicalItems.STICK));
            return generatedLoot;
        }
    }

    public static class SeedDropSerializer extends GlobalLootModifierSerializer<SeedDropModifier>
    {
        @Override
        public SeedDropModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition)
        {
            return new SeedDropModifier(ailootcondition);
        }

        @Override
        public JsonObject write(SeedDropModifier instance)
        {
            return new JsonObject();
        }
    }

    private static class SeedDropModifier extends LootModifier
    {
        protected SeedDropModifier(ILootCondition[] conditionsIn)
        {
            super(conditionsIn);
        }

        @Nonnull
        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
        {
            generatedLoot.add(new ItemStack(AlchemicalItems.SEEDS));
            return generatedLoot;
        }
    }
}
