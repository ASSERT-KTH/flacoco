package fr.spoonlabs.flacoco.api;

import fr.spoonlabs.flacoco.api.result.FlacocoResult;
import fr.spoonlabs.flacoco.api.result.Location;
import fr.spoonlabs.flacoco.api.result.Suspiciousness;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit4Strategy;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.TestDetector;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;
import java.util.Map;

import static fr.spoonlabs.flacoco.TestUtils.getCompilerVersion;
import static fr.spoonlabs.flacoco.TestUtils.getJavaVersion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class tests the execution of Flacoco on Math70
 */
public class Math70Test {

    @Rule
    public TemporaryFolder workspaceDir = new TemporaryFolder();

    @Before
    public void setUp() {
        // Run only on target release >= 5
        Assume.assumeTrue(getCompilerVersion() >= 5);

        // Run only on Java8
        Assume.assumeTrue(getJavaVersion() == 8);
        // FIXME: In CI, `testMath70` fails due to the test-runner args being too big (#57)
        Assume.assumeFalse(System.getProperty("os.name").startsWith("Windows"));

        LogManager.getRootLogger().setLevel(Level.INFO);
    }

    @Test
    public void testMath70() {
        // Setup config
        FlacocoConfig config = new FlacocoConfig();
        config.setProjectPath(new File("./examples/math_70").getAbsolutePath());
        config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
        config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
        config.setSpectrumFormula(SpectrumFormula.OCHIAI);
        config.setThreshold(0.5);

        // Run Flacoco
        Flacoco flacoco = new Flacoco(config);

        // Run default mode
        FlacocoResult result = flacoco.run();

        for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
            System.out.println(entry);
        }

        Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
        assertEquals(8, susp.size());

        assertEquals(1.0, susp.get(new Location("org.apache.commons.math.analysis.solvers.BisectionSolver", 72)).getScore(), 0);
        assertEquals(0.70, susp.get(new Location("org.apache.commons.math.analysis.solvers.BisectionSolver", 66)).getScore(), 0.01);
        assertEquals(0.5, susp.get(new Location("org.apache.commons.math.analysis.solvers.BisectionSolver", 81)).getScore(), 0);
        assertEquals(0.5, susp.get(new Location("org.apache.commons.math.analysis.solvers.BisectionSolver", 80)).getScore(), 0);
        assertEquals(0.5, susp.get(new Location("org.apache.commons.math.analysis.solvers.BisectionSolver", 89)).getScore(), 0);
        assertEquals(0.5, susp.get(new Location("org.apache.commons.math.analysis.solvers.BisectionSolver", 88)).getScore(), 0);
        assertEquals(0.5, susp.get(new Location("org.apache.commons.math.analysis.solvers.BisectionSolver", 87)).getScore(), 0);
        assertEquals(0.5, susp.get(new Location("org.apache.commons.math.analysis.solvers.BisectionSolver", 87)).getScore(), 0);
    }


    @Test
    public void testMath70TestDetection() {
        // Setup config
        FlacocoConfig config = new FlacocoConfig();
        config.setProjectPath(new File("./examples/math_70").getAbsolutePath());
        config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
        config.setComplianceLevel(4);

        // Find the tests
        TestDetector testDetector = new TestDetector(config);
        List<TestContext> testContexts = testDetector.getTests();

        // Check that there is only one test context
        assertEquals(1, testContexts.size());
        // Check that there are 2181 test methods in the test context
        TestContext testContext = testContexts.get(0);
        assertEquals(2181, testContext.getTestMethods().size());
        // Check that the correct test framework is set
        assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
    }
}
