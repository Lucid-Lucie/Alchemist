package lucie.alchemist.item;

import lucie.alchemist.Alchemist;
import net.minecraft.item.Item;

public class ItemPouch extends Item
{
    public ItemPouch()
    {
        super(new Item.Properties().maxStackSize(1).rarity(Alchemist.RARITY).group(Alchemist.GROUP));
        this.setRegistryName("pouch");
    }
}
