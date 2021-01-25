package lucie.alchemist.function;

import lucie.alchemist.utility.UtilityEffect;
import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "alchemist")
public class FunctionEffect
{
    @SubscribeEvent
    public static void onHurtEvent(LivingHurtEvent event)
    {
        if (!(event.getSource().getImmediateSource() instanceof LivingEntity) || event.getAmount() <= 1.0F) return;

        // Attacker need to attack target with brewing enchanted sword.
        LivingEntity attacker = (LivingEntity) event.getSource().getImmediateSource();
        LivingEntity target = event.getEntityLiving();
        ItemStack weapon = attacker.getHeldItem(Hand.MAIN_HAND);
        int uses;

        // Check for enchantment and effects.
        if (EnchantmentHelper.getEnchantmentLevel(UtilityGetter.Enchantments.BREWING, weapon) == 0 || !UtilityEffect.hasEffect(weapon)) return;

        // Used to check for duplicate effects.
        List<Effect> effects = new ArrayList<>();
        target.getActivePotionEffects().forEach(instance -> effects.add(instance.getPotion()));

        // Try to apply effects on mob.
        HashMap<EffectInstance, Integer> mixtures = UtilityEffect.getEffects(weapon);
        for (Map.Entry<EffectInstance, Integer> map : mixtures.entrySet())
        {
            // Effect can't already be applied to mob.
            if (effects.contains(map.getKey().getPotion())) continue;

            // Apply effect and decrease use.
            target.addPotionEffect(map.getKey());
            uses = map.getValue() - 1;
            mixtures.put(map.getKey(), uses);

            UtilityEffect.purgeEffects(weapon);
            UtilityEffect.setEffects(mixtures, weapon);

            if (uses < 1)
            {
                UtilityEffect.cleanEffects(weapon);
            }
        }
    }
}
