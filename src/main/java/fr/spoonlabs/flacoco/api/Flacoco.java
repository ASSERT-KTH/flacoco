package fr.spoonlabs.flacoco.api;

import fr.spoonlabs.flacoco.api.result.FlacocoResult;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.FaultLocalizationRunner;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumRunner;
import org.apache.log4j.Logger;

/**
 * This class serves as the main entry point to Flacoco.
 *
 * @author andre15silva
 */
public class Flacoco implements FlacocoAPI {

	private Logger logger = Logger.getLogger(Flacoco.class);
	private FlacocoConfig config = FlacocoConfig.getInstance();

	public Flacoco() {
	}

	/**
	 * Run method for Flacoco.
	 * @return FlacocoResult populated with the fault localization data.
	 */
	@Override
	public FlacocoResult run() {
		this.logger.info("Running Flacoco...");
		return getRunner().run();
	}

	/**
	 * Helper method to get the correct runner according to the configuration
	 * @return A FaultLocalizationRunner according to the fault localization family
	 */
	private FaultLocalizationRunner getRunner() {
		switch (this.config.getFamily()) {
			case SPECTRUM_BASED:
				return new SpectrumRunner();
		}
		return null;
	}

}
