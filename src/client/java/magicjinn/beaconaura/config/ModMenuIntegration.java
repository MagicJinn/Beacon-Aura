package magicjinn.beaconaura.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.config.v2.api.autogen.CustomImage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

        // UI Text Constants
        private static final String TITLE = "Beacon Aura Settings";
        private static final String CATEGORY_NAME = "Beacon Aura Tuning";
        private static final String CATEGORY_TOOLTIP = "Tune how much time is added per pulse, the duration cap, and the range formula.";

        // Option Labels
        private static final String EXTRA_SECONDS_LABEL = "Extra Seconds Per Level";
        private static final String MAX_MINUTES_LABEL = "Max Minutes Per Level";
        private static final String RANGE_BASE_LABEL = "Range Base";
        private static final String RANGE_PER_LEVEL_LABEL = "Range Per Level";

        // Option Descriptions
        private static final String EXTRA_SECONDS_DESC = "Seconds added per beacon level each pulse.";
        private static final String MAX_MINUTES_DESC = "Maximum effect duration cap per level (in minutes).";
        private static final String RANGE_BASE_DESC = "Base range added regardless of beacon level (blocks).";
        private static final String RANGE_PER_LEVEL_DESC = "Additional range per beacon level (blocks).";

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
                                                                                .range(0, 60))
                                                                .build())

                                                .option(Option.<Integer>createBuilder()
                                                                .name(Text.literal(MAX_MINUTES_LABEL))
                                                                .description(OptionDescription
                                                                                .of(Text.literal(MAX_MINUTES_DESC)))
                                                                .binding(15, () -> ModConfig.maxMinutesPerLevel,
                                                                                v -> ModConfig.maxMinutesPerLevel = v)
                                                                .controller(opt -> IntegerFieldControllerBuilder
                                                                                .create(opt)
                                                                                .range(1, 60))
                                                                .build())

                                                .option(Option.<Integer>createBuilder()
                                                                .name(Text.literal(RANGE_BASE_LABEL))
                                                                .description(OptionDescription
                                                                                .of(Text.literal(RANGE_BASE_DESC)))
                                                                .binding(10, () -> ModConfig.rangeBase,
                                                                                v -> ModConfig.rangeBase = v)
                                                                .controller(opt -> IntegerFieldControllerBuilder
                                                                                .create(opt)
                                                                                .range(0, 128))
                                                                .build())

                                                .option(Option.<Integer>createBuilder()
                                                                .name(Text.literal(RANGE_PER_LEVEL_LABEL))
                                                                .description(OptionDescription
                                                                                .of(Text.literal(RANGE_PER_LEVEL_DESC)))
                                                                .binding(10, () -> ModConfig.rangePerLevel,
                                                                                v -> ModConfig.rangePerLevel = v)
                                                                .controller(opt -> IntegerFieldControllerBuilder
                                                                                .create(opt)
                                                                                .range(0, 128))
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
