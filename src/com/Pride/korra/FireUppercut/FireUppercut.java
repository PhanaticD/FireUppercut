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
	
	private static String path = "ExtraAbilities.Prride.AirCocoon.";
	private long cooldown;
	private double damage;
	private int fireTicks;
	
	private Location location;

	public FireUppercut(Player player) {
		super(player);
		// TODO Auto-generated constructor stub
		if (!bPlayer.canBend(this)) {
			return;
		}
		cooldown = ConfigManager.getConfig().getLong(path + "Cooldown");
		damage = ConfigManager.getConfig().getDouble(path + "Damage");
		fireTicks = ConfigManager.getConfig().getInt(path + "FireTicks");
		location = GeneralMethods.getRightSide(player.getLocation(), .55).add(0, 0.8, 0);
		start();
	}

	@Override
	public long getCooldown() {
		// TODO Auto-generated method stub
		return cooldown;
	}

	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "FireUppercut";
	}

	@Override
	public boolean isHarmlessAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void progress() {
		// TODO Auto-generated method stub
		if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		if (player.isDead() || !player.isOnline()) {
			remove();
			return;
		}
		Location newLoc = GeneralMethods.getRightSide(player.getLocation(), .55).add(0, 2.8, 0);
		Vector dir = GeneralMethods.getDirection(location, newLoc);
		dir.normalize().multiply(0.5D);
		location.add(dir);
		
		if (location.distanceSquared(newLoc) < 0.1 * 0.1) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		ParticleEffect.FLAME.display(location, 0.2F, 0.2F, 0.2F, 0F, 8);
		ParticleEffect.CRIT.display(location, 0.2F, 0.2F, 0.2F, 0F, 8);
		
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(location, 2)) {
			if (entity instanceof LivingEntity && entity.getEntityId() != player.getEntityId()) {
				launchPlayer(entity);
				DamageHandler.damageEntity(entity, damage, this);
				entity.setFireTicks(fireTicks);
			}
		}
	}
	
	private void launchPlayer(Entity entity) {
		Vector vec = entity.getVelocity();
		vec.setY(0.7);
		entity.setVelocity(vec);
		return;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "Prride";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "FireUppercut Build 1";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Uppercuts with a fist of fire. Launches enemies into the air and does fire damage.";
	}

	@Override
	public String getInstructions() {
		// TODO Auto-generated method stub
		return "Sneak to launch an uppercut attack.";
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new UppercutListener(), ProjectKorra.plugin);
		ProjectKorra.log.info(getName() + " " + getVersion() + " by " + getAuthor() + " loaded! ");
		
		ConfigManager.getConfig().addDefault(path + "Cooldown", 4500);
		ConfigManager.getConfig().addDefault(path + "Damage", 4);
		ConfigManager.getConfig().addDefault(path + "FireTicks", 100);
		ConfigManager.defaultConfig.save();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		ProjectKorra.log.info(getName() + " " + getVersion() + " by " + getAuthor() + " stopped! ");
		super.remove();
	}

}
