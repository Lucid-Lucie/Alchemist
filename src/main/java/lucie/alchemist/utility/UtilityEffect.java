package lucie.alchemist.utility;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class UtilityEffect
{
    // Checks if item has mixture effects.
    public static boolean hasEffect(ItemStack stack)
    {
        return stack.getTag() != null && stack.getTag().contains("mixture") && !getEffects(stack).isEmpty();
    }

    // Checks if item has specified mixture effect.
    public static boolean hasEffect(ItemStack stack, Effect effect)
    {
        // Effects with 0 uses doesn't count so remove them.
        cleanEffects(stack);

        // Get all effects and check if it is empty.
        HashMap<EffectInstance, Integer> map = getEffects(stack);
        if (map.isEmpty()) return false;

        // Loop through effect instances and look for effects.
        for (EffectInstance instance : map.keySet())
        {
            // Use effect instead of instance so duplication can't happen.
            if (instance.getPotion().equals(effect)) return true;
        }

        return false;
    }

    // Get effects from compound.
    public static HashMap<EffectInstance, Integer> getEffects(ItemStack stack)
    {
        ListNBT list = (ListNBT) stack.getOrCreateTag().get("mixture");
        HashMap<EffectInstance, Integer> map = new HashMap<>();
        CompoundNBT nbt;
        Effect effect;

        if (list == null)
        {
            return map;
        }

        for (INBT value : list)
        {
            nbt = (CompoundNBT) value;
            effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(nbt.getString("effect")));

            if (effect == null)
            {
                continue;
            }

            map.put(new EffectInstance(effect, nbt.getInt("duration"), nbt.getInt("amplifier")), nbt.getInt("uses"));
        }

        return map;
    }

    // Set effects to compound.
    public static void setEffects(HashMap<EffectInstance, Integer> effects, ItemStack stack)
    {
        ListNBT list = new ListNBT();
        CompoundNBT nbt;
        cleanEffects(stack);

        // This should never happen.
        if (effects.isEmpty())
        {
            return;
        }

        // Loop through effect map and put it in nbt.
        for (Map.Entry<EffectInstance, Integer> map : effects.entrySet())
        {
            if (hasEffect(stack, map.getKey().getPotion()) || map.getKey().getPotion().getRegistryName() == null) continue;

            nbt = new CompoundNBT();
            nbt.putString("effect", map.getKey().getPotion().getRegistryName().toString());
            nbt.putInt("uses", map.getValue());
            nbt.putInt("duration", map.getKey().getDuration());
            nbt.putInt("amplifier", map.getKey().getAmplifier());

            list.add(nbt);
        }

        // Combines the old list with the new.
        ListNBT old = (ListNBT) stack.getOrCreateTag().get("mixture");
        if (old != null)
        {
            list.addAll(old);
        }

        // Output new compound data.
        purgeEffects(stack);
        stack.getOrCreateTag().put("mixture", list);
    }

    // Remove effects with 0 uses.
    public static void cleanEffects(ItemStack stack)
    {
        ListNBT list = (ListNBT) stack.getOrCreateTag().get("mixture");

        if (list == null || stack.getTag() == null) return;

        // Remove effects that has 0 uses.
        list.removeIf(nbt -> ((CompoundNBT) nbt).getInt("uses") == 0);

        // Replace mixture with cleaned mixture.
        purgeEffects(stack);
        stack.getTag().put("mixture", list);

    }

    // Remove all effects.
    public static void purgeEffects(ItemStack stack)
    {
        // Stack need to contain mixture to remove mixture.
        if (stack.getTag() != null && stack.getTag().contains("mixture"))
        {
            stack.getTag().remove("mixture");
        }
    }
}
