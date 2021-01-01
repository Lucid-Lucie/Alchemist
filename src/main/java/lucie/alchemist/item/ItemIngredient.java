package lucie.alchemist.item;

import lucie.alchemist.Alchemist;
import lucie.alchemist.utility.UtilityTooltip;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemIngredient extends Item
{
    String name;

    public ItemIngredient(String name)
    {
        super(new Item.Properties().rarity(Alchemist.RARITY).group(Alchemist.GROUP_INGREDIENTS));
        this.setRegistryName(name);
        this.name = name;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (Screen.hasShiftDown())
        {
            int number = I18n.format("journal.alchemist." + name).length();

            UtilityTooltip utility = new UtilityTooltip(tooltip);
            utility.clear();
            utility.color(new String[]{I18n.format("journal.alchemist") + " ", I18n.format("journal.alchemist.page", number)}, new TextFormatting[]{TextFormatting.YELLOW, TextFormatting.WHITE});
            utility.trim("\"" + I18n.format("journal.alchemist." + name) + "\"", 3);
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        if (this.equals(AlchemicalItems.SEEDS) && !context.getWorld().isRemote)
        {
            Block ground = context.getWorld().getBlockState(context.getPos()).getBlock();
            Block above = context.getWorld().getBlockState(context.getPos().up()).getBlock();

            // Turns dirt into grass.
            if (ground.equals(Blocks.DIRT) && above.equals(Blocks.AIR))
            {
                context.getWorld().setBlockState(context.getPos(), Blocks.GRASS_BLOCK.getDefaultState());

                // 1 in 4 chance to spawn with flora.
                if (random.nextInt(4) == 0)
                {
                    // All flowers, grass and fern.
                    List<Block> flora = new ArrayList<>(Arrays.asList(Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.GRASS, Blocks.FERN));
                    context.getWorld().setBlockState(context.getPos().up(), flora.get(random.nextInt(flora.size())).getDefaultState());
                }

                context.getItem().shrink(1);
                context.getWorld().playSound(null, context.getPos(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);

                return ActionResultType.SUCCESS;
            }

            // Turns netherrack into Crimson and Warped Nylium.
            if (ground.equals(Blocks.NETHERRACK) && above.equals(Blocks.AIR))
            {
                // Random decide if warped or crimson.
                Block replace = random.nextBoolean() ? Blocks.CRIMSON_NYLIUM : Blocks.WARPED_NYLIUM;
                context.getWorld().setBlockState(context.getPos(), replace.getDefaultState());

                if (random.nextInt(4) == 0)
                {
                    // Add flora to corresponding block.
                    List<Block> crimson = new ArrayList<>(Arrays.asList(Blocks.CRIMSON_FUNGUS, Blocks.CRIMSON_ROOTS));
                    List<Block> warped = new ArrayList<>(Arrays.asList(Blocks.WARPED_FUNGUS, Blocks.WARPED_ROOTS, Blocks.NETHER_SPROUTS));

                    Block flora = replace.equals(Blocks.CRIMSON_NYLIUM) ? crimson.get(random.nextInt(crimson.size())) : warped.get(random.nextInt(warped.size()));
                    context.getWorld().setBlockState(context.getPos().up(), flora.getDefaultState());
                }

                context.getItem().shrink(1);
                context.getWorld().playSound(null, context.getPos(), SoundEvents.BLOCK_NYLIUM_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);

                return ActionResultType.SUCCESS;
            }
        }

        return super.onItemUse(context);
    }
}
