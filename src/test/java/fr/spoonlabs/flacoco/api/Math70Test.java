package fr.spoonlabs.flacoco.api;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * This test class tests the execution of Flacoco on Math70
 */
public class Math70Test {

    @Rule
    public TemporaryFolder workspaceDir = new TemporaryFolder();

    @Before
    public void setUp() {
        LogManager.getRootLogger().setLevel(Level.DEBUG);

        FlacocoConfig config = FlacocoConfig.getInstance();
        config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
        config.setTestRunnerVerbose(true);
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
}
