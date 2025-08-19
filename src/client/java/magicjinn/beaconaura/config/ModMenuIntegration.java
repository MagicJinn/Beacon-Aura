package magicjinn.beaconaura.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

        private static final int MINVALUE = 0; // Who cares
        private static final int MAXVALUE = 99999; // Just to prevent people from putting it as maxInt

        private static final String TITLE = "Beacon Aura Settings"; // This doesn't seem to do anything
        private static final String CATEGORY_NAME = "Beacon Aura";
        private static final String CATEGORY_TOOLTIP = "Tune various settings for the mod.";

        private static final String EXTRA_SECONDS_LABEL = "Seconds Per Level";
        private static final String EXTRA_SECONDS_DESC = """
                        The amount of seconds added to the beacon effect's duration per beacon pulse, for each level of the beacon.
                        A beacon will always replenish at least 4 seconds on top of this value to maintain the effect, as the effect is applied every 4 seconds.
                        The total duration added each pulse is calculated as: 4 + (Extra Seconds Per Level * Beacon Level)
                        """;

        private static final String MAX_MINUTES_LABEL = "Maximum Minutes Per Level";
        private static final String MAX_MINUTES_DESC = """
                        The maximum duration, in minutes, that the beacon's effect can last, before it stops increasing, for each level of the beacon.
                        For example, a value of 15 will allow a level 1 beacon to last for a maximum of 15 minutes, a level 2 beacon for 30 minutes, and a level 4 beacon for 60 minutes.
                        """;

        private static final String RANGE_BASE_LABEL = "Range Base";
        private static final String RANGE_BASE_DESC = """
                        The base range, in blocks, that the beacon effect extends from the beacon, regardless of the beacon's level.
                        This range is a radius extending outwards from the beacon in all horizontal directions.
                        """;

        private static final String RANGE_PER_LEVEL_LABEL = "Range Per Level";
        private static final String RANGE_PER_LEVEL_DESC = """
                        The additional range, in blocks, added to the beacon effect's radius for each level of the beacon.
                        This value is multiplied by the beacon level and added to the base range to determine the total range of the beacon effect.
                        """;

        @Override
        public ConfigScreenFactory<?> getModConfigScreenFactory() {
                return parentScreen -> createConfigScreen(parentScreen);
        }

        public static Screen createConfigScreen(Screen parentScreen) {
                return YetAnotherConfigLib.createBuilder()
                                .title(Text.literal(TITLE))
                                .category(ConfigCategory.createBuilder()
                                                .name(Text.literal(CATEGORY_NAME))
                                                .tooltip(Text.literal(CATEGORY_TOOLTIP))
                                                .option(Option.<Integer>createBuilder()
                                                                .name(Text.literal(EXTRA_SECONDS_LABEL))
                                                                .description(OptionDescription
                                                                                .of(Text.literal(EXTRA_SECONDS_DESC)))
                                                                .binding(4, () -> ModConfig.extraSecondsPerLevel,
                                                                                v -> ModConfig.extraSecondsPerLevel = v)
                                                                .controller(opt -> IntegerFieldControllerBuilder
                                                                                .create(opt)
                                                                                .range(MINVALUE, MAXVALUE))
                                                                .build())

                                                .option(Option.<Integer>createBuilder()
                                                                .name(Text.literal(MAX_MINUTES_LABEL))
                                                                .description(OptionDescription
                                                                                .of(Text.literal(MAX_MINUTES_DESC)))
                                                                .binding(15, () -> ModConfig.maxMinutesPerLevel,
                                                                                v -> ModConfig.maxMinutesPerLevel = v)
                                                                .controller(opt -> IntegerFieldControllerBuilder
                                                                                .create(opt)
                                                                                .range(MINVALUE, MAXVALUE))
                                                                .build())

                                                .option(Option.<Integer>createBuilder()
                                                                .name(Text.literal(RANGE_BASE_LABEL))
                                                                .description(OptionDescription
                                                                                .of(Text.literal(RANGE_BASE_DESC)))
                                                                .binding(10, () -> ModConfig.rangeBase,
                                                                                v -> ModConfig.rangeBase = v)
                                                                .controller(opt -> IntegerFieldControllerBuilder
                                                                                .create(opt)
                                                                                .range(MINVALUE, MAXVALUE))
                                                                .build())

                                                .option(Option.<Integer>createBuilder()
                                                                .name(Text.literal(RANGE_PER_LEVEL_LABEL))
                                                                .description(OptionDescription
                                                                                .of(Text.literal(RANGE_PER_LEVEL_DESC)))
                                                                .binding(10, () -> ModConfig.rangePerLevel,
                                                                                v -> ModConfig.rangePerLevel = v)
                                                                .controller(opt -> IntegerFieldControllerBuilder
                                                                                .create(opt)
                                                                                .range(MINVALUE, MAXVALUE))
                                                                .build())

                                                .build())
                                .save(() -> {
                                        // Save the config when the user clicks save
                                        ModConfig.save();
                                })
                                .build()
                                .generateScreen(parentScreen);
        }
}
