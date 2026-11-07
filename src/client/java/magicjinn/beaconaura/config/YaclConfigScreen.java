package magicjinn.beaconaura.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import magicjinn.beaconaura.BeaconAura;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;
import java.util.function.Supplier;

/** Loaded only when YACL is present. Keep YACL types out of {@link ModMenuIntegration}. */
final class YaclConfigScreen {
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 99999;
    private static final String CATEGORY_NAME = "Beacon Aura";

    private YaclConfigScreen() {
    }

    static Screen create(Screen parentScreen) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal(""))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal(CATEGORY_NAME))
                        .tooltip(Component.literal(""))
                        .option(createOption(ModData.EXTRA_SECONDS))
                        .option(createOption(ModData.MAX_MINUTES))
                        .build())
                .save(ModConfig::save)
                .build()
                .generateScreen(parentScreen);
    }

    private static Option<Integer> createOption(ModData modData) {
        return Option.<Integer>createBuilder()
                .name(Component.literal(modData.label))
                .description(OptionDescription.createBuilder()
                        .text(Component.literal(modData.desc))
                        .webpImage(modData.image)
                        .build())
                .binding(modData.defaultValue, modData.getter, modData.setter)
                .controller(opt -> IntegerFieldControllerBuilder.create(opt).range(MIN_VALUE, MAX_VALUE))
                .build();
    }

    private static Identifier image(String name) {
        return Identifier.fromNamespaceAndPath(BeaconAura.MOD_ID, "textures/gui/" + name + ".webp");
    }

    private enum ModData {
        EXTRA_SECONDS("Seconds Per Level",
                """
                        The amount of seconds added to the beacon effect's duration per beacon pulse, for each level of the beacon.
                        A beacon will always replenish at least 4 seconds on top of this value to maintain the effect, as the effect is applied every 4 seconds.

                        The total duration added each pulse is calculated as:
                        4 + (Extra Seconds Per Level * Beacon Level)


                        Mod default: 4
                        """,
                image("extra_seconds_image"), ModConfig.extraSecondsPerLevel,
                () -> ModConfig.extraSecondsPerLevel,
                v -> ModConfig.extraSecondsPerLevel = v),
        MAX_MINUTES("Maximum Minutes Per Level",
                """
                        The maximum duration, in minutes, that the beacon's effect can last, before it stops increasing, for each level of the beacon.
                        For example, a value of 15 will allow a level 1 beacon to last for a maximum of 15 minutes, a level 2 beacon for 30 minutes, and a level 4 beacon for 60 minutes.


                        Mod default: 15
                        """,
                image("max_minutes_image"), ModConfig.maxMinutesPerLevel,
                () -> ModConfig.maxMinutesPerLevel,
                v -> ModConfig.maxMinutesPerLevel = v);

        final String label;
        final String desc;
        final Identifier image;
        final int defaultValue;
        final Supplier<Integer> getter;
        final Consumer<Integer> setter;

        ModData(String label, String desc, Identifier image, int defaultValue,
                Supplier<Integer> getter, Consumer<Integer> setter) {
            this.label = label;
            this.desc = desc;
            this.image = image;
            this.defaultValue = defaultValue;
            this.getter = getter;
            this.setter = setter;
        }
    }
}
