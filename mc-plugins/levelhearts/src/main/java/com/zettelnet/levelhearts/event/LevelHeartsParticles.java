package com.zettelnet.levelhearts.event;

import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.zettelnet.levelhearts.LevelHearts;
import com.zettelnet.levelhearts.LevelHeartsPlugin;

public class LevelHeartsParticles implements Listener {

	private final LevelHeartsPlugin plugin;

	public LevelHeartsParticles() {
		this.plugin = LevelHearts.getPlugin();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMaxHealthChange(PlayerMaxHealthChangeEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		final Location loc = player.getLocation().add(0, 2, 0);

		try {
			if (LevelHearts.getConfiguration().maxHealthParticleEffectIncrease() && event.getMaxHealthChange() > 0) {
				if (plugin.isChristmas()) {
					new ChristmasRunnable(player).schedule();
				}
				loc.getWorld().spawnParticle(Particle.HEART, loc, 1);
			}
			if (LevelHearts.getConfiguration().maxHealthParticleEffectDecrease() && event.getMaxHealthChange() < 0) {
				loc.getWorld().spawnParticle(Particle.SPELL_MOB_AMBIENT, loc, 1);
			}
		} catch (Exception e) {
			System.err.println("An error occured while trying to send particles effects to " + player.getName()
					+ ". Make sure to update LevelHearts to the latest version. Disable particle effects in config.yml if that does not resolve the issue :[");
		}
	}

	public class ChristmasRunnable extends BukkitRunnable {

		private final Player player;

		private int delay;
		private int state;

		private final Note F_SHARP = Note.sharp(0, Tone.F);
		private final Note A = Note.natural(0, Tone.A);
		private final Note D = Note.natural(0, Tone.D);
		private final Note E = Note.natural(0, Tone.E);
		private final Note G = Note.natural(0, Tone.G);
		
		private final Instrument INSTRUMENT = Instrument.PIANO;

		public ChristmasRunnable(Player player) {
			this.player = player;
			this.delay = 0;
			this.state = 0;
		}

		public void schedule() {
			runTaskTimer(plugin, 0, 2);
		}

		@Override
		public void run() {
			if (delay > 0) {
				delay--;
				return;
			}
			Location loc = player.getLocation();
			switch (state) {
			case 0:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 2;
				break;
			case 1:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 2;
				break;
			case 2:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 4;
				break;
			case 3:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 2;
				break;
			case 4:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 2;
				break;
			case 5:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 4;
				break;
			case 6:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 3;
				break;
			case 7:
				player.playNote(loc, INSTRUMENT, A);
				delay = 1;
				break;
			case 8:
				player.playNote(loc, INSTRUMENT, D);
				delay = 2;
				break;
			case 9:
				player.playNote(loc, INSTRUMENT, E);
				delay = 2;
				break;
			case 10:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 6;
				break;
			case 12:
				player.playNote(loc, INSTRUMENT, G);
				delay = 2;
				break;
			case 13:
				player.playNote(loc, INSTRUMENT, G);
				delay = 2;
				break;
			case 14:
				player.playNote(loc, INSTRUMENT, G);
				delay = 3;
				break;
			case 15:
				player.playNote(loc, INSTRUMENT, G);
				delay = 1;
				break;
			case 16:
				player.playNote(loc, INSTRUMENT, G);
				delay = 2;
				break;
			case 17:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 2;
				break;
			case 18:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 3;
				break;
			case 19:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 1;
				break;
			case 20:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 2;
				break;
			case 21:
				player.playNote(loc, INSTRUMENT, E);
				delay = 2;
				break;
			case 22:
				player.playNote(loc, INSTRUMENT, E);
				delay = 2;
				break;
			case 23:
				player.playNote(loc, INSTRUMENT, F_SHARP);
				delay = 2;
				break;
			case 24:
				player.playNote(loc, INSTRUMENT, E);
				delay = 4;
				break;
			case 25:
				player.playNote(loc, INSTRUMENT, A);
				break;
			default:
				cancel();
				break;
			}
			state++;
		}

	}
}
