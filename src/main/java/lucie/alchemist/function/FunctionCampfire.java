package lucie.alchemist.function;

import lucie.alchemist.utility.UtilityEffect;
import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(modid = "alchemist")
public class FunctionCampfire
{
    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.RightClickBlock event)
    {
        ItemStack tool = event.getPlayer().getHeldItem(Hand.MAIN_HAND);
        ItemStack potion = event.getPlayer().getHeldItem(Hand.OFF_HAND);
        BlockState state = event.getWorld().getBlockState(event.getPos());
        CampfireType campfire = CampfireType.getCampfire(state);
        ITag<Item> tag = ItemTags.getCollection().get(new ResourceLocation("alchemist:applicable_weapons"));

        System.out.println(campfire);

        // Check if block is campfire, tool is in tags, and potion is potion and hand is right hand.
        if (campfire == null || !potion.getItem().equals(Items.POTION) || event.getHand() == Hand.OFF_HAND || tag == null || !tag.contains(tool.getItem())) return;

        if (PotionUtils.getPotionFromItem(potion).equals(Potions.WATER) && UtilityEffect.hasEffect(tool))
        {
            // Cleaning
            UtilityEffect.purgeEffects(tool);
            event.getPlayer().setHeldItem(Hand.MAIN_HAND, tool);
            event.getPlayer().setHeldItem(Hand.OFF_HAND, new ItemStack(Items.GLASS_BOTTLE));
        }
        else
        {
            // Applying
            List<EffectInstance> effects = PotionUtils.getEffectsFromStack(potion);
            int brewing = EnchantmentHelper.getEnchantmentLevel(UtilityGetter.Enchantments.BREWING, tool);
            int amount = UtilityEffect.getEffects(tool).size() + 1;
            int uses = campfire.getMultiplier() * (EnchantmentHelper.getEnchantmentLevel(UtilityGetter.Enchantments.PROFICIENCY, tool) + 1);

            // Amount need to correspond with brewing, can't have duplicate effect, and potion need effects.
            if (amount > brewing || effects.isEmpty() || UtilityEffect.hasEffect(tool, effects.get(0).getPotion())) return;

            // Put effect into map and set itemstack with new effect.
            HashMap<EffectInstance, Integer> map = new HashMap<>();
            map.put(effects.get(0), uses);

            UtilityEffect.setEffects(map, tool);
        }

        if (!event.getWorld().isRemote)
        {
            // Server side stuff
            event.getPlayer().setHeldItem(Hand.MAIN_HAND, tool);
            event.getPlayer().setHeldItem(Hand.OFF_HAND, new ItemStack(Items.GLASS_BOTTLE));
        }
        else
        {
            // Client side stuff.
            event.getPlayer().playSound(SoundEvents.ITEM_BOTTLE_EMPTY, 1.0F, 1.0F);
            event.getPlayer().playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5F, 1.0F);
        }

        event.setCancellationResult(ActionResultType.SUCCESS);
        event.setCanceled(true);
    }

    private enum CampfireType
    {
        CAMPFIRE(16),
        SOUL_CAMPFIRE(24);

        private int multiplier;

        CampfireType(int multiplier)
        {
            this.multiplier = multiplier;
        }

        @Nullable
        public static CampfireType getCampfire(BlockState state)
        {
            if (!state.hasProperty(BlockStateProperties.LIT) || !state.get(BlockStateProperties.LIT)) return null;
            if (state.getBlock().equals(Blocks.CAMPFIRE)) return CAMPFIRE;
            if (state.getBlock().equals(Blocks.SOUL_CAMPFIRE)) return SOUL_CAMPFIRE;
            return null;
        }

        public int getMultiplier()
        {
            return multiplier;
        }
    }
}
