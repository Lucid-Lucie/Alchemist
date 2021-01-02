package lucie.alchemist.utility;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

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
            if (stack.getTag() == null || !stack.getTag().contains("mixture") || (!stack.getTag().getCompound("mixture").contains("primary") && primary) || (!stack.getTag().getCompound("mixture").contains("secondary") && !primary)) return new Tool("none", 0, 0, 0, false);

            String type = primary ? "primary" : "secondary";
            CompoundNBT nbt = stack.getTag().getCompound("mixture").getCompound(type);

            return new Tool(nbt.getString("effect"), nbt.getInt("duration"), nbt.getInt("amplifier"), nbt.getInt("uses"), true);
        }

        public String getEffect()
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

    public static class Pouch
    {
        String effect;
        private int duration, amplifier, uses;
        private boolean exist;

        public Pouch(String effect, int duration, int amplifier, int uses, boolean exist)
        {
            this.effect = effect;
            this.duration = duration;
            this.amplifier = amplifier;
            this.uses = uses;
            this.exist = exist;
        }

        public static Pouch convert(ItemStack stack)
        {
            if (stack.getTag() == null || !stack.getTag().contains("mixture")) return new Pouch("none", 0, 0, 0, false);

            CompoundNBT nbt = stack.getTag().getCompound("mixture");

            return new Pouch(nbt.getString("effect"), nbt.getInt("duration"), nbt.getInt("amplifier"), nbt.getInt("uses"), true);
        }

        public String getEffect()
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
