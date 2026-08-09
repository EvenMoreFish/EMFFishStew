package org.evenmorefish.fishstew;

import com.oheers.fish.api.registry.EMFRegistry;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.evenmorefish.fishstew.command.MainCommand;
import org.evenmorefish.fishstew.config.MessageConfig;
import org.evenmorefish.fishstew.item.FishStewManager;
import org.evenmorefish.fishstew.reward.FishStewRewardType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.DaisyLib;
import uk.firedev.daisylib.logging.ComponentLogging;
import uk.firedev.daisylib.logging.Logging;

public final class FishStewPlugin extends JavaPlugin {

    private static FishStewPlugin INSTANCE;

    private final ComponentLogging logging = Logging.logging(this);

    private Metrics metrics;

    public FishStewPlugin() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " has already been assigned!");
        }
        INSTANCE = this;
    }

    public static @NotNull FishStewPlugin getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException(FishStewPlugin.class.getSimpleName() + " has not been assigned!");
        }
        return INSTANCE;
    }

    @Override
    public void onLoad() {
        new MessageConfig().init();
        registerCommands();
    }

    @Override
    public void onEnable() {
        DaisyLib.get().init(this);
        this.metrics = new Metrics(this, 28266);

        getServer().getPluginManager().registerEvents(new FishStewListener(), this);
        FishStewManager.getInstance().load();

        EMFRegistry.REWARD_TYPE.register(new FishStewRewardType());
    }

    @Override
    public void onDisable() {
        FishStewManager.getInstance().unload();
    }

    @SuppressWarnings("UnstableApiUsage")
    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(MainCommand.get());
        });
    }

    public void reload() {
        FishStewManager.getInstance().reload();
    }

    public @NonNull ComponentLogging getLogging() {
        return this.logging;
    }

}
