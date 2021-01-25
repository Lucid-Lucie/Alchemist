package lucie.alchemist.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import lucie.alchemist.Alchemist;
import lucie.alchemist.effect.EffectThieving;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "alchemist", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AlchemistData extends JsonReloadListener
{
    public AlchemistData()
    {
        super((new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create(), "potions");
    }

    @SubscribeEvent
    public static void onDatapackRegister(AddReloadListenerEvent event)
    {
        event.addListener(new AlchemistData());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, @Nonnull IResourceManager resourceManagerIn, @Nonnull IProfiler profilerIn)
    {
        if (objectIn.isEmpty())
        {
            Alchemist.LOGGER.warn("Couldn't find data files!");
            return;
        }

        Alchemist.LOGGER.info("Loading Alchemist's Combat Data: Potions");

        DataThieving thieving;

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet())
        {
            thieving = new Gson().fromJson(entry.getValue(), DataThieving.class);

            if (thieving.getDisable())
            {
                Alchemist.LOGGER.info("Skipping: '" + entry.getKey() + "' as it has been disabled.");
                continue;
            }

            EffectThieving.THIEVING.put(new ResourceLocation(thieving.getTag()), new ResourceLocation(thieving.getTable()));
            Alchemist.LOGGER.info("Applied: " + entry.getKey());
        }
    }
}
