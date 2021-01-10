package lucie.alchemist.brewing;

import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import javax.annotation.Nonnull;

public class BrewingLevitation implements IBrewingRecipe
{
    @Override
    public boolean isInput(ItemStack input)
    {
        return input.getItem() == Items.POTION && (PotionUtils.getPotionFromItem(input) == Potions.SLOW_FALLING || PotionUtils.getPotionFromItem(input) == Potions.LONG_SLOW_FALLING || PotionUtils.getPotionFromItem(input) == UtilityGetter.Potions.LEVITATION);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient)
    {
        return ingredient.getItem().equals(Items.REDSTONE) || ingredient.getItem().equals(Items.FERMENTED_SPIDER_EYE);
    }

    @Nonnull
    @Override
    public ItemStack getOutput(ItemStack input, @Nonnull ItemStack ingredient)
    {
        // Slow falling + Fermented spider eye = Levitation
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(Potions.SLOW_FALLING) && ingredient.getItem().equals(Items.FERMENTED_SPIDER_EYE))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.LEVITATION);
        }

        // Levitation + Redstone = long potion.
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(UtilityGetter.Potions.LEVITATION) && ingredient.getItem().equals(Items.REDSTONE))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.LEVITATION_LONG);
        }

        // Long slow fall + Fermented spider eye = long potion..
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(Potions.LONG_SLOW_FALLING) && ingredient.getItem().equals(Items.FERMENTED_SPIDER_EYE))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.LEVITATION_LONG);
        }

        return new ItemStack(Items.AIR);
    }
}
