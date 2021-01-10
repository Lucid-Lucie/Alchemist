package lucie.alchemist.function;

import lucie.alchemist.Alchemist;
import lucie.alchemist.utility.UtilityCompound;
import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.Random;

import static lucie.alchemist.utility.UtilityCompound.*;

@Mod.EventBusSubscriber(modid = "alchemist")
public class FunctionCampfire
{
    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.RightClickBlock event)
    {
        /*
        Alchemist.LOGGER.info(event.getPlayer().getHeldItem(Hand.MAIN_HAND));
        Alchemist.LOGGER.info(event.getPlayer().getHeldItem(Hand.OFF_HAND));
        Alchemist.LOGGER.info(event.getWorld().getBlockState(event.getPos()).getBlock());
        */

        ItemStack tool = event.getPlayer().getHeldItem(Hand.MAIN_HAND);
        ItemStack potion = event.getPlayer().getHeldItem(Hand.OFF_HAND);
        BlockState state = event.getWorld().getBlockState(event.getPos());

        int brewing = EnchantmentHelper.getEnchantmentLevel(UtilityGetter.Enchantment.BREWING, tool);

        // Check for tool, campfire, and potion.
        if (!checkTool(tool) || !checkCampfire(state)|| !potion.getItem().equals(Items.POTION)) return;

        Tool primary = Tool.convert(tool, true);
        Tool secondary = Tool.convert(tool, false);

        EffectInstance instance = PotionUtils.getEffectsFromStack(potion).get(0);
        String effect = Objects.requireNonNull(instance.getPotion().getRegistryName()).toString();

        // Check if slot is available.
        if (primary.getUses() != 0 && (brewing < 2 || secondary.getUses() > 0)) return;

        // Effect cant already be applied.
        if ((primary.getUses() != 0 && primary.getEffectName().equals(effect)) || secondary.getUses() != 0 && secondary.getEffectName().equals(effect)) return;

        if (!event.getWorld().isRemote)
        {
            int duration = instance.getDuration() == 1 ? 1 : instance.getDuration() / 10;
            int amplifier = instance.getAmplifier();
            int uses = getUses(EnchantmentHelper.getEnchantmentLevel(UtilityGetter.Enchantment.PROFICIENCY, tool));

            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("uses", uses);
            nbt.putInt("duration", duration);
            nbt.putInt("amplifier", amplifier);
            nbt.putString("effect", effect);

            if (tool.getTag() == null) tool.setTag(new CompoundNBT());

            if (primary.getUses() == 0)
            {
                if (!tool.getTag().contains("potion")) tool.getTag().put("potion", new CompoundNBT());

                tool.getTag().getCompound("potion").put("primary", nbt);
            }
            else if (brewing > 1)
            {
                // Add mixture on secondary.
                tool.getTag().getCompound("potion").put("secondary", nbt);
            }

            event.getPlayer().setHeldItem(Hand.OFF_HAND, new ItemStack(Items.GLASS_BOTTLE));
            event.getPlayer().setHeldItem(Hand.MAIN_HAND, tool);
            if (new Random().nextInt(5) == 0) event.getWorld().setBlockState(event.getPos(), state.getBlock().getDefaultState().with(CampfireBlock.LIT, false).with(CampfireBlock.FACING, state.get(CampfireBlock.FACING)));

            Alchemist.LOGGER.info(tool.getTag());
        }

        event.setCancellationResult(ActionResultType.SUCCESS);
        event.setCanceled(true);
    }

    private static boolean checkTool(ItemStack tool)
    {
        return tool.getMaxDamage() != 0 && EnchantmentHelper.getEnchantmentLevel(UtilityGetter.Enchantment.BREWING, tool) > 0;
    }

    private static boolean checkCampfire(BlockState state)
    {
        return (state.getBlock().equals(Blocks.CAMPFIRE) || state.getBlock().equals(Blocks.SOUL_CAMPFIRE)) && state.get(CampfireBlock.LIT);
    }

    private static int getUses(int level)
    {
        return level == 0 ? 16 : 16 + new Random().nextInt((16 * level) + 1);
    }
}
