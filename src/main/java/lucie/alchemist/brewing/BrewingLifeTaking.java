package lucie.alchemist.brewing;

import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import javax.annotation.Nonnull;

public class BrewingLifeTaking implements IBrewingRecipe
{
    @Override
    public boolean isInput(ItemStack input)
    {
        return input.getItem() == Items.POTION && (PotionUtils.getPotionFromItem(input) == Potions.AWKWARD || PotionUtils.getPotionFromItem(input) == UtilityGetter.Potions.LIFE_TAKING);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient)
    {
        return ingredient.getItem().equals(Items.GLOWSTONE_DUST) || ingredient.getItem().equals(Items.POPPY);
    }

    @Nonnull
    @Override
    public ItemStack getOutput(ItemStack input, @Nonnull ItemStack ingredient)
    {
        // Awkward + Main ingredient = potion basic.
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(Potions.AWKWARD) && ingredient.getItem().equals(Items.POPPY))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.LIFE_TAKING);
        }

        // Potion + Glowstone dust = strong potion.
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(UtilityGetter.Potions.LIFE_TAKING) && ingredient.getItem().equals(Items.GLOWSTONE_DUST))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.LIFE_TAKING_STRONG);
        }

        return new ItemStack(Items.AIR);
    }
}
