package lucie.alchemist.function;

import net.minecraft.util.ResourceLocation;

public class FunctionMixture
{
    public static class Mixture
    {
        String effect;
        boolean instant, soulfire;
        int[] durations, amplifiers, uses;

        public Mixture(String effect, boolean instant, boolean soulfire, int[] durations, int[] amplifiers, int[] uses)
        {
            this.effect = effect;
            this.instant = instant;
            this.soulfire = soulfire;
            this.durations = durations;
            this.amplifiers = amplifiers;
            this.uses = uses;
        }

        public ResourceLocation getResourceLocation()
        {
            return new ResourceLocation(effect);
        }

        public String getEffect()
        {
            return effect;
        }

        public boolean isSoulfire()
        {
            return soulfire;
        }

        public int[] getDurations()
        {
            return durations;
        }

        public int[] getAmplifiers()
        {
            return amplifiers;
        }

        public int[] getUses()
        {
            return uses;
        }
    }
}
