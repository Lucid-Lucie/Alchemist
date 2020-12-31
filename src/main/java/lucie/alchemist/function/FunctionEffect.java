package lucie.alchemist.function;

import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
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
    public static void tooltip(LivingHurtEvent event)
    {
        if (!(event.getSource().getImmediateSource() instanceof LivingEntity)) return;

        LivingEntity entity = event.getEntityLiving();
        ItemStack stack = ((LivingEntity) event.getSource().getImmediateSource()).getHeldItem(((LivingEntity) event.getSource().getImmediateSource()).getActiveHand());

        if (!FunctionTools.getItems().contains(stack.getItem())) return;

        Tool primary = Tool.convert(stack, true);
        Tool secondary = Tool.convert(stack, false);
        EffectInstance instance;
        List<Effect> effects = new ArrayList<>();

        for (EffectInstance e : entity.getActivePotionEffects())
        {
            effects.add(e.getPotion());
        }

        if (stack.getTag() == null || !primary.doesExist()) return;

        if (primary.getUses() > 0)
        {
            instance = UtilityGetter.getEffectInstance(new ResourceLocation(primary.getEffect()), primary.getDuration(), primary.getAmplifier());

            if (!effects.contains(instance.getPotion()))
            {
                entity.addPotionEffect(instance);
                stack.getTag().getCompound("mixture").getCompound("primary").putInt("uses", primary.getUses() - 1);
            }
        }

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
