package lucie.alchemist.utility;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class UtilityParticle
{
    public static void spawnParticles(BasicParticleType type, World world, BlockPos worldIn)
    {
        Random random = world.rand;

        for(Direction direction : Direction.values())
        {
            BlockPos blockpos = worldIn.offset(direction);

            for (int i = 0; i < 3; i++)
            {
                if (!world.getBlockState(blockpos).isOpaqueCube(world, blockpos))
                {
                    Direction.Axis direction$axis = direction.getAxis();
                    double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double)direction.getXOffset() : (double)random.nextFloat();
                    double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double)direction.getYOffset() : (double)random.nextFloat();
                    double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double)direction.getZOffset() : (double)random.nextFloat();
                    world.addParticle(type, (double)worldIn.getX() + d1, (double)worldIn.getY() + d2, (double)worldIn.getZ() + d3, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}
