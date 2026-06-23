package org.evenmorefish.fishstew.item;

import com.oheers.fish.EvenMoreFish;
import com.oheers.fish.FishUtils;
import com.oheers.fish.api.config.ConfigBase;
import com.oheers.fish.api.registry.RegistryItem;
import com.oheers.fish.competition.CompetitionQueue;
import com.oheers.fish.competition.configs.CompetitionFile;
import com.oheers.fish.items.ItemFactory;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.evenmorefish.fishstew.FishStewPlugin;
import org.evenmorefish.fishstew.utils.Keys;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class FishStewItem extends ConfigBase implements RegistryItem {

    private static final Random RANDOM = new Random();

    private final @NotNull String id;
    private final @NotNull ItemFactory factory;
    private final @NotNull List<CompetitionFile> compFiles;

    public FishStewItem(@NotNull File file) throws InvalidConfigurationException {
        super(file, FishStewPlugin.getInstance(), false);
        String key = getConfig().getString("id");
        if (key == null) {
            throw new InvalidConfigurationException("ID does not exist for " + file.getName());
        }
        this.id = key;
        this.factory = ItemFactory.itemFactory(getConfig());

        List<CompetitionFile> compFiles = getCompetitionIds().stream()
            .map(EvenMoreFish.getInstance().getCompetitionQueue()::getItem)
            .filter(Objects::nonNull)
            .toList();
        if (compFiles.isEmpty()) {
            throw new InvalidConfigurationException("Competition ID not configured properly for " + file.getName());
        }
        this.compFiles = compFiles;
    }

    @Override
    public @NotNull String getKey() {
        return this.id;
    }

    private @NotNull ItemStack getItem() {
        return getItem(null);
    }

    public boolean isDisabled() {
        return getConfig().getBoolean("disabled", false);
    }

    public boolean shouldRespectMinimumPlayers() {
        return getConfig().getBoolean("respect-minimum-players", false);
    }

    public @NotNull ItemStack getItem(@Nullable UUID uuid) {
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

    public void give(@NotNull Player player) {
        FishUtils.giveItem(getItem(player.getUniqueId()), player);
    }

    public @NotNull CompetitionFile getRandomCompFile() {
        int index = RANDOM.nextInt(compFiles.size());
        return compFiles.get(index);
    }

    private List<String> getCompetitionIds() {
        String path = "competition-id";
        if (getConfig().isList(path)) {
            return getConfig().getStringList(path);
        }
        return List.of(getConfig().getString(path));
    }

}
