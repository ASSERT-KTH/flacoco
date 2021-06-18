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
		launcher.addInputResource(config.getProjectPath() + File.separator + "src/main");
		launcher.addInputResource(config.getProjectPath() + File.separator + "src/test");
		launcher.buildModel();
		launcher.addProcessor(new SpoonLocalizedFaultFinder());

		// Convert keys
		Map<CtStatement, Double> result = new HashMap<>();
		for (String key : original.keySet()) {
			// Compute location information
			SpoonLocalizedFaultFinder.fullyQualifiedClassName = key.substring(0, key.indexOf("@"))
					.replace("/", ".");
			SpoonLocalizedFaultFinder.lineNumber = Integer.parseInt(key.substring(key.lastIndexOf("@") + 1));

			// Launch processor to find the top-most CtElement of the given line
			launcher.process();

			if (SpoonLocalizedFaultFinder.found == null) {
				logger.error("Spoon found no CtElement for the location " +
						SpoonLocalizedFaultFinder.fullyQualifiedClassName + ":" + SpoonLocalizedFaultFinder.lineNumber);
				continue;
			}

			System.out.println(key);
			System.out.println(SpoonLocalizedFaultFinder.found);
			// FIXME: If the original key maps to the same CtStatement, then we potentially have a problem
			result.put(SpoonLocalizedFaultFinder.found, original.get(key));

			// Prepare for next key
			SpoonLocalizedFaultFinder.found = null;
		}

		return result;
	}

}
