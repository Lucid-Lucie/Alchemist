package lucie.alchemist.item;

import lucie.alchemist.Alchemist;
import net.minecraft.item.Item;

public class ItemPestle extends Item
{
    public ItemPestle()
    {
        super(new Item.Properties().maxStackSize(1).rarity(Alchemist.RARITY).group(Alchemist.GROUP));
        this.setRegistryName("pestle");
    }
}
