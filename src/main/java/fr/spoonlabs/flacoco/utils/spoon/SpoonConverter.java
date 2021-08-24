package fr.spoonlabs.flacoco.utils.spoon;

import fr.spoonlabs.flacoco.api.result.Location;
import fr.spoonlabs.flacoco.api.result.Suspiciousness;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import org.apache.log4j.Logger;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts a mapping between line numbers and suspiciousness scores into a mapping betwen
 * CtStatements and suspiciousness scores.
 *
 * @author andre15silva
 */
public class SpoonConverter {

	private static Logger logger = Logger.getLogger(SpoonConverter.class);
	private static FlacocoConfig config = FlacocoConfig.getInstance();

	public static Map<CtStatement, Suspiciousness> convert(Map<Location, Suspiciousness> original) {
		logger.debug("Converting results to Spoon format...");
		logger.debug(original);

		// Init spoon Launcher
		Launcher launcher = new Launcher();
		for (String dir : config.getSrcJavaDir())
			launcher.addInputResource(dir);
		for (String dir : config.getSrcTestDir())
			launcher.addInputResource(dir);
		launcher.buildModel();
		launcher.addProcessor(new SpoonLocalizedFaultFinder());

		// Convert keys
		Map<CtStatement, Suspiciousness> result = new HashMap<>();
		for (Location location : original.keySet()) {
			// Compute location information
			SpoonLocalizedFaultFinder.fullyQualifiedClassName = location.getClassName();
			SpoonLocalizedFaultFinder.lineNumber = location.getLineNumber();

			// Launch processor to find the top-most CtStatement of the given line
			launcher.process();

			if (SpoonLocalizedFaultFinder.found == null) {
				logger.error("Spoon found no CtStatement for the location " +
						SpoonLocalizedFaultFinder.fullyQualifiedClassName + ":" + SpoonLocalizedFaultFinder.lineNumber);
				continue;
			}

			// Warning message that should never occur.
			if (result.containsKey(SpoonLocalizedFaultFinder.found) &&
					!result.get(SpoonLocalizedFaultFinder.found).equals(original.get(location))) {
				logger.error("Converting [" + location + "] to [" + SpoonLocalizedFaultFinder.found + "] resulted in a " +
						"duplicate key with different suspiciouness values. Please report this to the developers of " +
						"Flacoco on https://github.com/SpoonLabs/flacoco");
			}
			result.put(SpoonLocalizedFaultFinder.found, original.get(location));

			// Prepare for next key
			SpoonLocalizedFaultFinder.found = null;
		}

		return result;
	}

}
