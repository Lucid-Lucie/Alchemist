package lucie.alchemist.function;

import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static lucie.alchemist.utility.UtilityCompound.Tool;

@Mod.EventBusSubscriber(modid = "alchemist")
public class FunctionEffect
{
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event)
    {
        // Check for entity capable of holding tool.
        if (!(event.getSource().getImmediateSource() instanceof LivingEntity)) return;

        // Get entity, currently held item, and list of tools.
        LivingEntity entity = event.getEntityLiving();
        ItemStack stack = ((LivingEntity) event.getSource().getImmediateSource()).getHeldItem(((LivingEntity) event.getSource().getImmediateSource()).getActiveHand());
        ITag<Item> tools = ItemTags.getCollection().get(new ResourceLocation("alchemist", "tools"));

        // Check of tool.
        if (tools == null || !tools.contains(stack.getItem())) return;

        // Check for both primary and secondary slots.
        Tool primary = Tool.convert(stack, true);
        Tool secondary = Tool.convert(stack, false);
        EffectInstance instance;
        List<Effect> effects = new ArrayList<>();

        // Get list of all effects currently applied on entity.
        for (EffectInstance e : entity.getActivePotionEffects())
        {
            effects.add(e.getPotion());
        }

        // Stack needs nbt and primary slot used by mixture.
        if (stack.getTag() == null || !primary.doesExist()) return;

        // Apply primary mixture.
        if (primary.getUses() > 0)
        {
            instance = UtilityGetter.getEffectInstance(new ResourceLocation(primary.getEffect()), primary.getDuration(), primary.getAmplifier());

            if (!effects.contains(instance.getPotion()))
            {
                entity.addPotionEffect(instance);
                stack.getTag().getCompound("mixture").getCompound("primary").putInt("uses", primary.getUses() - 1);
            }
        }

        // Apply secondary mixture.
        if (secondary.getUses() > 0)
        {
            instance = UtilityGetter.getEffectInstance(new ResourceLocation(secondary.getEffect()), secondary.getDuration(), secondary.getAmplifier());

            if (!effects.contains(instance.getPotion()))
            {
                entity.addPotionEffect(instance);
                stack.getTag().getCompound("mixture").getCompound("secondary").putInt("uses", secondary.getUses() - 1);
            }
        }
    }
}
