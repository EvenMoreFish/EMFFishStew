package org.evenmorefish.fishstew.recipe;

import com.oheers.fish.api.config.serializer.ItemSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import uk.firedev.daisylib.recipe.choice.RecipeChoiceWrapper;

public class ItemAddonChoiceWrapper implements RecipeChoiceWrapper {

    @Override
    public @Nullable RecipeChoice parse(@NonNull String string) {
        ItemStack item = ItemSerializer.get().deserializeItemAddon(string);
        if (item == null || item.isEmpty()) {
            return null;
        }
        return new RecipeChoice.ExactChoice(item);
    }

}
