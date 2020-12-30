package lucie.alchemist;

import lucie.alchemist.effect.EffectArmorTeleportation;
import lucie.alchemist.effect.EffectCursedGreed;
import lucie.alchemist.effect.EffectLifeTaking;
import lucie.alchemist.effect.EffectSoulDraining;
import lucie.alchemist.item.AlchemicalItems;
import lucie.alchemist.item.ItemMixture;
import lucie.alchemist.item.ItemPestle;
import lucie.alchemist.item.ItemPouch;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("alchemist")
public class Alchemist
{
    public static final Logger LOGGER = LogManager.getLogger();

    public static final Rarity RARITY = Rarity.create("alchemist", TextFormatting.YELLOW);

    public static final ItemGroup GROUP_INGREDIENTS = new ItemGroup("alchemist.ingredients")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(AlchemicalItems.POUCH);
        }
    };

    public static final ItemGroup GROUP_MIXTURES = new ItemGroup("alchemist.mixtures")
    {
        @Override
        public ItemStack createIcon()
        {
            return AlchemicalItems.SOUL_DRAINING.compoundWrite(new ItemStack(AlchemicalItems.SOUL_DRAINING), 0, true);
        }
    };



    public Alchemist()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        ItemModelsProperties.registerProperty(AlchemicalItems.SOUL_DRAINING, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onPotionsRegistry(RegistryEvent.Register<Effect> event)
        {
            event.getRegistry().register(new EffectSoulDraining());
            event.getRegistry().register(new EffectLifeTaking());
            event.getRegistry().register(new EffectCursedGreed());
            event.getRegistry().register(new EffectArmorTeleportation());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
        {
            event.getRegistry().register(new ItemPouch());
            event.getRegistry().register(new ItemPestle());

            event.getRegistry().register(new ItemMixture("soul_draining"));
        }
    }
}
