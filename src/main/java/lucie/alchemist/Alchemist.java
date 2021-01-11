package lucie.alchemist;

import lucie.alchemist.brewing.*;
import lucie.alchemist.effect.EffectDisplacement;
import lucie.alchemist.effect.EffectLifeTaking;
import lucie.alchemist.effect.EffectSoulDraining;
import lucie.alchemist.effect.EffectThieving;
import lucie.alchemist.enchantment.EnchantmentBrewing;
import lucie.alchemist.enchantment.EnchantmentProficiency;
import lucie.alchemist.potion.PotionBase;
import lucie.alchemist.utility.UtilityGetter.Effects;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("alchemist")
public class Alchemist
{
    public static final Logger LOGGER = LogManager.getLogger();

    public Alchemist()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        BrewingRecipeRegistry.addRecipe(new BrewingThieving());
        BrewingRecipeRegistry.addRecipe(new BrewingDisplacement());
        BrewingRecipeRegistry.addRecipe(new BrewingLifeTaking());
        BrewingRecipeRegistry.addRecipe(new BrewingSoulDraining());
        BrewingRecipeRegistry.addRecipe(new BrewingLevitation());
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onPotionsRegistry(RegistryEvent.Register<Potion> event)
        {
            event.getRegistry().register(new PotionBase("displacement", new EffectInstance(Effects.DISPLACEMENT, 1, 0)));
            event.getRegistry().register(new PotionBase("displacement_strong", new EffectInstance(Effects.DISPLACEMENT, 1, 1)));

            event.getRegistry().register(new PotionBase("life_taking", new EffectInstance(Effects.LIFE_TAKING, 1, 0)));
            event.getRegistry().register(new PotionBase("life_taking_strong", new EffectInstance(Effects.LIFE_TAKING, 1, 1)));

            event.getRegistry().register(new PotionBase("soul_draining", new EffectInstance(Effects.SOUL_DRAINING, 900)));
            event.getRegistry().register(new PotionBase("soul_draining_long", new EffectInstance(Effects.SOUL_DRAINING, 1800)));
            event.getRegistry().register(new PotionBase("soul_draining_strong", new EffectInstance(Effects.SOUL_DRAINING, 420, 1)));

            event.getRegistry().register(new PotionBase("thieving", new EffectInstance(Effects.THIEVING, 900)));
            event.getRegistry().register(new PotionBase("thieving_long", new EffectInstance(Effects.THIEVING, 1800)));
            event.getRegistry().register(new PotionBase("thieving_strong", new EffectInstance(Effects.THIEVING, 420, 1)));

            event.getRegistry().register(new PotionBase("levitation", new EffectInstance(net.minecraft.potion.Effects.LEVITATION, 200)));
            event.getRegistry().register(new PotionBase("levitation_long", new EffectInstance(net.minecraft.potion.Effects.LEVITATION, 400)));
        }

        @SubscribeEvent
        public static void onEffectsRegistry(RegistryEvent.Register<Effect> event)
        {
            event.getRegistry().register(new EffectLifeTaking());
            event.getRegistry().register(new EffectSoulDraining());
            event.getRegistry().register(new EffectThieving());
            event.getRegistry().register(new EffectDisplacement());
        }

        @SubscribeEvent
        public static void onEnchantmentsRegistry(RegistryEvent.Register<Enchantment> event)
        {
            event.getRegistry().register(new EnchantmentBrewing());
            event.getRegistry().register(new EnchantmentProficiency());
        }
    }
}
