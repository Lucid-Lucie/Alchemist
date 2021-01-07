package lucie.alchemist.item;

import lucie.alchemist.Alchemist;
import lucie.alchemist.init.InitializeItems;
import lucie.alchemist.init.InitializeItems.ItemType;
import lucie.alchemist.utility.UtilityTooltip;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
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
        // Seeds usability.
        if (equals(InitializeItems.SEEDS) && !context.getWorld().isRemote)
        {
            return useSeeds(context, context.getWorld().getBlockState(context.getPos()));
        }

        return super.onItemUse(context);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn)
    {
        // Leather usability.
        if (equals(InitializeItems.LEATHER))
        {
            return useLeather(playerIn, handIn);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private static ActionResult<ItemStack> useLeather(PlayerEntity player, Hand hand)
    {
        ItemStack leather = player.getHeldItem(Hand.MAIN_HAND);
        ItemStack armor = player.getHeldItem(Hand.OFF_HAND);
        List<Item> list = new ArrayList<>(Arrays.asList(Items.LEATHER_BOOTS, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET));

        // Needs armor, leather, damage, and right hand to repair.
        if (!leather.getItem().equals(InitializeItems.LEATHER) || !list.contains(armor.getItem()) || armor.getDamage() == 0 || hand == Hand.OFF_HAND) return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));

        // Calculate repair.
        int repair = armor.getMaxDamage() / 3;
        int damage = repair > armor.getDamage() ? 0 : armor.getDamage() - repair;

        // Repair with material.
        armor.setDamage(damage);
        leather.shrink(1);
        player.setHeldItem(hand, armor);

        return new ActionResult<>(ActionResultType.SUCCESS, leather);
    }

    private static ActionResultType useSeeds(ItemUseContext context, BlockState state)
    {
        // Block needs to be either grass, crimson, or warped and have a air block above.
        if ((!state.getBlock().equals(Blocks.DIRT) && !state.getBlock().equals(Blocks.NETHERRACK)) || !context.getWorld().getBlockState(context.getPos().up()).getBlock().equals(Blocks.AIR)) return ActionResultType.PASS;

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
            if (flora == null) return ActionResultType.PASS;

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
