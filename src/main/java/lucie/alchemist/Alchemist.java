package lucie.alchemist;

import lucie.alchemist.block.AlchemicalBlocks;
import lucie.alchemist.block.BlockCampfire;
import lucie.alchemist.effect.EffectDisplacement;
import lucie.alchemist.effect.EffectThieving;
import lucie.alchemist.effect.EffectLifeTaking;
import lucie.alchemist.effect.EffectSoulDraining;
import lucie.alchemist.enchantment.AlchemicalEnchantments;
import lucie.alchemist.enchantment.EnchantmentKnowledge;
import lucie.alchemist.enchantment.EnchantmentMastery;
import lucie.alchemist.enchantment.EnchantmentProficiency;
import lucie.alchemist.item.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod("alchemist")
public class Alchemist
{
    public static final Logger LOGGER = LogManager.getLogger();

    public static final Rarity RARITY = Rarity.create("alchemist", TextFormatting.YELLOW);

    public static final ItemGroup GROUP = new ItemGroup("alchemist.materials")
    {
        @Nonnull
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(AlchemicalItems.POUCH);
        }

        @Override
        public void fill(@Nonnull NonNullList<ItemStack> items)
        {
            super.fill(items);

            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(AlchemicalEnchantments.PROFICIENCY, 1)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(AlchemicalEnchantments.PROFICIENCY, 2)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(AlchemicalEnchantments.PROFICIENCY, 3)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(AlchemicalEnchantments.KNOWLEDGE, 1)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(AlchemicalEnchantments.MASTERY, 1)));
        }
    };

    public Alchemist()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        ItemModelsProperties.registerProperty(AlchemicalItems.SLOWNESS, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(AlchemicalItems.DISPLACEMENT, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(AlchemicalItems.LEVITATION, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(AlchemicalItems.THIEVING, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(AlchemicalItems.SOUL_DRAINING, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(AlchemicalItems.LIFE_TAKING, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);

        RenderTypeLookup.setRenderLayer(AlchemicalBlocks.CAMPFIRE, RenderType.getCutout());
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
            event.getRegistry().register(new EnchantmentProficiency());
            event.getRegistry().register(new EnchantmentMastery());
            event.getRegistry().register(new EnchantmentKnowledge());
        }

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event)
        {
            event.getRegistry().register(new BlockCampfire());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
        {
            // Tools.
            event.getRegistry().register(new ItemPouch());
            event.getRegistry().register(new ItemPestle());

            // Materials.
            event.getRegistry().register(new ItemMaterial("stick"));
            event.getRegistry().register(new ItemMaterial("essence"));
            event.getRegistry().register(new ItemMaterial("seeds"));
            event.getRegistry().register(new ItemMaterial("leather"));

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
