package magicjinn.beaconaura.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import magicjinn.beaconaura.BeaconAura;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ModMenuIntegration implements ModMenuApi {

    private static final int MINVALUE = 0; // Who cares
    private static final int MAXVALUE = 99999; // Just to prevent people from putting it as maxInt

    private static final String TITLE = "Beacon Aura Settings"; // This doesn't seem to do anything
    private static final String CATEGORY_NAME = "Beacon Aura";
    private static final String CATEGORY_TOOLTIP = "Tune various settings for the mod.";

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
                        .option(createOption(ModData.EXTRA_SECONDS))
                        .option(createOption(ModData.MAX_MINUTES))
                        .option(createOption(ModData.RANGE_BASE))
                        .option(createOption(ModData.RANGE_PER_LEVEL))
                        .build())
                .save(() -> {
                    // Save the config when the user clicks save
                    ModConfig.save();
                })
                .build()
                .generateScreen(parentScreen);
    }

    private static Option<Integer> createOption(ModData dict) {
        return Option.<Integer>createBuilder()
                .name(Text.literal(dict.label))
                .description(OptionDescription.createBuilder()
                        .text(Text.literal(dict.desc))
                        .webpImage(dict.image)
                        .build())
                .binding(4, () -> ModConfig.extraSecondsPerLevel,
                        v -> ModConfig.extraSecondsPerLevel = v)
                .controller(opt -> IntegerFieldControllerBuilder
                        .create(opt)
                        .range(MINVALUE, MAXVALUE))
                .build();
    }

    private static Identifier gimage(String name) {
        return Identifier.of(BeaconAura.MOD_ID, "textures/gui/" + name + ".webp");
    }

    private enum ModData {
        EXTRA_SECONDS(
                "Seconds Per Level",
                """
                        The amount of seconds added to the beacon effect's duration per beacon pulse, for each level of the beacon.
                        A beacon will always replenish at least 4 seconds on top of this value to maintain the effect, as the effect is applied every 4 seconds.

                        The total duration added each pulse is calculated as:
                        4 + (Extra Seconds Per Level * Beacon Level)


                        Mod default: 4
                        """,
                gimage("extra_seconds_image")),
        MAX_MINUTES(
                "Maximum Minutes Per Level",
                """
                        The maximum duration, in minutes, that the beacon's effect can last, before it stops increasing, for each level of the beacon.
                        For example, a value of 15 will allow a level 1 beacon to last for a maximum of 15 minutes, a level 2 beacon for 30 minutes, and a level 4 beacon for 60 minutes.


                        Mod default: 15
                        """,
                gimage("max_minutes_image")),
        RANGE_BASE(
                "Range Base",
                """
                        The base range, in blocks, that the beacon effect extends from the beacon, regardless of the beacon's level.
                        This range is a radius extending outwards from the beacon in all horizontal directions.


                        Vanilla: 10

                        Mod default: 32
                        """,
                gimage("range_base_image")),
        RANGE_PER_LEVEL(
                "Range Per Level",
                """
                        The additional range, in blocks, added to the beacon effect's radius for each level of the beacon.
                        This value is multiplied by the beacon level and added to the base range to determine the total range of the beacon effect.


                        Vanilla: 10

                        Mod default: 32
                        """,
                gimage("range_per_level_image"));

        public final String label;
        public final String desc;
        public final Identifier image;

        ModData(String label, String desc, Identifier image) {
            this.label = label;
            this.desc = desc;
            this.image = image;
        }

    }


    public static void main(String[] args) {
        Map<ModData, Map<String, Object>> configValues = new HashMap<>();
        for (ModData modData : ModData.values()) {
            configValues.put(modData, Map.of(
                    "LABEL", modData.label,
                    "DESC", modData.desc,
                    "IMAGE", modData.image));
        }
    }

}