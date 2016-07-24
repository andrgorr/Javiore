package pw.javipepe.javiore.modules.rankManager.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import pw.javipepe.javiore.modules.rankManager.RankManagerModule;


/**
 * @author: Javi
 * <p>
 * Class created on 3/07/16 as part of project Javiore
 **/
public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin (PlayerJoinEvent event) {
        RankManagerModule.updateRankPlayer(event.getPlayer());
    }

    @EventHandler
    public void onChat (AsyncPlayerChatEvent event) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',event.getPlayer().getDisplayName() + " &7► &r" + event.getMessage()));
        event.setCancelled(true);
    }
}
