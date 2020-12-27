package lucie.alchemist;

import lucie.alchemist.item.ItemPestle;
import lucie.alchemist.item.ItemPouch;
import net.minecraft.item.*;
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

    public static final ItemGroup GROUP = new ItemGroup("alchemist")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Items.LEATHER);
        }
    };

    public Alchemist()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
        {
            event.getRegistry().register(new ItemPouch());
            event.getRegistry().register(new ItemPestle());
        }
    }
}
