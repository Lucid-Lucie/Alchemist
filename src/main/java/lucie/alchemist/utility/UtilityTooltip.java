package lucie.alchemist.utility;

import lucie.alchemist.Alchemist;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class UtilityTooltip
{
    private List<ITextComponent> tooltip;

    public UtilityTooltip(List<ITextComponent> tooltip)
    {
        this.tooltip = tooltip;
    }

    public void color(String text, TextFormatting color)
    {
        StringTextComponent component = new StringTextComponent(text);
        component.setStyle(Style.EMPTY.setFormatting(color));
        tooltip.add(component);
    }

    public void color(String[] texts, TextFormatting[] formattings)
    {
        if (texts.length != formattings.length)
        {
            // Text need to have as many items as formatting.
            Alchemist.LOGGER.error("colorTooltip mismatch! Texts: '" + texts.length + "', Formattings: '" + formattings.length + "'. Returning without formatting.");
            return;
        }

        StringTextComponent output = new StringTextComponent("");

        for (int i = 0; i < texts.length; i++)
        {
            StringTextComponent c = new StringTextComponent(texts[i]);
            c.setStyle(Style.EMPTY.setFormatting(formattings[i]));
            output.append(c);
        }

        tooltip.add(output);
    }

    public void space()
    {
        tooltip.add(new StringTextComponent(""));
    }

    public void clear()
    {
        tooltip.clear();
    }

    private String effect(EffectInstance effect)
    {
        int seconds, t1, t2, t3;
        String text;

        // Ticks to Seconds with parenthesis example: 300 = (00:15)

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

        return text;
    }

    public void effect(EffectInstance effect, TextFormatting color)
    {
       StringTextComponent component = new StringTextComponent(" " + effect(effect));
       component.setStyle(Style.EMPTY.setFormatting(color));
       tooltip.add(component);
    }
}
