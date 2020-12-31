package lucie.alchemist;

import lucie.alchemist.effect.EffectArmorTeleportation;
import lucie.alchemist.effect.EffectCursedGreed;
import lucie.alchemist.effect.EffectLifeTaking;
import lucie.alchemist.effect.EffectSoulDraining;
import lucie.alchemist.enchantment.AlchemicalEnchantments;
import lucie.alchemist.enchantment.EnchantmentKnowledge;
import lucie.alchemist.enchantment.EnchantmentMastery;
import lucie.alchemist.enchantment.EnchantmentProficiency;
import lucie.alchemist.item.*;
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

        @Override
        public void fill(NonNullList<ItemStack> items)
        {
            super.fill(items);

            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(AlchemicalEnchantments.PROFICIENCY, 1)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(AlchemicalEnchantments.PROFICIENCY, 2)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(AlchemicalEnchantments.PROFICIENCY, 3)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(AlchemicalEnchantments.KNOWLEDGE, 1)));
            items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(AlchemicalEnchantments.MASTERY, 1)));
        }
    };

    public static final ItemGroup GROUP_MIXTURES = new ItemGroup("alchemist.mixtures")
    {
        @Override
        public ItemStack createIcon()
        {
            return AlchemicalItems.SOUL_DRAINING.compoundWrite(new ItemStack(AlchemicalItems.LIFE_TAKING), 0, true);
        }
    };

    public Alchemist()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        ItemModelsProperties.registerProperty(AlchemicalItems.SLOWNESS, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(AlchemicalItems.ARMOR_TELEPORTATION, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(AlchemicalItems.LEVITATION, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(AlchemicalItems.CURSED_GREED, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(AlchemicalItems.SOUL_DRAINING, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
        ItemModelsProperties.registerProperty(AlchemicalItems.LIFE_TAKING, new ResourceLocation("mixture"), (stack, world, entity) -> ItemMixture.compoundCheck(stack) ? 1 : 0);
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
        public static void onEnchantmentsRegistry(RegistryEvent.Register<Enchantment> event)
        {
            event.getRegistry().register(new EnchantmentProficiency());
            event.getRegistry().register(new EnchantmentMastery());
            event.getRegistry().register(new EnchantmentKnowledge());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
        {
            // Tools.
            event.getRegistry().register(new ItemPouch());
            event.getRegistry().register(new ItemPestle());

            // Ingredients.
            event.getRegistry().register(new ItemIngredient("stick"));
            event.getRegistry().register(new ItemIngredient("compost"));
            event.getRegistry().register(new ItemIngredient("seeds"));
            event.getRegistry().register(new ItemIngredient("leather"));

            // Mixtures.
            event.getRegistry().register(new ItemMixture("slowness"));
            event.getRegistry().register(new ItemMixture("armor_teleportation"));
            event.getRegistry().register(new ItemMixture("levitation"));
            event.getRegistry().register(new ItemMixture("cursed_greed"));
            event.getRegistry().register(new ItemMixture("soul_draining"));
            event.getRegistry().register(new ItemMixture("life_taking"));
        }
    }
}
