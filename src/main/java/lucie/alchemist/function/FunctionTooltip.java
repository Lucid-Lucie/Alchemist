package lucie.alchemist.function;

import lucie.alchemist.utility.UtilityEffect;
import lucie.alchemist.utility.UtilityGetter;
import lucie.alchemist.utility.UtilityKeybind;
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

import java.util.Map;

@Mod.EventBusSubscriber(modid = "alchemist")
public class FunctionTooltip
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event)
    {
        // Stack need to be correct tool.
        int brewing = EnchantmentHelper.getEnchantmentLevel(UtilityGetter.Enchantments.BREWING, event.getItemStack());
        if (brewing == 0) return;

        ItemStack stack = event.getItemStack();
        UtilityTooltip tooltip = new UtilityTooltip(event.getToolTip());
        TextFormatting name = stack.isEnchanted() ? TextFormatting.AQUA : stack.getRarity().color;

        if (Screen.hasShiftDown() && UtilityKeybind.INFO.isKeyDown() && UtilityEffect.hasEffect(stack))
        {
            // Get name and when applied.
            tooltip.clear();

            // Show count of potions on name.
            tooltip.setCount(stack, name, UtilityEffect.getEffects(stack).size(), brewing);
            tooltip.setColor(new String[]{I18n.format("tooltip.alchemist.potions")}, new TextFormatting[]{TextFormatting.GRAY});
            tooltip.space();
            tooltip.setColor(new String[]{I18n.format("potion.whenDrank")}, new TextFormatting[]{TextFormatting.GRAY});

            // Show all potions.
            for (Map.Entry<EffectInstance, Integer> map : UtilityEffect.getEffects(stack).entrySet())
            {
                tooltip.setPotion(map.getKey(), map.getValue(), TextFormatting.DARK_GREEN);
            }
        }
        else if (UtilityEffect.hasEffect(stack))
        {
            // Show count of potions on name.
            event.getToolTip().remove(0);
            tooltip.setCount(stack, name, UtilityEffect.getEffects(stack).size(), brewing);

            // Shift to show potion information.
            event.getToolTip().add(1, tooltip.getColor(new String[]{I18n.format("tooltip.alchemist.info", UtilityKeybind.INFO.func_238171_j_().getString().toUpperCase())}, new TextFormatting[]{TextFormatting.DARK_GRAY}));
        }
    }
}
