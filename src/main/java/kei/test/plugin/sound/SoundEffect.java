package kei.test.plugin.sound;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class SoundEffect {

	private static SoundEffect singleton = new SoundEffect();

	private SoundEffect() {
	}

	public static SoundEffect getInstance() {
		return singleton;
	}

	public void playSoundEffect(Player player, Sound sound, double range) {
		Collection<Player> players = player.getLocation().getNearbyPlayers(range);

		float volume = (float) (Math.random() * 0.2f) + 0.8f;
		float pitch = (float) (Math.random() * 0.2f) + 0.8f;
		for (Player e : players) {
			Location loc = e.getLocation();
			e.playSound(loc, sound, volume, pitch);
		}
	}

	public void playSoundEffect(LivingEntity entity, Sound sound, double range) {
		Collection<Player> players = entity.getLocation().getNearbyPlayers(range);

		float volume = (float) (Math.random() * 0.2f) + 0.8f;
		float pitch = (float) (Math.random() * 0.2f) + 0.8f;
		for (Player e : players) {
			Location loc = e.getLocation();
			e.playSound(loc, sound, volume, pitch);
		}
	}

	public void playSoundAllPlayers(Sound sound) {
		Location loc;

		float volume = (float) (Math.random() * 0.2f) + 0.8f;
		float pitch = (float) (Math.random() * 0.2f) + 0.8f;
		for (Player player : Bukkit.getOnlinePlayers()) {
			loc = player.getLocation();
			if (loc != null && loc.getWorld() != null) {
				player.playSound(loc, sound, volume, pitch);
			}
		}
	}
}
