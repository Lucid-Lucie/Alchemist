package lucie.alchemist;

import lucie.alchemist.enchantment.EnchantmentBrewing;
import lucie.alchemist.enchantment.EnchantmentProficiency;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("alchemist")
public class Alchemist
{
    public static final Logger LOGGER = LogManager.getLogger();

    public Alchemist()
    {

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onEffectsRegistry(RegistryEvent.Register<Effect> event)
        {

        }

        @SubscribeEvent
        public static void onEnchantmentsRegistry(RegistryEvent.Register<Enchantment> event)
        {
            event.getRegistry().register(new EnchantmentBrewing());
            event.getRegistry().register(new EnchantmentProficiency());
        }
    }
}
