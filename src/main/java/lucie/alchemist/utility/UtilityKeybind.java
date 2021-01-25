package lucie.alchemist.utility;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UtilityKeybind
{
    public static KeyBinding INFO = new KeyBinding("keybind.alchemist.info", 68, "keybind.alchemist.category");
}
