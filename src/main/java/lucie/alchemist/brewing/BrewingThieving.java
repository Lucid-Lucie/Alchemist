package lucie.alchemist.brewing;

import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import javax.annotation.Nonnull;

public class BrewingThieving implements IBrewingRecipe
{
    @Override
    public boolean isInput(ItemStack input)
    {
        return input.getItem() == Items.POTION && (PotionUtils.getPotionFromItem(input) == Potions.AWKWARD || PotionUtils.getPotionFromItem(input) == UtilityGetter.Potions.THIEVING);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient)
    {
        return ingredient.getItem().equals(Items.REDSTONE) || ingredient.getItem().equals(Items.GLOWSTONE_DUST) || ingredient.getItem().equals(Items.GOLDEN_APPLE);
    }

    @Nonnull
    @Override
    public ItemStack getOutput(ItemStack input, @Nonnull ItemStack ingredient)
    {
        // Awkward + Main ingredient = potion basic.
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(Potions.AWKWARD) && ingredient.getItem().equals(Items.GOLDEN_APPLE))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.THIEVING);
        }

        // Potion + Redstone = long potion.
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(UtilityGetter.Potions.THIEVING) && ingredient.getItem().equals(Items.REDSTONE))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.THIEVING_LONG);
        }

        // Potion + Glowstone dust = strong potion.
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(UtilityGetter.Potions.THIEVING) && ingredient.getItem().equals(Items.GLOWSTONE_DUST))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.THIEVING_STRONG);
        }

        return new ItemStack(Items.AIR);
    }
}
