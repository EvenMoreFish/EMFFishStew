package org.evenmorefish.fishstew.item;

import com.oheers.fish.EvenMoreFish;
import com.oheers.fish.FishUtils;
import com.oheers.fish.api.registry.RegistryItem;
import com.oheers.fish.competition.configs.CompetitionFile;
import com.oheers.fish.items.ItemFactory;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.configuration.ConfigurationSection;
import uk.firedev.daisylib.config.BasicConfig;
import uk.firedev.daisylib.recipe.AbstractConfigRecipe;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.evenmorefish.fishstew.FishStewPlugin;
import org.evenmorefish.fishstew.utils.Keys;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import uk.firedev.daisylib.recipe.RecipeUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class FishStewItem extends BasicConfig implements RegistryItem {

    private static final Random RANDOM = new Random();

    private final @NonNull String id;
    private final @NonNull ItemFactory factory;
    private final @NonNull List<CompetitionFile> compFiles;
    private final @Nullable AbstractConfigRecipe<?> recipe;

    public FishStewItem(@NonNull File file) throws InvalidConfigurationException {
        super(file, FishStewPlugin.getInstance());
        String key = getConfig().getString("id");
        if (key == null) {
            throw new InvalidConfigurationException("ID does not exist for " + file.getName());
        }
        this.id = key;
        this.factory = ItemFactory.itemFactory(fetchBoostedCopy(file));

        List<CompetitionFile> compFiles = getCompetitionIds().stream()
            .map(EvenMoreFish.getInstance().getCompetitionQueue()::getItem)
            .filter(Objects::nonNull)
            .toList();
        if (compFiles.isEmpty()) {
            throw new InvalidConfigurationException("Competition ID not configured properly for " + file.getName());
        }
        this.compFiles = compFiles;
        this.recipe = loadRecipe();
    }

    // TODO can be removed once EMF is using ConfigurationSection.
    private YamlDocument fetchBoostedCopy(@NonNull File file) {
        try {
            return YamlDocument.create(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AbstractConfigRecipe<?> loadRecipe() {
        ConfigurationSection section = getConfig().getConfigurationSection("recipe");
        if (section == null) {
            return null;
        }
        return RecipeUtil.getRecipe(
            section,
            getRecipeKey(),
            getItem(null)
        );
    }

    private @NonNull NamespacedKey getRecipeKey() {
        return new NamespacedKey(FishStewPlugin.getInstance(), "fishstew-" + getKey());
    }

    @Override
    public @NonNull String getKey() {
        return this.id;
    }

    private @NonNull ItemStack getItem() {
        return getItem(null);
    }

    public boolean isDisabled() {
        return getConfig().getBoolean("disabled", false);
    }

    public boolean shouldRespectMinimumPlayers() {
        return getConfig().getBoolean("respect-minimum-players", false);
    }

    public @NonNull ItemStack getItem(@Nullable UUID uuid) {
        ItemStack item;
        if (uuid == null) {
            item = this.factory.createItem();
        } else {
            item = this.factory.createItem(uuid);
        }
        // Apply the custom data so we can correctly identify this item.
        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(Keys.STEW_ID, PersistentDataType.STRING, this.id);
        });
        return item;
    }

    public void give(@NonNull Player player) {
        FishUtils.giveItem(getItem(player.getUniqueId()), player);
    }

    public @NonNull CompetitionFile getRandomCompFile() {
        int index = RANDOM.nextInt(compFiles.size());
        return compFiles.get(index);
    }

    public @Nullable AbstractConfigRecipe<?> getRecipe() {
        return this.recipe;
    }

    private List<String> getCompetitionIds() {
        String path = "competition-id";
        if (getConfig().isList(path)) {
            return getConfig().getStringList(path);
        }
        String val = getConfig().getString(path);
        return val == null ? List.of() : List.of(val);
    }

}
