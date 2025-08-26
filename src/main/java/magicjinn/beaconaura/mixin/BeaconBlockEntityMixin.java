package magicjinn.beaconaura.mixin;

import magicjinn.beaconaura.config.ModConfig;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {

	// Timing constants (vanilla pulse cadence and behavior tweaks)
	private static final int TICKS_PER_SECOND = 20; // Ticks per second
	private static final int PULSE_SECONDS = 4; // Beacon re-applies effects every 4 seconds
	private static final int PULSE_INTERVAL_TICKS = PULSE_SECONDS * TICKS_PER_SECOND; // Amount of ticks between pulses

	/**
	 * @author MagicJinn
	 * @reason Beacon Aura overwrites this method to allow effect duration stacking
	 *         and range modification
	 */
	@Overwrite
	private static void applyPlayerEffects(World world, BlockPos pos, int beaconLevel,
			@Nullable RegistryEntry<StatusEffect> primaryEffect,
			@Nullable RegistryEntry<StatusEffect> secondaryEffect) {
		if (world.isClient || primaryEffect == null)
			return;

		// Amount of ticks each level adds to the effect per pulse
		final int extraTicksPerLevel = beaconLevel * ModConfig.extraSecondsPerLevel * TICKS_PER_SECOND;
		// Final amount of ticks to add
		final int ticksToAdd = PULSE_INTERVAL_TICKS + extraTicksPerLevel;
		// Maximum allowed duration based on beacon level
		final int maxEffectDuration = ModConfig.maxMinutesPerLevel * 60 * TICKS_PER_SECOND * beaconLevel;

		// Effect radius
		final double range = beaconLevel * ModConfig.rangePerLevel + ModConfig.rangeBase;

		final boolean isSecondarySameAsPrimary = Objects.equals(primaryEffect, secondaryEffect);
		final int amplifier = (beaconLevel >= 4 && isSecondarySameAsPrimary) ? 1 : 0;

		// Apply to all non-spectating players in range, spanning full world height
		// and down to bedrock
		final Box box = new Box(
				pos.getX() - range, world.getBottomY(), pos.getZ() - range,
				pos.getX() + range + 1, world.getHeight(), pos.getZ() + range + 1);

		final List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, box);
		if (players.isEmpty())
			return;

		applyEffectToPlayers(primaryEffect, players, ticksToAdd, maxEffectDuration, amplifier);
		// Secondary applies only at level 4+, and only if different from primary
		if (beaconLevel >= 4 && !isSecondarySameAsPrimary && secondaryEffect != null) {
			applyEffectToPlayers(secondaryEffect, players, ticksToAdd, maxEffectDuration, 0);
		}
	}

	/**
	 * Adds or extends the specified effect on each player, without exceeding the
	 * cap.
	 */
	private static void applyEffectToPlayers(@Nullable RegistryEntry<StatusEffect> effect,
			List<PlayerEntity> players,
			int ticksToAdd,
			int maxTicks,
			int amplifier) {
		if (effect == null)
			return;

		for (PlayerEntity player : players) {
			final StatusEffectInstance current = player.getStatusEffect(effect);
			// If no effect is present, add ticksToAdd + 1 second, to make sure it stays
			// Otherwise add ticksToAdd to the current effect
			final int newDuration = (current != null ? current.getDuration() + ticksToAdd
					: ticksToAdd + TICKS_PER_SECOND);
			if (newDuration < maxTicks) {
				// If within the allowed duration, apply the new effect
				// (don't use clamp or min to avoid small beacons overwriting eachother)
				player.addStatusEffect(new StatusEffectInstance(effect, newDuration, amplifier, true, true));
			}
		}
	}
}