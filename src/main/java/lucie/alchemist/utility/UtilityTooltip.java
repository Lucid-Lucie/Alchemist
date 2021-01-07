package lucie.alchemist.utility;

import lucie.alchemist.Alchemist;
import lucie.alchemist.init.InitializeItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class UtilityTooltip
{
    private List<ITextComponent> tooltip;

    public UtilityTooltip(List<ITextComponent> tooltip)
    {
        this.tooltip = tooltip;
    }

    // Clears tooltip.
    public void clear()
    {
        tooltip.clear();
    }

    // Adds empty space to tooltip.
    public void space()
    {
        tooltip.add(new StringTextComponent(""));
    }

    // Adds Journal.
    public void journal(InitializeItems.ItemType type, String name, int page)
    {
        if (!Screen.hasShiftDown()) return;

        clear();
        setColor(new String[]{I18n.format("title.alchemist." + type.getJournal()) + ": ", I18n.format("title.alchemist.page", page)}, new TextFormatting[]{type.getRarity().color, TextFormatting.WHITE});
        setTrim("\"" + I18n.format("journal.alchemist." + name) + "\"", 3);
    }

    /* Getters and Setters */

    // Sets trimmed strings
    public void setTrim(String localized, int rows)
    {
        tooltip.addAll(getTrim(localized, rows));
    }

    // Gets trimmed strings.
    public List<StringTextComponent> getTrim(String localized, int rows)
    {
        rows = localized.length() / rows;

        List<StringTextComponent> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder(); // String factory
        Style style = Style.EMPTY.setFormatting(TextFormatting.GRAY).setItalic(true);
        StringTextComponent s;

        int size = 0;

        for (int i = 0; i < localized.length(); i++)
        {
            char c = localized.charAt(i);

            // If the size is greater than specified length / rows, clip the string.
            if (c == ' ' && size >= rows)
            {
                s = new StringTextComponent(builder.toString());
                s.setStyle(style);
                list.add(s);

                builder = new StringBuilder();
                size = 0;
                continue;
            }

            size += 1;
            builder.append(c);
        }

        // Get last row of text.
        s = new StringTextComponent(builder.toString());
        s.setStyle(style);
        list.add(s);

        return list;
    }

    // Sets specified colors to to text with index.
    public void setColor(String[] texts, TextFormatting[] formatting)
    {
        tooltip.add(getColor(texts, formatting));
    }

    // Gets specified colors to to text with index.
    public StringTextComponent getColor(String[] texts, TextFormatting[] formatting)
    {
        if (texts.length != formatting.length)
        {
            // Text need to have as many items as formatting.
            Alchemist.LOGGER.error("colorTooltip mismatch! Texts: '" + texts.length + "', Formatting: '" + formatting.length + "'. Returning without formatting.");
            return null;
        }

        StringTextComponent output = new StringTextComponent("");

        for (int i = 0; i < texts.length; i++)
        {
            StringTextComponent c = new StringTextComponent(texts[i]);
            c.setStyle(Style.EMPTY.setFormatting(formatting[i]));
            output.append(c);
        }

        return output;
    }

    // Sets effect text with specified color.
    public void setEffect(EffectInstance effect, TextFormatting color)
    {
        tooltip.add(getEffect(effect, color));
    }

    // Gets effect text with specified color.
    public StringTextComponent getEffect(EffectInstance effect, TextFormatting color)
    {
        int seconds, t1, t2, t3;
        String text;

        // Ticks to Seconds with parenthesis, example: 300 = (00:15)

        seconds = effect.getDuration() / 20;

        t1 = seconds % 60;
        t2 = seconds / 60;
        t3 = t2 % 60;
        t2 = t2 / 60;

        String time = t3 < 10 ? "(0" + t3 + ":" : "(" + t3 + ":";
        time = t1 < 10 ? time + "0" + t1 + ")" : time + t1 + ")";
        if (seconds > 3599) time = t2 + ":" + time;

        if (effect.getAmplifier() != 0)
        {
            String amplifier;

            // Turn arabic to numeral
            switch (effect.getAmplifier() + 1)
            {
                case 2: amplifier = "II"; break;
                case 3: amplifier = "III"; break;
                case 4: amplifier = "IV"; break;
                case 5: amplifier = "V"; break;
                default: amplifier = "?"; break;
            }

            // String with amplifier
            if (effect.getDuration() == 1)
            {
                text = I18n.format(effect.getEffectName()) + " " + amplifier;
            }
            else
            {
                text = I18n.format(effect.getEffectName()) + " " + amplifier + " " + time;
            }

        }
        else
        {
            // String without amplifier
            if (effect.getDuration() == 1)
            {
                text = I18n.format(effect.getEffectName());
            }
            else
            {
                text = I18n.format(effect.getEffectName()) + " " + time;
            }

        }

        StringTextComponent component = new StringTextComponent(" " + text);
        component.setStyle(Style.EMPTY.setFormatting(color));
        return component;
    }

    // Sets mixture tooltip.
    public void setMixture(EffectInstance effect, int uses, TextFormatting color)
    {
        tooltip.add(getMixture(effect, uses, color));
    }

    // Gets mixture tooltip.
    public StringTextComponent getMixture(EffectInstance effect, int uses, TextFormatting color)
    {
        StringTextComponent amount = getColor(new String[]{" " + uses}, new TextFormatting[]{color});
        StringTextComponent potion = getEffect(effect, color);
        amount.append(potion);
        return amount;
    }

    // Sets mixture count.
    public void setCount(ItemStack stack, TextFormatting color, int count, int max)
    {
        tooltip.add(0, getCount(stack, color, count, max));
    }

    // Gets mixture count.
    public StringTextComponent getCount(ItemStack stack, TextFormatting color, int count, int max)
    {
        return getColor(new String[]{stack.getDisplayName().getString() + ": ", "[", String.valueOf(count), "/", String.valueOf(max), "]"}, new TextFormatting[]{color, TextFormatting.GRAY, TextFormatting.WHITE, TextFormatting.GRAY, TextFormatting.WHITE, TextFormatting.GRAY});
    }
}
