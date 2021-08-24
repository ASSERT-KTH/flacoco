package fr.spoonlabs.flacoco.core.coverage.framework;

import eu.stamp_project.testrunner.EntryPoint;
import eu.stamp_project.testrunner.listener.CoveredTestResultPerTestMethod;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.method.TestMethod;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeoutException;

public class JUnit4Strategy extends TestFrameworkStrategy {

	private static final Logger logger = Logger.getLogger(JUnit4Strategy.class);

	private static JUnit4Strategy instance;

	private JUnit4Strategy() {

	}

	public static JUnit4Strategy getInstance() {
		if (instance == null) {
			instance = new JUnit4Strategy();
		}
		return instance;
	}

	@Override
	public CoveredTestResultPerTestMethod execute(TestContext testContext) throws TimeoutException {
		logger.debug("Running " + testContext);
		this.setupTestRunnerEntryPoint();

		return EntryPoint.runOnlineCoveredTestResultPerTestMethods(
				this.computeClasspath(),
				FlacocoConfig.getInstance().getBinJavaDir(),
				FlacocoConfig.getInstance().getBinTestDir(),
				testContext.getTestMethods().stream().map(TestMethod::getFullyQualifiedClassName).distinct().toArray(String[]::new),
				new String[0]
		);
	}

}
