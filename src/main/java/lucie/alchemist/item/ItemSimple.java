package lucie.alchemist.item;

import lucie.alchemist.Alchemist;
import lucie.alchemist.block.AlchemicalBlocks;
import lucie.alchemist.block.BlockCampfire;
import lucie.alchemist.item.AlchemicalItems.ItemType;
import lucie.alchemist.utility.UtilityTooltip;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemSimple extends Item
{
    private String name;

    private ItemType type;

    public ItemSimple(String name, ItemType type)
    {
        super(new Item.Properties().rarity(type.getRarity()).group(Alchemist.GROUP));
        setRegistryName(name);
        this.name = name;
        this.type = type;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        new UtilityTooltip(tooltip).journal(type, name, I18n.format("journal.alchemist." + name).length());
    }

    @Nonnull
    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        BlockState state = context.getWorld().getBlockState(context.getPos());

        // Essences usability
        if (equals(AlchemicalItems.ESSENCE) && !context.getWorld().isRemote)
        {
            return useEssence(context, state);
        }

        // Seeds usability.
        if (equals(AlchemicalItems.SEEDS) && !context.getWorld().isRemote)
        {
            return useSeeds(context, state);
        }

        return super.onItemUse(context);
    }

    private static ActionResultType useEssence(ItemUseContext context, BlockState state)
    {
        // Block need to be campfire and lit.
        if (!state.getBlock().equals(Blocks.CAMPFIRE) || !state.get(CampfireBlock.LIT)) return ActionResultType.FAIL;

        // Replace campfire.
        context.getWorld().setBlockState(context.getPos(), AlchemicalBlocks.CAMPFIRE.getDefaultState().with(BlockCampfire.AMOUNT, 12).with(BlockCampfire.FACING, state.get(CampfireBlock.FACING)));
        context.getWorld().playSound(null, context.getPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        context.getItem().shrink(1);

        return ActionResultType.SUCCESS;
    }

    private static ActionResultType useSeeds(ItemUseContext context, BlockState state)
    {
        // Block needs to be either grass, crimson, or warped and have a air block above.
        if ((!state.getBlock().equals(Blocks.DIRT) && !state.getBlock().equals(Blocks.NETHERRACK)) || !context.getWorld().getBlockState(context.getPos().up()).getBlock().equals(Blocks.AIR)) return ActionResultType.FAIL;

        // Get block replacement and assume type is grass.
        Block output = state.getBlock().equals(Blocks.DIRT) ? Blocks.GRASS : random.nextBoolean() ? Blocks.CRIMSON_NYLIUM : Blocks.WARPED_NYLIUM;
        String type = "grass";

        // Change type if it isn't grass.
        if (!output.getBlock().equals(Blocks.GRASS)) type = output.equals(Blocks.CRIMSON_NYLIUM) ? "crimson" : "warped";

        // Nether flora has lower chance of spawning.
        int value = output.equals(Blocks.GRASS) ? 4 : 8;

        // Chance to spawn with flora.
        if (random.nextInt(value) == 0)
        {
            // Floral list stored as tags.
            ITag<Block> flora = BlockTags.getCollection().get(new ResourceLocation("alchemist", "seeds_" + type));
            if (flora == null) return ActionResultType.FAIL;

            // Set flora.
            context.getWorld().setBlockState(context.getPos().up(), flora.getRandomElement(random).getDefaultState());
        }

        // Set replacement.
        context.getItem().shrink(1);
        context.getWorld().setBlockState(context.getPos(), output.getDefaultState());
        context.getWorld().playSound(null, context.getPos(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);

        return ActionResultType.SUCCESS;
    }
}
