package fr.spoonlabs.flacoco.localization.spectrum;

import eu.stamp_project.testrunner.listener.impl.CoverageCollectorDetailed;
import eu.stamp_project.testrunner.runner.coverage.JUnit4JacocoRunner;
import eu.stamp_project.testrunner.runner.coverage.JacocoRunner;
import fr.spoonlabs.flacoco.api.Flacoco;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.CoverageRunner;
import fr.spoonlabs.flacoco.core.coverage.MatrixCoverage;
import fr.spoonlabs.flacoco.core.test.TestDetector;
import fr.spoonlabs.flacoco.core.test.TestInformation;
import fr.spoonlabs.flacoco.localization.FaultLocalizationRunner;
import org.apache.log4j.Logger;
import spoon.Launcher;
import spoon.reflect.declaration.CtElement;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SpectrumRunner implements FaultLocalizationRunner {

	private Logger logger = Logger.getLogger(Flacoco.class);
	private FlacocoConfig config = FlacocoConfig.getInstance();

	@Override
	public Map<String, Double> runDefault() {
		MatrixCoverage matrixCoverage = computeCoverageMatrix();
		SpectrumSuspicousComputation flcalc = new SpectrumSuspicousComputation();
		return flcalc.calculateSuspicious(matrixCoverage, this.config.getSpectrumFormula().getFormula());
	}

	@Override
	public Map<CtElement, Double> runSpoon() {
		MatrixCoverage matrixCoverage = computeCoverageMatrix();

		return null;
	}

	private MatrixCoverage computeCoverageMatrix() {
		this.logger.debug("Running spectrum-based fault localization...");
		this.logger.debug(this.config);

		// Get the tests
		TestDetector testDetector = new TestDetector();
		List<TestInformation> tests = testDetector.findTests();

		CoverageRunner detector = new CoverageRunner();

		// Compute target path
		String pathToClasses = this.config.getProjectPath() + File.separator + "target/classes/";
		String pathToTestClasses = this.config.getProjectPath() + File.separator + "target/test-classes/";

		JacocoRunner runner = new JUnit4JacocoRunner(pathToClasses, pathToTestClasses, new CoverageCollectorDetailed());

		return detector.getCoverageMatrix(runner, this.config.getClasspath(), pathToClasses, pathToTestClasses, tests);
	}
}