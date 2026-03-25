package magicjinn.beaconaura;

import magicjinn.beaconaura.config.ModConfig;
import net.fabricmc.api.ModInitializer;

public class BeaconAura implements ModInitializer {
	public static final String MOD_ID = "beacon-aura";

	@Override
	public void onInitialize() {
		ModConfig.load();
	}
}