package com.Pride.korra.FireUppercut;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;

public class UppercutListener implements Listener {
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (!event.isSneaking()) {
			return;
		}
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(event.getPlayer());
		if (bPlayer != null && bPlayer.canBend(CoreAbility.getAbility("FireUppercut")) && CoreAbility.getAbility(event.getPlayer(), FireUppercut.class) == null) {
			new FireUppercut(event.getPlayer());
		}
	}

}
