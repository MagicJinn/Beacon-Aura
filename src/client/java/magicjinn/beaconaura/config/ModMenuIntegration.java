package magicjinn.beaconaura.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi {
    private static final String YACL_MOD_ID = "yet_another_config_lib_v3";

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!FabricLoader.getInstance().isModLoaded(YACL_MOD_ID))
            return null; // No config button without YACL

        return yaclFactory();
    }

    /** Separate method so YACL classes are not resolved when YACL is absent. */
    private static ConfigScreenFactory<?> yaclFactory() {
        return YaclConfigScreen::create;
    }
}
