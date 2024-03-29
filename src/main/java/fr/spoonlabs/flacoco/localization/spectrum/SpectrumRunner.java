package fr.spoonlabs.flacoco.localization.spectrum;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fr.spoonlabs.flacoco.api.result.FlacocoResult;
import fr.spoonlabs.flacoco.api.result.Location;
import fr.spoonlabs.flacoco.api.result.Suspiciousness;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.CoverageMatrix;
import fr.spoonlabs.flacoco.core.coverage.CoverageRunner;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.TestDetector;
import fr.spoonlabs.flacoco.localization.FaultLocalizationRunner;
import fr.spoonlabs.flacoco.utils.spoon.SpoonConverter;

public class SpectrumRunner implements FaultLocalizationRunner {

	private Logger logger = Logger.getLogger(SpectrumRunner.class);

	private FlacocoConfig config;

	public SpectrumRunner(FlacocoConfig config) {
		this.config = config;
	}

	@Override
	public FlacocoResult run() {
		FlacocoResult result = new FlacocoResult();

		// Warn if system memory is lower than 4GiB
		long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean())
				.getTotalPhysicalMemorySize();
		if (memorySize < 4294967296L) {
			logger.warn("System memory is lower than 4GiB. "
					+ "Caution when running spectrum-based fault localization on large projects, as coverage computation require more than the available memory");
		}

		CoverageMatrix coverageMatrix = computeCoverageMatrix();
		result.setFailingTests(coverageMatrix.getFailingTestCases());
		result.setExecutedTests(coverageMatrix.getTests().keySet());

		SpectrumSuspiciousComputation ssc = new SpectrumSuspiciousComputation(config);
		Map<Location, Suspiciousness> defaultMapping = ssc.calculateSuspicious(coverageMatrix,
				this.config.getSpectrumFormula().getFormula());
		result.setDefaultSuspiciousnessMap(defaultMapping);

		if (config.isComputeSpoonResults()) {
			result = new SpoonConverter(config).convertResult(result);
		}

		return result;
	}

	private CoverageMatrix computeCoverageMatrix() {
		this.logger.debug("Running spectrum-based fault localization...");
		this.logger.debug(this.config);

		// Get the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> tests = testDetector.getTests();

		CoverageRunner detector = new CoverageRunner(config);

		return detector.getCoverageMatrix(tests);
	}
}