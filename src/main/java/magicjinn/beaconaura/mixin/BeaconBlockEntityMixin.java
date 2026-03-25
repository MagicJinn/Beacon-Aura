package magicjinn.beaconaura.mixin;

import java.util.List;
import java.util.Objects;

import magicjinn.beaconaura.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    private static final int TICKS_PER_SECOND = 20; // Ticks per second
    private static final int PULSE_SECONDS = 4; // Beacon re-applies effects every 4 seconds
    private static final int PULSE_INTERVAL_TICKS = PULSE_SECONDS * TICKS_PER_SECOND; // Amount of ticks between pulses

    /**
     * @author MagicJinn
     * @reason Beacon Aura overwrites this method to allow effect duration stacking
     *         and range modification
     */
    @Overwrite
    private static void applyPlayerEffects(Level world, BlockPos pos, int beaconLevel,
            @Nullable Holder<MobEffect> primaryEffect,
            @Nullable Holder<MobEffect> secondaryEffect) {
        if (world.isClientSide() || primaryEffect == null) {
            return;
        }

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

        // Apply to all non-spectating players in range, spanning full world height and
        // down to bedrock
        final AABB box = new AABB(
                pos.getX() - range, world.getMinY(), pos.getZ() - range,
                pos.getX() + range + 1.0D, world.getMaxY(), pos.getZ() + range + 1.0D);

        final List<Player> players = world
                .getEntities((Entity) null, box, entity -> entity instanceof Player && !entity.isSpectator())
                .stream()
                .map(entity -> (Player) entity)
                .toList();
        if (players.isEmpty()) {
            return;
        }

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
    private static void applyEffectToPlayers(@Nullable Holder<MobEffect> effect,
            List<Player> players,
            int ticksToAdd,
            int maxTicks,
            int amplifier) {
        if (effect == null) {
            return;
        }

        for (Player player : players) {
            // Information about the current effect
            final MobEffectInstance currentEffect = player.getEffect(effect);
            final boolean effectPresent = currentEffect != null;

            final int oldAmplifier = effectPresent ? currentEffect.getAmplifier() : -1;
            final boolean amplifierChanged = oldAmplifier != amplifier;

            // Default duration if no effect present or amplifier changed
            int newDuration = ticksToAdd + TICKS_PER_SECOND;

            // If effect present and amplifier unchanged, stack durations
            if (effectPresent && !amplifierChanged) {
                newDuration = currentEffect.getDuration() + ticksToAdd;
            }

            // If within the allowed duration, apply the new effect
            if (newDuration < maxTicks) {
                player.addEffect(new MobEffectInstance(effect, newDuration, amplifier, true, false));
            }
        }
    }
}
