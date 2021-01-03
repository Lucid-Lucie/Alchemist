package lucie.alchemist.item;

import lucie.alchemist.Alchemist;
import lucie.alchemist.utility.UtilityTooltip;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPouch extends Item
{
    public ItemPouch()
    {
        super(new Item.Properties().rarity(Alchemist.RARITY).group(Alchemist.GROUP));
        setRegistryName("pouch");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (Screen.hasShiftDown())
        {
            int number = I18n.format("journal.alchemist.pouch").length();

            UtilityTooltip utility = new UtilityTooltip(tooltip);
            utility.clear();
            utility.color(new String[]{I18n.format("journal.alchemist") + " ", I18n.format("journal.alchemist.page", number)}, new TextFormatting[]{TextFormatting.YELLOW, TextFormatting.WHITE});
            utility.trim("\"" + I18n.format("journal.alchemist.pouch") + "\"", 3);
        }
    }
}
