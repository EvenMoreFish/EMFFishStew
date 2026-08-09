package org.evenmorefish.fishstew.config;

import org.bukkit.entity.Player;
import org.evenmorefish.fishstew.FishStewPlugin;
import org.jspecify.annotations.NonNull;
import uk.firedev.daisylib.config.BasicConfig;
import uk.firedev.daisylib.messages.message.ComponentMessage;
import uk.firedev.daisylib.messages.message.ComponentSingleMessage;

@SuppressWarnings("UnstableApiUsage")
public class MessageConfig extends BasicConfig {

    private static MessageConfig INSTANCE = null;

    public MessageConfig() {
        super("messages.yml", "messages.yml", FishStewPlugin.getInstance());
    }

    public void init() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " has already been assigned!");
        }
        INSTANCE = this;
    }

    public static @NonNull MessageConfig getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException(MessageConfig.class.getSimpleName() + " has not been assigned!");
        }
        return INSTANCE;
    }

    public @NonNull ComponentSingleMessage getPrefix() {
        return super.getComponentMessage("prefix", "<gray>[FishStew]</gray> ").toSingleMessage();
    }

    public @NonNull ComponentMessage<?, ?> getReloaded() {
        return getComponentMessage("reloaded", "{prefix}<white>Successfully reloaded the plugin.");
    }

    public @NonNull ComponentMessage<?, ?> getStewReceived() {
        return getComponentMessage("stew-received", "{prefix}<white>You have been given a fish stew.");
    }

    public @NonNull ComponentMessage<?, ?> getStewGiven(@NonNull Player target) {
        return getComponentMessage("stew-given", "{prefix}<white>You have given {target} a fish stew.")
            .replace("{target}", target.name());
    }

    public @NonNull ComponentMessage<?, ?> getNotEnoughPlayers() {
        return getComponentMessage("not-enough-players", "{prefix}<white>There are not enough players online to start the competition.");
    }

    public @NonNull ComponentMessage<?, ?> getCompetitionActive() {
        return getComponentMessage("competition-active", "{prefix}<white>A competition is already active! Please wait until it is over.");
    }

    public @NonNull ComponentMessage<?, ?> getStewInvalid() {
        return getComponentMessage("stew-invalid", "{prefix}<white>This fish stew is no longer valid.");
    }

    @Override
    public @NonNull ComponentMessage<?, ?> getComponentMessage(@NonNull String path, @NonNull Object def) {
        return super.getComponentMessage(path, def).replace("{prefix}", getPrefix());
    }

}
