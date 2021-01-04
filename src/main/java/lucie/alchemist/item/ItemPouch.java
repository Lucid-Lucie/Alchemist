package lucie.alchemist.item;

import lucie.alchemist.Alchemist;
import lucie.alchemist.item.AlchemicalItems.ItemType;
import lucie.alchemist.utility.UtilityTooltip;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPouch extends Item
{
    public ItemPouch()
    {
        super(new Item.Properties().rarity(ItemType.TOOL.getRarity()).group(Alchemist.GROUP));
        setRegistryName("pouch");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        new UtilityTooltip(tooltip).journal(ItemType.TOOL, "pouch", I18n.format("journal.alchemist.pouch").length());
    }
}
