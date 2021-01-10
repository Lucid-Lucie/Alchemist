package lucie.alchemist.brewing;

import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import javax.annotation.Nonnull;

public class BrewingSoulDraining implements IBrewingRecipe
{
    @Override
    public boolean isInput(ItemStack input)
    {
        return input.getItem() == Items.POTION && (PotionUtils.getPotionFromItem(input) == Potions.AWKWARD || PotionUtils.getPotionFromItem(input) == UtilityGetter.Potions.SOUL_DRAINING);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient)
    {
        return ingredient.getItem().equals(Items.REDSTONE) || ingredient.getItem().equals(Items.GLOWSTONE_DUST) || ingredient.getItem().equals(Items.SOUL_SAND);
    }

    @Nonnull
    @Override
    public ItemStack getOutput(ItemStack input, @Nonnull ItemStack ingredient)
    {
        // Awkward + Main ingredient = potion basic.
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(Potions.AWKWARD) && ingredient.getItem().equals(Items.SOUL_SAND))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.SOUL_DRAINING);
        }

        // Potion + Redstone = long potion.
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(UtilityGetter.Potions.SOUL_DRAINING) && ingredient.getItem().equals(Items.REDSTONE))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.SOUL_DRAINING_LONG);
        }

        // Potion + Glowstone dust = strong potion.
        if (input.getItem().equals(Items.POTION) && PotionUtils.getPotionFromItem(input).equals(UtilityGetter.Potions.SOUL_DRAINING) && ingredient.getItem().equals(Items.GLOWSTONE_DUST))
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), UtilityGetter.Potions.SOUL_DRAINING_STRONG);
        }

        return new ItemStack(Items.AIR);
    }
}
