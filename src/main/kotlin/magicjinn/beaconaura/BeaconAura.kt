package magicjinn.beaconaura

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object BeaconAura : ModInitializer {
    private val logger = LoggerFactory.getLogger("beacon-aura")

	override fun onInitialize() {
		logger.info("Beacon Aura initialized!")
	}
}