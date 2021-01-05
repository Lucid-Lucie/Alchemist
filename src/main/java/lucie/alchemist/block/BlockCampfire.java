package lucie.alchemist.block;

import lucie.alchemist.init.InitializeItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockCampfire extends Block
{
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);

    public static final IntegerProperty AMOUNT =  IntegerProperty.create("amount", 1, 12);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockCampfire()
    {
        super(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD).notSolid());
        setRegistryName("campfire");
        setDefaultState(stateContainer.getBaseState().with(AMOUNT, 6).with(FACING, Direction.NORTH));
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        // Tags
        ITag<Item> fuels = ItemTags.getCollection().get(new ResourceLocation("alchemist", "fuels"));
        ITag<Item> ashes = ItemTags.getCollection().get(new ResourceLocation("alchemist", "ashes"));
        ItemStack stack = player.getHeldItem(handIn);

        // Ashes interaction.
        if (ashes != null && ashes.contains(stack.getItem()) && !worldIn.isRemote)
        {
            stack.shrink(1);

            ItemEntity item = new ItemEntity(worldIn, pos.getX() + 0.5F, pos.getY() + 0.5, pos.getZ() + 0.5F, new ItemStack(InitializeItems.ASH));

            double angle = Math.random()*Math.PI*2;

            item.setVelocity(Math.cos(angle)*0.15, 0.3, Math.sin(angle)*0.15);

            worldIn.addEntity(item);

            if (state.get(AMOUNT) == 1)
            {
                // When amount reaches zero it destroys the block.
                worldIn.destroyBlock(pos, true);
            }
            else
            {
                worldIn.setBlockState(pos, state.with(AMOUNT, state.get(AMOUNT) - 1));
            }

            worldIn.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 1.0F);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F);

            return ActionResultType.SUCCESS;
        }

        // Fuel interaction.
        if (fuels != null && fuels.contains(stack.getItem()) && state.get(AMOUNT) < 12 && !worldIn.isRemote)
        {
            int amount = state.get(AMOUNT) + new Random().nextInt(3) + 2;
            if (amount > 12) amount = 12;

            worldIn.setBlockState(pos, state.with(AMOUNT, amount));
            stack.shrink(1);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F);

            return ActionResultType.SUCCESS;
        }

        // Sand or Ash interaction.
        if (stack.getItem().equals(Items.SAND) || stack.getItem().equals(InitializeItems.ASH))
        {
            if (state.get(AMOUNT) == 1)
            {
                // When amount reaches zero it destroys the block.
                worldIn.destroyBlock(pos, true);
            }
            else
            {
                worldIn.setBlockState(pos, state.with(AMOUNT, state.get(AMOUNT) - 1));
            }

            stack.shrink(1);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            worldIn.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 0.25F, 1.0F);

            return ActionResultType.SUCCESS;
        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        if (!entityIn.isImmuneToFire() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn))
        {
            entityIn.attackEntityFrom(DamageSource.IN_FIRE, 1.0F);
            entityIn.setFire(5);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(AMOUNT, 8).with(FACING, context.getPlacementHorizontalFacing());
    }

    @Nonnull
    @Override
    public BlockState rotate(BlockState state, Rotation rot)
    {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Nonnull
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(AMOUNT, FACING);
    }

    @Override
    public boolean allowsMovement(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, PathType type)
    {
        return false;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return 3 + state.get(AMOUNT);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }
}
