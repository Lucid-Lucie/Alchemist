package lucie.alchemist.utility;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilityCompound
{
    public static class Tool
    {
        String effect;
        private int duration, amplifier, uses;
        private boolean exist;

        public Tool(String effect, int duration, int amplifier, int uses, boolean exist)
        {
            this.effect = effect;
            this.duration = duration;
            this.amplifier = amplifier;
            this.uses = uses;
            this.exist = exist;
        }

        public static Tool convert(ItemStack stack, boolean primary)
        {
            if (stack.getTag() == null || !stack.getTag().contains("potion") || (!stack.getTag().getCompound("potion").contains("primary") && primary) || (!stack.getTag().getCompound("potion").contains("secondary") && !primary)) return new Tool("none", 0, 0, 0, false);

            String type = primary ? "primary" : "secondary";
            CompoundNBT nbt = stack.getTag().getCompound("potion").getCompound(type);

            return new Tool(nbt.getString("effect"), nbt.getInt("duration"), nbt.getInt("amplifier"), nbt.getInt("uses"), true);
        }

        public Effect getEffect()
        {
            return ForgeRegistries.POTIONS.getValue(new ResourceLocation(effect));
        }

        public String getEffectName()
        {
            return effect;
        }

        public int getDuration()
        {
            return duration;
        }

        public int getAmplifier()
        {
            return amplifier;
        }

        public int getUses()
        {
            return uses;
        }

        public boolean doesExist()
        {
            return exist;
        }
    }
}
