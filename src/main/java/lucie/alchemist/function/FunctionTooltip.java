package lucie.alchemist.function;

import lucie.alchemist.init.InitializeEnchantments;
import lucie.alchemist.utility.UtilityCompound;
import lucie.alchemist.utility.UtilityGetter;
import lucie.alchemist.utility.UtilityTooltip;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "alchemist")
public class FunctionTooltip
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        ITag<Item> tools = ItemTags.getCollection().get(new ResourceLocation("alchemist", "tools"));

        // Stack need to be correct tool.
        if (tools == null || !tools.contains(stack.getItem())) return;

        // Using tools instead of long ass lines.
        UtilityTooltip tooltip = new UtilityTooltip(event.getToolTip());
        UtilityCompound.Tool primary = UtilityCompound.Tool.convert(stack, true);
        UtilityCompound.Tool secondary = UtilityCompound.Tool.convert(stack, false);

        // Get color that name has, get max available mixtures, get amount of mixtures in use.
        TextFormatting name = stack.isEnchanted() ? TextFormatting.AQUA : stack.getRarity().color;
        int max = EnchantmentHelper.getEnchantmentLevel(InitializeEnchantments.KNOWLEDGE, stack) > 0 ? 2 : 1;
        int count = primary.getUses() > 0 ? 1 : 0;
        count += secondary.getUses() > 0 ? 1 : 0;

        // Shows mixtures.
        if (Screen.hasShiftDown() && ((primary.doesExist() && primary.getUses() > 0) || (secondary.doesExist() && secondary.getUses() > 0)))
        {
            // Get name and when applied.
            tooltip.clear();

            // Show count of mixtures on name.
            tooltip.setCount(stack, name, count, max);
            tooltip.setColor(new String[]{I18n.format("tooltip.alchemist.mixtures")}, new TextFormatting[]{TextFormatting.GRAY});
            tooltip.space();
            tooltip.setColor(new String[]{I18n.format("potion.whenDrank")}, new TextFormatting[]{TextFormatting.GRAY});

            // Show primary mixture.
            if (primary.doesExist() && primary.getUses() > 0)
            {
                tooltip.setMixture(UtilityGetter.getEffectInstance(new ResourceLocation(primary.getEffect()), primary.getDuration(), primary.getAmplifier()), primary.getUses(), TextFormatting.DARK_GREEN);
            }

            // Show secondary mixture.
            if (secondary.doesExist() && secondary.getUses() > 0)
            {
                tooltip.setMixture(UtilityGetter.getEffectInstance(new ResourceLocation(secondary.getEffect()), secondary.getDuration(), secondary.getAmplifier()), secondary.getUses(), TextFormatting.DARK_GREEN);
            }
        }
        else if ((primary.doesExist() && primary.getUses() > 0) || (secondary.doesExist() && secondary.getUses() > 0))
        {
            // Show count of mixtures on name.
            event.getToolTip().remove(0);
            tooltip.setCount(stack, name, count, max);

            // Shift to show mixture information.
            event.getToolTip().add(1, tooltip.getColor(new String[]{I18n.format("tooltip.alchemist.info")}, new TextFormatting[]{TextFormatting.GRAY}));
        }
    }
}
