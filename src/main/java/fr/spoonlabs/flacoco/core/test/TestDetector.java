package fr.spoonlabs.flacoco.core.test;

import eu.stamp_project.testrunner.test_framework.TestFramework;
import fr.spoonlabs.flacoco.api.Flacoco;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import org.apache.log4j.Logger;
import spoon.Launcher;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matias Martinez
 */
public class TestDetector {

	private Logger logger = Logger.getLogger(Flacoco.class);
	private FlacocoConfig config = FlacocoConfig.getInstance();

	public List<TestInformation> findTests() {
		// Create Spoon model to retrieve information about the tests
		Launcher laucher = new Launcher();
		laucher.addInputResource(this.config.getProjectPath() + File.separator + "src/main");
		laucher.addInputResource(this.config.getProjectPath() + File.separator + "src/test");
		laucher.buildModel();

		// Init test framework
		TestFramework.init(laucher.getFactory());

		List<TestInformation> tuples = new ArrayList();

		List<CtType<?>> modelclassesOfTest = TestFramework.getAllTestClasses();
		for (CtType<?> type : modelclassesOfTest) {

			if (type.isAbstract()) {
				continue;
			}

			List<CtMethod<?>> methodsTest = TestFramework.getAllTest(type);

			TestInformation tuple = new TestInformation(type, methodsTest);

			tuples.add(tuple);
		}

		return tuples;
	}

}