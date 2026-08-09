package org.evenmorefish.fishstew.item;

import com.oheers.fish.api.FileUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.evenmorefish.fishstew.FishStewPlugin;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.recipe.AbstractConfigRecipe;
import uk.firedev.daisylib.recipe.RecipeUtil;

import java.io.File;
import java.util.List;

public class FishStewManager {

    private static final FishStewManager instance = new FishStewManager();

    private final File itemDirectory = new File(FishStewPlugin.getInstance().getDataFolder(), "items");

    private FishStewManager() {}

    public static @NonNull FishStewManager getInstance() {
        return instance;
    }

    public void load() {
        loadExampleFile();
        loadFiles();
        logLoadedItems();
    }

    private void loadExampleFile() {
        if (!itemDirectory.exists()) {
            itemDirectory.mkdirs();
        }
        FishStewPlugin.getInstance().saveResource("items/_example.yml", true);
    }

    private void loadFiles() {
        List<File> stewFiles = FileUtil.getFilesInDirectory(itemDirectory, true, true);
        stewFiles.forEach(this::loadFile);
    }

    private void loadFile(@NonNull File file) {
        try {
            FishStewItem item = new FishStewItem(file);
            if (item.isDisabled()) {
                return;
            }
            AbstractConfigRecipe<?> recipe = item.getRecipe();
            if (recipe != null) {
                item.getRecipe().register();
            }
            FishStewRegistry.getInstance().register(item);
        } catch (InvalidConfigurationException exception) {
            FishStewPlugin.getInstance().getLogging().warn(exception.getMessage());
        }
    }

    private void logLoadedItems() {
        FishStewPlugin.getInstance().getLogging().info(
            "Loaded FishStewManager with " + FishStewRegistry.getInstance().getSize() + " Item(s)."
        );
    }

    public void reload() {
        unload();
        load();
    }

    public void unload() {
        FishStewRegistry.getInstance().clear();
    }

}
