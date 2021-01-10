package lucie.alchemist.brewing;

import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import javax.annotation.Nonnull;

public class BrewingDisplacement implements IBrewingRecipe
{
    @Override
    public boolean isInput(ItemStack input)
    {
        return input.getItem() == Items.POTION && (PotionUtils.getPotionFromItem(input) == Potions.AWKWARD || PotionUtils.getPotionFromItem(input) == UtilityGetter.Potions.DISPLACEMENT);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient)
    {
        return ingredient.getItem().equals(Items.GLOWSTONE_DUST) || ingredient.getItem().equals(Items.ENDER_PEARL);
    }

    @Nonnull
    @Override
    public ItemStack getOutput(ItemStack input, @Nonnull ItemStack ingredient)
    {
        // Awkward + Main ingredient = potion basic.
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(Potions.AWKWARD) && ingredient.getItem().equals(Items.ENDER_PEARL))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.DISPLACEMENT);
        }

        // Potion + Redstone = long potion.
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(UtilityGetter.Potions.DISPLACEMENT) && ingredient.getItem().equals(Items.GLOWSTONE_DUST))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.DISPLACEMENT_STRONG);
        }

        return new ItemStack(Items.AIR);
    }
}
