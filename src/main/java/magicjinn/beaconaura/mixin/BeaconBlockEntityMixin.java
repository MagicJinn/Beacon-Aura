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
    /**
	 * @author MagicJinn
	 * @reason Replaces vanilla beacon effect to apply effects every 3 ticks and extend duration if already present.
	 */
	@Overwrite
	private static void applyPlayerEffects(World world, BlockPos pos, int beaconLevel,
										   @Nullable RegistryEntry<StatusEffect> primaryEffect,
										   @Nullable RegistryEntry<StatusEffect> secondaryEffect) {
		if (world.isClient || primaryEffect == null) return;

		// Beacon pulse interval in ticks (4 seconds)
		int pulseIntervalTicks = 4 * 20;

		// Extra seconds per level: Level 1 = +1s, Level 2 = +2s, etc.
		int extraTicksPerLevel = beaconLevel * 20; // 20 ticks per second

		// Total ticks to add this pulse
		int ticksToAdd = pulseIntervalTicks + extraTicksPerLevel;

		// Max effect duration per level (15 minutes per level)
		int maxEffectDurationPerLevel = 20 * 60 * 15;
		int maxEffectDuration = maxEffectDurationPerLevel * beaconLevel;

		double range = beaconLevel * 10 + 10;
		int amplifier = (beaconLevel >= 4 && Objects.equals(primaryEffect, secondaryEffect)) ? 1 : 0;

		Box box = new Box(pos).expand(range).stretch(0.0F, world.getHeight(), 0.0F);
		List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, box);

		for (PlayerEntity player : players) {
			StatusEffectInstance current = player.getStatusEffect(primaryEffect);
			int newDuration = (current != null ? current.getDuration() + ticksToAdd : ticksToAdd);
			newDuration = Math.min(newDuration, maxEffectDuration);
			player.addStatusEffect(new StatusEffectInstance(primaryEffect, newDuration, amplifier, true, true));
		}

		if (beaconLevel >= 4 && !Objects.equals(primaryEffect, secondaryEffect) && secondaryEffect != null) {
			for (PlayerEntity player : players) {
				StatusEffectInstance current = player.getStatusEffect(secondaryEffect);
				int newDuration = (current != null ? current.getDuration() + ticksToAdd : ticksToAdd);
				newDuration = Math.min(newDuration, maxEffectDuration);
				player.addStatusEffect(new StatusEffectInstance(secondaryEffect, newDuration, 0, true, true));
			}
		}
	}
}
