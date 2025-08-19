package magicjinn.beaconaura.mixin;

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
	private static final int TICKS_PER_SECOND = 20;
	private static final int PULSE_SECONDS = 4; // Beacon re-applies effects every 4 seconds
	private static final int EXTRA_SECONDS_PER_LEVEL = 4; // Additional duration added per level each pulse
	private static final int MAX_MINUTES_PER_LEVEL = 15; // Cap duration per level

	/**
	 * Overwrites vanilla beacon effect logic.
	 * Behavior:
	 * - Pulses every 4 seconds (vanilla).
	 * - Extends existing effect duration instead of resetting it.
	 * - Caps the accumulated duration to (15 minutes) × (beacon level).
	 * - Adds (4 + 4 * beaconLevel) seconds of duration on each pulse
	 * (e.g., level 1 → 8s, level 4 → 20s).
	 */
	@Overwrite
	private static void applyPlayerEffects(World world, BlockPos pos, int beaconLevel,
										   @Nullable RegistryEntry<StatusEffect> primaryEffect,
										   @Nullable RegistryEntry<StatusEffect> secondaryEffect) {
		// Server-only; no-op if no primary effect (mirrors vanilla gate)
		if (world.isClient || primaryEffect == null) return;

		// Amount of time added to the effect this pulse
		final int pulseIntervalTicks = PULSE_SECONDS * TICKS_PER_SECOND;
		final int extraTicksPerLevel = beaconLevel * EXTRA_SECONDS_PER_LEVEL * TICKS_PER_SECOND;
		final int ticksToAdd = pulseIntervalTicks + extraTicksPerLevel;

		// Maximum allowed duration based on beacon level
		final int maxEffectDuration = MAX_MINUTES_PER_LEVEL * 60 * TICKS_PER_SECOND * beaconLevel;

		// Effect radius: base 10 + 10 per beacon level (vanilla)
		final double range = beaconLevel * 10 + 10;

		final boolean secondarySameAsPrimary = Objects.equals(primaryEffect, secondaryEffect);
		final int amplifier = (beaconLevel >= 4 && secondarySameAsPrimary) ? 1 : 0;

		// Apply to all non-spectating players in range, spanning full world height
		final Box box = new Box(pos).expand(range).stretch(0.0F, world.getHeight(), 0.0F);
		final List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, box);
		if (players.isEmpty())
			return;

		applyEffectToPlayers(primaryEffect, players, ticksToAdd, maxEffectDuration, amplifier);

		// Secondary applies only at level 4+, and only if different from primary
		if (beaconLevel >= 4 && !secondarySameAsPrimary && secondaryEffect != null) {
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
		if (effect == null || players.isEmpty())
			return;

		for (PlayerEntity player : players) {
			final StatusEffectInstance current = player.getStatusEffect(effect);
			final int newDuration = (current != null ? current.getDuration() + ticksToAdd : ticksToAdd);
			if (newDuration < maxTicks) {
				// Ambient + showParticles = true to mirror beacon-style visuals
				player.addStatusEffect(new StatusEffectInstance(effect, newDuration, amplifier, true, true));
			}
		}
	}
}