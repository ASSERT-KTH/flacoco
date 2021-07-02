package fr.spoonlabs.flacoco.core.test;

import eu.stamp_project.testrunner.test_framework.TestFramework;
import fr.spoonlabs.flacoco.api.Flacoco;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit4Strategy;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit5Strategy;
import org.apache.log4j.Logger;
import spoon.Launcher;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Matias Martinez
 */
public class TestDetector {

	private Logger logger = Logger.getLogger(Flacoco.class);
	private FlacocoConfig config = FlacocoConfig.getInstance();

	public List<TestInformation> findTests() {
		// Create Spoon model to retrieve information about the tests
		Launcher laucher = new Launcher();
		laucher.addInputResource(new File(this.config.getProjectPath() + File.separator + "src/main").getAbsolutePath());
		laucher.addInputResource(new File(this.config.getProjectPath() + File.separator + "src/test").getAbsolutePath());
		laucher.buildModel();

		// Init test framework
		TestFramework.init(laucher.getFactory());

		List<TestInformation> tuples = new ArrayList<>();

		for (CtType<?> type : TestFramework.getAllTestClasses()) {

			if (type.isAbstract()) {
				continue;
			}

			List<CtMethod<?>> jUnit4Tests = TestFramework.getAllTest(type).stream()
					.filter(TestFramework::isJUnit4).collect(Collectors.toList());
			List<CtMethod<?>> jUnit5Tests = TestFramework.getAllTest(type).stream()
					.filter(TestFramework::isJUnit5).collect(Collectors.toList());

			if (!jUnit4Tests.isEmpty())
				tuples.add(new TestInformation(type, jUnit4Tests, new JUnit4Strategy()));
			if (!jUnit5Tests.isEmpty())
				tuples.add(new TestInformation(type, jUnit5Tests, new JUnit5Strategy()));
		}

		return tuples;
	}

}