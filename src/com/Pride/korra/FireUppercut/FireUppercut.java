package com.Pride.korra.FireUppercut;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.FireAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class FireUppercut extends FireAbility implements AddonAbility {
	
	private static String path = "ExtraAbilities.Prride.FireUppercut.";
	private long cooldown;
	private double damage;
	private int fireTicks;
	private double radius;
	
	private Location particleStartLoc, particleEndLoc;

	public FireUppercut(Player player) {
		super(player);

		if (!bPlayer.canBend(this)) {
			return;
		}
		cooldown = ConfigManager.getConfig().getLong(path + "Cooldown");
		damage = ConfigManager.getConfig().getDouble(path + "Damage");
		fireTicks = ConfigManager.getConfig().getInt(path + "FireTicks");
		radius = ConfigManager.getConfig().getDouble(path + "Radius");

		particleStartLoc = GeneralMethods.getRightSide(player.getLocation(), .55).add(0, 0.8, 0);
		
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(particleStartLoc, radius)) {
			if (entity instanceof LivingEntity && entity.getEntityId() != player.getEntityId()) {
                            DamageHandler.damageEntity(entity, damage, this);
                            entity.setFireTicks(fireTicks);
                            entity.setVelocity(entity.getVelocity().add(new Vector(0, 1.1, 0)));
			}
		}
                player.setVelocity(player.getVelocity().add(new Vector(0, 0.5, 0)));
                
                particleEndLoc = GeneralMethods.getRightSide(player.getLocation(), .55).add(0, 5, 0);
                
                start();
                bPlayer.addCooldown(this);
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public Location getLocation() {
		return particleStartLoc;
	}

	@Override
	public String getName() {
		return "FireUppercut";
	}

	@Override
	public boolean isHarmlessAbility() {
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		return true;
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline()) {
                        bPlayer.addCooldown(this);
                        remove();
			return;
		}
		if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		
		Vector dir = GeneralMethods.getDirection(particleStartLoc, particleEndLoc);
		dir.normalize().multiply(0.5D);
		particleStartLoc.add(dir);
		
		ParticleEffect.FLAME.display(particleStartLoc, 0.2F, 0.2F, 0.2F, 0F, 8);
		ParticleEffect.CRIT.display(particleStartLoc, 0.2F, 0.2F, 0.2F, 0F, 8);
		
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(particleStartLoc, radius)) {
			if (entity instanceof LivingEntity && entity.getEntityId() != player.getEntityId()) {
				ParticleEffect.CRIT.display(entity.getLocation().add(0, 1, 0), 0.3F, 0.3F, 0.3F, 0F, 10);
			}
		}
		if (particleStartLoc.distanceSquared(particleEndLoc) < 0.1) {
                        remove();
			return;
		}

	}

	@Override
	public String getAuthor() {
		return "Prride & PhanaticD";
	}

	@Override
	public String getVersion() {
		return "FireUppercut Build 1";
	}
	
	@Override
	public String getDescription() {
		return "Uppercuts with a fist of fire. Launches enemies into the air and does fire damage.";
	}

	@Override
	public String getInstructions() {
		return "Sneak to launch an uppercut attack.";
	}

	@Override
	public void load() {
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new FireUppercutListener(), ProjectKorra.plugin);
		ProjectKorra.log.info(getName() + " " + getVersion() + " by " + getAuthor() + " loaded! ");
		
		ConfigManager.getConfig().addDefault(path + "Cooldown", 4500);
		ConfigManager.getConfig().addDefault(path + "Damage", 4);
		ConfigManager.getConfig().addDefault(path + "FireTicks", 100);
		ConfigManager.getConfig().addDefault(path + "Radius", 3);
		ConfigManager.defaultConfig.save();
	}

	@Override
	public void stop() {
		ProjectKorra.log.info(getName() + " " + getVersion() + " by " + getAuthor() + " stopped! ");
		super.remove();
	}

}
