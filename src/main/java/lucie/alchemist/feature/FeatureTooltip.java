package lucie.alchemist.feature;

import lucie.alchemist.utility.UtilityCompound;
import lucie.alchemist.utility.UtilityGetter;
import lucie.alchemist.utility.UtilityTooltip;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "alchemist")
public class FeatureTooltip
{
    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();

        // Stack need to be correct tool.
        if (!FeatureTools.getItems().contains(stack.getItem())) return;

        // Using tools instead of long ass lines.
        UtilityTooltip tooltip = new UtilityTooltip(event.getToolTip());
        UtilityCompound.Tool primary = UtilityCompound.Tool.convert(stack, true);
        UtilityCompound.Tool secondary = UtilityCompound.Tool.convert(stack, false);
        TextFormatting color = stack.getRarity().color.equals(TextFormatting.WHITE) ? TextFormatting.DARK_GREEN : TextFormatting.WHITE;
        ResourceLocation location;

        // Shows mixtures.
        if (Screen.hasShiftDown() && ((primary.doesExist() && primary.getUses() > 0) || (secondary.doesExist() && secondary.getUses() > 0)))
        {
            // Get name and when applied.
            tooltip.clear();
            tooltip.color(new String[]{stack.getDisplayName().getString()}, new TextFormatting[]{stack.getRarity().color});
            tooltip.color(new String[]{I18n.format("potion.whenDrank")}, new TextFormatting[]{TextFormatting.GRAY});
            tooltip.space();

            // Show primary mixture.
            if (primary.doesExist() && primary.getUses() > 0)
            {
                location = new ResourceLocation(primary.getEffect());

                tooltip.mixture(UtilityGetter.getEffectInstance(location, primary.getDuration(), primary.getAmplifier()), primary.getUses(), location, stack.getRarity().color, color);
            }

            // Show secondary mixture.
            if (secondary.doesExist() && secondary.getUses() > 0)
            {
                if (primary.doesExist() && primary.getUses() > 0) tooltip.space();

                location = new ResourceLocation(secondary.getEffect());

                tooltip.mixture(UtilityGetter.getEffectInstance(location, secondary.getDuration(), secondary.getAmplifier()), secondary.getUses(), location, stack.getRarity().color, color);
            }
        }
        else if ((primary.doesExist() && primary.getUses() > 0) || (secondary.doesExist() && secondary.getUses() > 0))
        {
            // Shift to show mixture information.
            tooltip.color(new String[]{I18n.format("tooltip.alchemist.info")}, new TextFormatting[]{TextFormatting.DARK_GRAY});
        }
    }
}
