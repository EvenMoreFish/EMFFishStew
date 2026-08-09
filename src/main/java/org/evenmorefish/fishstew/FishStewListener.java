package org.evenmorefish.fishstew;

import com.oheers.fish.api.events.EMFPluginReloadEvent;
import com.oheers.fish.competition.Competition;
import com.oheers.fish.competition.configs.CompetitionFile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.evenmorefish.fishstew.config.MessageConfig;
import org.evenmorefish.fishstew.item.FishStewItem;
import org.evenmorefish.fishstew.item.FishStewRegistry;
import org.evenmorefish.fishstew.utils.Keys;

public class FishStewListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }
        Player player = event.getPlayer();
        if (!EquipmentSlot.HAND.equals(event.getHand())) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        String id = item.getPersistentDataContainer().get(Keys.STEW_ID, PersistentDataType.STRING);
        if (id == null) {
            return;
        }
        FishStewItem fishStew = FishStewRegistry.getInstance().get(id);
        if (fishStew == null) {
            MessageConfig.getInstance().getStewInvalid().send(player);
            return;
        }
        if (Competition.isActive()) {
            MessageConfig.getInstance().getCompetitionActive().send(player);
            return;
        }
        CompetitionFile file = fishStew.getRandomCompFile();
        Competition competition = new Competition(file);

        // Optionally respect the competition player requirement
        if (fishStew.shouldRespectMinimumPlayers() && !competition.isPlayerRequirementMet()) {
            MessageConfig.getInstance().getNotEnoughPlayers().send(player);
            return;
        }

        competition.setAdminStarted(true);
        competition.begin();
        
        int newAmount = item.getAmount() - 1;
        if (newAmount > 0) {
            player.getInventory().setItemInMainHand(item.asQuantity(newAmount));
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }

    @EventHandler
    public void onEMFReload(EMFPluginReloadEvent event) {
        FishStewPlugin plugin = FishStewPlugin.getInstance();
        plugin.getLogger().info("Detected EvenMoreFish reload. Reloading...");
        plugin.reload();
    }

}
