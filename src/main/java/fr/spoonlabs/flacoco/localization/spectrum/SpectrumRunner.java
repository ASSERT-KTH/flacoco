package fr.spoonlabs.flacoco.localization.spectrum;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.CoverageMatrix;
import fr.spoonlabs.flacoco.core.coverage.CoverageRunner;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.TestDetector;
import fr.spoonlabs.flacoco.localization.FaultLocalizationRunner;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class SpectrumRunner implements FaultLocalizationRunner {

	private Logger logger = Logger.getLogger(SpectrumRunner.class);
	private FlacocoConfig config = FlacocoConfig.getInstance();

	@Override
	public Map<String, Double> run() {
		CoverageMatrix coverageMatrix = computeCoverageMatrix();
		SpectrumSuspiciousComputation flcalc = new SpectrumSuspiciousComputation();
		return flcalc.calculateSuspicious(coverageMatrix, this.config.getSpectrumFormula().getFormula());
	}

	private CoverageMatrix computeCoverageMatrix() {
		this.logger.debug("Running spectrum-based fault localization...");
		this.logger.debug(this.config);

		// Get the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.findTests();

		CoverageRunner detector = new CoverageRunner();

		return detector.getCoverageMatrix(tests);
	}
}