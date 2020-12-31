package lucie.alchemist.feature;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lucie.alchemist.utility.UtilityGetter;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeatureTools
{
    public static final Usable USABLE = new Gson().fromJson(new JsonParser().parse(new InputStreamReader((Objects.requireNonNull(Usable.class.getClassLoader().getResourceAsStream("data/alchemist/enchantments/knowledge.json"))), StandardCharsets.UTF_8)).toString(), Usable.class);

    public static List<Item> getItems()
    {
        return USABLE.getItems();
    }

    public static class Usable
    {
        private String[] enchantments;

        public Usable(String[] enchantments)
        {
            this.enchantments = enchantments;
        }

        public List<Item> getItems()
        {
            List<Item> list = new ArrayList<>();

            for (String s : enchantments)
            {
                list.add(UtilityGetter.getItem(new ResourceLocation(s)));
            }

            return list;
        }
    }
}
