package magicjinn.beaconaura;

import net.fabricmc.api.ModInitializer;
import magicjinn.beaconaura.config.ModConfig;

public class BeaconAura implements ModInitializer {
    public static final String MOD_ID = "beacon-aura";

    @Override
    public void onInitialize() {
        // Load configuration
        ModConfig.load();
    }
}