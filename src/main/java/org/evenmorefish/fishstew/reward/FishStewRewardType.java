package org.evenmorefish.fishstew.reward;

import com.oheers.fish.api.reward.RewardType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.evenmorefish.fishstew.FishStewPlugin;
import org.evenmorefish.fishstew.item.FishStewItem;
import org.evenmorefish.fishstew.item.FishStewRegistry;
import org.jspecify.annotations.NonNull;

public class FishStewRewardType extends RewardType {

    @Override
    public void doReward(@NonNull Player player, @NonNull String key, @NonNull String value, Location hookLocation) {
        FishStewItem item = FishStewRegistry.getInstance().get(value);
        if (item == null) {
            FishStewPlugin.getInstance().getLogging().warn(value + " is not a valid FishStewItem.");
            return;
        }
        item.give(player);
    }

    @Override
    public @NonNull String getIdentifier() {
        return "fishstew";
    }

    @Override
    public @NonNull String getAuthor() {
        return "FireML";
    }

    @Override
    public @NonNull Plugin getPlugin() {
        return FishStewPlugin.getInstance();
    }

}
