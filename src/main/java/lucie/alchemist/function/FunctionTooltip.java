package lucie.alchemist.function;

import lucie.alchemist.utility.UtilityGetter;
import lucie.alchemist.utility.UtilityTooltip;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static lucie.alchemist.utility.UtilityCompound.Tool;

@Mod.EventBusSubscriber(modid = "alchemist")
public class FunctionTooltip
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();

        // Stack need to be correct tool.
        if (EnchantmentHelper.getEnchantmentLevel(UtilityGetter.Enchantment.BREWING, stack) == 0) return;

        // Using tools instead of long ass lines.
        UtilityTooltip tooltip = new UtilityTooltip(event.getToolTip());
        Tool primary = Tool.convert(stack, true);
        Tool secondary = Tool.convert(stack, false);

        // Get color that name has, get max available potions, get amount of potions in use.
        TextFormatting name = stack.isEnchanted() ? TextFormatting.AQUA : stack.getRarity().color;
        int max = EnchantmentHelper.getEnchantmentLevel(UtilityGetter.Enchantment.BREWING, stack) > 0 ? 2 : 1;
        int count = primary.getUses() > 0 ? 1 : 0;
        count += secondary.getUses() > 0 ? 1 : 0;

        // Shows potions.
        if (Screen.hasShiftDown() && ((primary.doesExist() && primary.getUses() > 0) || (secondary.doesExist() && secondary.getUses() > 0)))
        {
            // Get name and when applied.
            tooltip.clear();

            // Show count of potions on name.
            tooltip.setCount(stack, name, count, max);
            tooltip.setColor(new String[]{I18n.format("tooltip.alchemist.potions")}, new TextFormatting[]{TextFormatting.GRAY});
            tooltip.space();
            tooltip.setColor(new String[]{I18n.format("potion.whenDrank")}, new TextFormatting[]{TextFormatting.GRAY});

            // Show primary potion.
            if (primary.doesExist() && primary.getUses() > 0)
            {
                tooltip.setPotion(new EffectInstance(primary.getEffect(), primary.getDuration(), primary.getAmplifier()), primary.getUses(), TextFormatting.DARK_GREEN);
            }

            // Show secondary potion.
            if (secondary.doesExist() && secondary.getUses() > 0)
            {
                tooltip.setPotion(new EffectInstance(secondary.getEffect(), secondary.getDuration(), secondary.getAmplifier()), secondary.getUses(), TextFormatting.DARK_GREEN);
            }
        }
        else if ((primary.doesExist() && primary.getUses() > 0) || (secondary.doesExist() && secondary.getUses() > 0))
        {
            // Show count of potions on name.
            event.getToolTip().remove(0);
            tooltip.setCount(stack, name, count, max);

            // Shift to show potion information.
            event.getToolTip().add(1, tooltip.getColor(new String[]{I18n.format("tooltip.alchemist.info")}, new TextFormatting[]{TextFormatting.DARK_GRAY}));
        }
    }
}
