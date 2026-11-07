package magicjinn.beaconaura.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import magicjinn.beaconaura.config.ModConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    private static final int TICKS_PER_SECOND = 20;
    private static final int PULSE_INTERVAL_TICKS = 4 * TICKS_PER_SECOND; // Amount of ticks between pulses

    /**
     * Stacks duration at each {@code addEffect} call instead of replacing
     * {@code applyEffects}. Writes the stacked value back into the local
     * {@code durationTicks} so injects like BeaconSaturation that read that
     * local after the secondary apply still see the stacked duration.
     */
    @WrapOperation(method = "applyEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
    private static boolean stackEffectDuration(
            Player player,
            MobEffectInstance incoming,
            Operation<Boolean> original,
            @Local(argsOnly = true) int levels,
            @Local(name = "durationTicks") LocalIntRef durationTicks) {
        final Holder<MobEffect> effect = incoming.getEffect();
        final int amplifier = incoming.getAmplifier();

        final int ticksToAdd = PULSE_INTERVAL_TICKS
                + levels * ModConfig.extraSecondsPerLevel * TICKS_PER_SECOND;
        final int maxTicks = ModConfig.maxMinutesPerLevel * 60 * TICKS_PER_SECOND * levels;

        final MobEffectInstance current = player.getEffect(effect);
        int newDuration = ticksToAdd + TICKS_PER_SECOND;
        if (current != null && current.getAmplifier() == amplifier) {
            newDuration = current.getDuration() + ticksToAdd;
        }
        newDuration = Math.min(newDuration, maxTicks);

        // Keep the shared local in sync for other mixins (e.g. BeaconSaturation).
        durationTicks.set(newDuration);

        return original.call(player,
                new MobEffectInstance(effect, newDuration, amplifier, true, false));
    }
}
