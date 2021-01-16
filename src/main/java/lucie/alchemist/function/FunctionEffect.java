package lucie.alchemist.function;

import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
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
    public static void onHurtEvent(LivingHurtEvent event)
    {
        if (!(event.getSource().getImmediateSource() instanceof PlayerEntity)) return;

        // Get player as attacker and mob as target.
        PlayerEntity attacker = (PlayerEntity) event.getSource().getImmediateSource();
        LivingEntity target = event.getEntityLiving();
        ItemStack weapon = attacker.getHeldItem(Hand.MAIN_HAND);
        boolean hasEnchantment = EnchantmentHelper.getEnchantmentLevel(UtilityGetter.Enchantments.BREWING, weapon) > 0;

        boolean debug = attacker.inventory.hasItemStack(new ItemStack(Items.DEBUG_STICK));

        if (debug)
        {
            System.out.println("---- Start of Checking ---- ");
            System.out.println("Target: " + target);
            System.out.println("Attacker: " + attacker);
            System.out.println("Weapon: " + weapon);
            System.out.println("Source: " + event.getSource());
            System.out.println("Hand: " + attacker.getActiveHand());
        }

        // Needs to be main hand and needs to be weapon.
        if (!hasEnchantment || (!(weapon.getItem() instanceof SwordItem) && !(weapon.getItem() instanceof AxeItem)))
        {
            if (debug)
            {
                System.out.println("Has Enchantment: " + hasEnchantment);
                System.out.println("Is Sword or Axe: " + (weapon.getItem() instanceof SwordItem || weapon.getItem() instanceof AxeItem));
                System.out.println("Event Status: Failed");
                System.out.println("---- End of Checking ---- ");
            }

            return;
        }

        // Check for both primary and secondary slots.
        Tool primary = Tool.convert(weapon, true);
        Tool secondary = Tool.convert(weapon, false);
        EffectInstance instance;
        List<Effect> effects = new ArrayList<>();
        target.getActivePotionEffects().forEach(effectInstance -> effects.add(effectInstance.getPotion()));

        if (debug)
        {
            System.out.println("Effects: " + effects);
            System.out.println("Primary: " + "Exist: " + primary.doesExist() + ", Duration: " + primary.getDuration() + ", Amplifier: " + primary.getAmplifier() + ", Effect: " + primary.getEffect());
            System.out.println("Secondary: " + "Exist: " + secondary.doesExist() + ", Duration: " + secondary.getDuration() + ", Amplifier: " + secondary.getAmplifier() + ", Effect: " + secondary.getEffect());
        }

        // Stack needs nbt and primary slot used by potion.
        if (weapon.getTag() == null || !primary.doesExist())
        {
            if (debug)
            {
                System.out.println("Compound:" + (weapon.getTag() == null));
                System.out.println("Does Primary Exist: " + primary.doesExist());
                System.out.println("Event Status: Failed");
                System.out.println("---- End of Checking ---- ");
            }

            return;
        }

        // Apply primary potion.
        if (primary.getUses() > 0)
        {
            instance = new EffectInstance(primary.getEffect(), primary.getDuration(), primary.getAmplifier());

            if (!effects.contains(instance.getPotion()))
            {
                if (debug) System.out.println("Primary applied: " + instance);

                target.addPotionEffect(instance);
                weapon.getTag().getCompound("potion").getCompound("primary").putInt("uses", primary.getUses() - 1);
            }
        }

        // Apply secondary potion.
        if (secondary.getUses() > 0)
        {
            instance = new EffectInstance(secondary.getEffect(), secondary.getDuration(), secondary.getAmplifier());

            if (!effects.contains(instance.getPotion()))
            {
                if (debug) System.out.println("Secondary applied: " + instance);

                target.addPotionEffect(instance);
                weapon.getTag().getCompound("potion").getCompound("secondary").putInt("uses", secondary.getUses() - 1);
            }
        }

        if (debug)
        {
            System.out.println("Event Status: Succeeded");
            System.out.println("---- End of Checking ---- ");
        }
    }
}
