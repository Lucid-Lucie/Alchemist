package lucie.alchemist;

import lucie.alchemist.effect.EffectDisplacement;
import lucie.alchemist.effect.EffectLifeTaking;
import lucie.alchemist.effect.EffectSoulDraining;
import lucie.alchemist.effect.EffectThieving;
import lucie.alchemist.enchantment.EnchantmentKnowledge;
import lucie.alchemist.enchantment.EnchantmentMastery;
import lucie.alchemist.enchantment.EnchantmentProficiency;
import lucie.alchemist.init.InitializeEnchantments;
import lucie.alchemist.init.InitializeItems;
import lucie.alchemist.init.InitializeItems.ItemType;
import lucie.alchemist.item.ItemMixture;
import lucie.alchemist.item.ItemPestle;
import lucie.alchemist.item.ItemSimple;
import net.minecraft.block.ComposterBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod("alchemist")
public class Alchemist
{
    public static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup GROUP = new ItemGroup("alchemist.materials")
    {
        @Nonnull
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(InitializeItems.POUCH);
        }

        @Override
        public void fill(@Nonnull NonNullList<ItemStack> items)
        {
            super.fill(items);

            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(InitializeEnchantments.PROFICIENCY, 1)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(InitializeEnchantments.PROFICIENCY, 2)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(InitializeEnchantments.PROFICIENCY, 3)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(InitializeEnchantments.KNOWLEDGE, 1)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(InitializeEnchantments.MASTERY, 1)));
        }
    };

    public Alchemist()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ComposterBlock.CHANCES.put(InitializeItems.SEEDS, 0.3F);
        ComposterBlock.CHANCES.put(InitializeItems.STICK, 0.15F);
        ComposterBlock.CHANCES.put(InitializeItems.ESSENCE, 0.9F);
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        ItemModelsProperties.registerProperty(InitializeItems.SLOWNESS, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(InitializeItems.DISPLACEMENT, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(InitializeItems.LEVITATION, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(InitializeItems.THIEVING, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(InitializeItems.SOUL_DRAINING, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(InitializeItems.LIFE_TAKING, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onPotionsRegistry(RegistryEvent.Register<Effect> event)
        {
            event.getRegistry().register(new EffectSoulDraining());
            event.getRegistry().register(new EffectLifeTaking());
            event.getRegistry().register(new EffectThieving());
            event.getRegistry().register(new EffectDisplacement());
        }

        @SubscribeEvent
        public static void onEnchantmentsRegistry(RegistryEvent.Register<Enchantment> event)
        {
            // Tools.
            event.getRegistry().register(new EnchantmentKnowledge());

            // Pestle.
            event.getRegistry().register(new EnchantmentProficiency());
            event.getRegistry().register(new EnchantmentMastery());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
        {
            // Tools.
            event.getRegistry().register(new ItemSimple("pouch", ItemType.TOOL));
            event.getRegistry().register(new ItemPestle());

            // Materials.
            event.getRegistry().register(new ItemSimple("stick", ItemType.MATERIAL));
            event.getRegistry().register(new ItemSimple("essence", ItemType.MATERIAL));
            event.getRegistry().register(new ItemSimple("seeds", ItemType.MATERIAL));
            event.getRegistry().register(new ItemSimple("leather", ItemType.MATERIAL));

            // Ingredients.
            event.getRegistry().register(new ItemSimple("ash", ItemType.INGREDIENT));

            // Mixtures.
            event.getRegistry().register(new ItemMixture("slowness"));
            event.getRegistry().register(new ItemMixture("displacement"));
            event.getRegistry().register(new ItemMixture("levitation"));
            event.getRegistry().register(new ItemMixture("thieving"));
            event.getRegistry().register(new ItemMixture("soul_draining"));
            event.getRegistry().register(new ItemMixture("life_taking"));
        }
    }
}
