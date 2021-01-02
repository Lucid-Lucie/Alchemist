package lucie.alchemist.function;

import net.minecraft.util.ResourceLocation;

public class FunctionMixture
{
    public static class Mixture
    {
        String effect;
        boolean instant;
        int[] durations, amplifiers, uses;

        public Mixture(String effect, boolean instant, int[] durations, int[] amplifiers, int[] uses)
        {
            this.effect = effect;
            this.instant = instant;
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
