package fr.spoonlabs.flacoco.utils.spoon;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import org.apache.log4j.Logger;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;

import java.io.File;
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

	public static Map<CtStatement, Double> convert(Map<String, Double> original) {
		logger.debug("Converting results to Spoon format...");
		logger.debug(original);

		// Init spoon Launcher
		Launcher launcher = new Launcher();
		launcher.addInputResource(new File(config.getProjectPath() + File.separator + "src/main").getAbsolutePath());
		launcher.addInputResource(new File(config.getProjectPath() + File.separator + "src/test").getAbsolutePath());
		launcher.buildModel();
		launcher.addProcessor(new SpoonLocalizedFaultFinder());

		// Convert keys
		Map<CtStatement, Double> result = new HashMap<>();
		for (String key : original.keySet()) {
			// Compute location information
			SpoonLocalizedFaultFinder.fullyQualifiedClassName = key.substring(0, key.indexOf("@"))
					.replace("/", ".");
			SpoonLocalizedFaultFinder.lineNumber = Integer.parseInt(key.substring(key.lastIndexOf("@") + 1));

			// Launch processor to find the top-most CtStatement of the given line
			launcher.process();

			if (SpoonLocalizedFaultFinder.found == null) {
				logger.error("Spoon found no CtStatement for the location " +
						SpoonLocalizedFaultFinder.fullyQualifiedClassName + ":" + SpoonLocalizedFaultFinder.lineNumber);
				continue;
			}

			// Warning message that should never occur.
			if (result.containsKey(SpoonLocalizedFaultFinder.found) &&
					!result.get(SpoonLocalizedFaultFinder.found).equals(original.get(key))) {
				logger.error("Converting [" + key + "] to [" + SpoonLocalizedFaultFinder.found + "] resulted in a " +
						"duplicate key with different suspiciouness values. Please report this to the developers of " +
						"Flacoco on https://github.com/SpoonLabs/flacoco");
			}
			result.put(SpoonLocalizedFaultFinder.found, original.get(key));

			// Prepare for next key
			SpoonLocalizedFaultFinder.found = null;
		}

		return result;
	}

}
