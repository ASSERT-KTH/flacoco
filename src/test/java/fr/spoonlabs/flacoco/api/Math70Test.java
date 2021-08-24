package fr.spoonlabs.flacoco.api;

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
        // Run only on Java8
        Assume.assumeTrue(getJavaVersion() == 8);
        // FIXME: In CI, `testMath70` fails due to the test-runner args being too big (#57)
        Assume.assumeFalse(System.getProperty("os.name").startsWith("Windows"));

        LogManager.getRootLogger().setLevel(Level.INFO);
        FlacocoConfig config = FlacocoConfig.getInstance();
        config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
    }

    @After
    public void tearDown() {
        FlacocoConfig.deleteInstance();
    }

    @Test
    public void testMath70() {
        // Setup config
        FlacocoConfig config = FlacocoConfig.getInstance();
        config.setProjectPath(new File("./examples/math_70").getAbsolutePath());
        config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
        config.setSpectrumFormula(SpectrumFormula.OCHIAI);
        config.setThreshold(0.5);

        // Run Flacoco
        Flacoco flacoco = new Flacoco();

        // Run default mode
        Map<String, Suspiciousness> susp = flacoco.runDefault();

        for (String line : susp.keySet()) {
            System.out.println("" + line + " " + susp.get(line));
        }

        assertEquals(8, susp.size());

        assertEquals(1.0, susp.get("org/apache/commons/math/analysis/solvers/BisectionSolver@-@72").getScore(), 0);
        assertEquals(0.70, susp.get("org/apache/commons/math/analysis/solvers/BisectionSolver@-@66").getScore(), 0.01);
        assertEquals(0.5, susp.get("org/apache/commons/math/analysis/solvers/BisectionSolver@-@81").getScore(), 0);
        assertEquals(0.5, susp.get("org/apache/commons/math/analysis/solvers/BisectionSolver@-@80").getScore(), 0);
        assertEquals(0.5, susp.get("org/apache/commons/math/analysis/solvers/BisectionSolver@-@89").getScore(), 0);
        assertEquals(0.5, susp.get("org/apache/commons/math/analysis/solvers/BisectionSolver@-@88").getScore(), 0);
        assertEquals(0.5, susp.get("org/apache/commons/math/analysis/solvers/BisectionSolver@-@87").getScore(), 0);
        assertEquals(0.5, susp.get("org/apache/commons/math/analysis/solvers/BisectionSolver@-@87").getScore(), 0);
    }


    @Test
    public void testMath70TestDetection() {
        // Setup config
        FlacocoConfig config = FlacocoConfig.getInstance();
        config.setProjectPath(new File("./examples/math_70").getAbsolutePath());
        config.setComplianceLevel(4);

        // Find the tests
        TestDetector testDetector = new TestDetector();
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
