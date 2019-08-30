package eu.wauz.wauzcore.skills;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;

public class SkillTheChariot implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Chariot VII";

	@Override
	public String getSkillId() {
		return SKILL_NAME;
	}
	
	@Override
	public String getSkillDescriptionType() {
		return "AoE";
	}

	@Override
	public String getSkillDescriptionEffect() {
		return "Knockback Charge";
	}

	@Override
	public int getCooldownSeconds() {
		return 18;
	}

	@Override
	public int getManaCost() {
		return 4;
	}

	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		Location origin = player.getLocation();
		Location target = origin.clone().add(origin.getDirection().multiply(10).setY(0));
		
		SkillParticle particle = new SkillParticle(Particle.CLOUD);
		SkillUtils.spawnParticleLine(origin, target, particle, 3);
		SkillUtils.throwEntity(player, target);

		Set<Entity> targets = new HashSet<>();
		targets.addAll(SkillUtils.getTargetsInRadius(origin, 2.5));
		targets.addAll(SkillUtils.getTargetsInRadius(target, 2.5));
		for(Entity entity : targets) {
			SkillUtils.throwBackEntity(entity, origin, 1.6);
		}
		return targets.size() > 0;
	}

}
