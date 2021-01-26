package lucie.alchemist.particle;

import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = "alchemist", bus = Mod.EventBusSubscriber.Bus.MOD)
public class AlchemistParticles
{
    @ObjectHolder("alchemist:campfire")
    public static BasicParticleType CAMPFIRE;

    @ObjectHolder("alchemist:soul_campfire")
    public static BasicParticleType SOUL_CAMPFIRE;
}