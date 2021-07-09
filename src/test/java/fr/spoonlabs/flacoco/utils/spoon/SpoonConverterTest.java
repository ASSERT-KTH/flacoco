package fr.spoonlabs.flacoco.utils.spoon;

import fr.spoonlabs.flacoco.api.Suspiciousness;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import spoon.reflect.code.CtStatement;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpoonConverterTest {

	@Rule
	public TemporaryFolder workspaceDir = new TemporaryFolder();

	@Before
	public void setUp() {
		LogManager.getRootLogger().setLevel(Level.DEBUG);

		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
	}

	@After
	public void tearDown() {
		FlacocoConfig.deleteInstance();
	}

	@Test
	@Ignore
	public void testConvertSpoonExample() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL3/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Init mapping
		Map<String, Suspiciousness> map = new HashMap<>();
		map.put("fr/spoonlabs/FLtest1/Calculator@-@5", new Suspiciousness(1.0, null, null));
		map.put("fr/spoonlabs/FLtest1/Calculator@-@6", new Suspiciousness(0.95, null, null));
		map.put("fr/spoonlabs/FLtest1/Calculator@-@12", new Suspiciousness(0.90, null, null));
		map.put("fr/spoonlabs/FLtest1/Calculator@-@8", new Suspiciousness(0.85, null, null));
		map.put("fr/spoonlabs/FLtest1/Calculator@-@14", new Suspiciousness(0.80, null, null));
		map.put("fr/spoonlabs/FLtest1/Calculator@-@15", new Suspiciousness(0.75, null, null));
		map.put("fr/spoonlabs/FLtest1/Calculator@-@26", new Suspiciousness(0.70, null, null));
		map.put("fr/spoonlabs/FLtest1/Calculator@-@21", new Suspiciousness(0.65, null, null));

		Map<CtStatement, Suspiciousness> converted = SpoonConverter.convert(map);

		for (CtStatement statement : converted.keySet()) {
			System.out.println(statement + " : " + converted.get(statement));
		}

		// Even though we had 8 keys, we now have 7 as two of them map to the same CtStatement
		assertEquals(7, converted.size());

		for (CtStatement statement : converted.keySet()) {
			switch (statement.getPosition().getLine()) {
				case 12:
					assertEquals("op.equals(\"+\")", statement.toString());
					break;
				case 14:
					assertEquals("op.equals(\"-\")", statement.toString());
					break;
				case 15:
					assertEquals("return op1 - op2", statement.toString());
					break;
				case 26:
					assertEquals("throw new java.lang.UnsupportedOperationException(op)", statement.toString());
					break;
				case 5:
					// This is the case where two keys get mapped to just one statement
					// In this case, it is an empty constructor
					assertEquals("{\n}", statement.toString());
					break;
				case 3:
					// Here we get the whole class as there is no CtStatement for the field init
					// Line 8 gets mapped to 3
					assertTrue(statement.toString().startsWith("public class Calculator {\n"));
					break;
				case 21:
					assertTrue(statement.toString().startsWith("java.lang.System.out.println"));
					break;
			}
		}
	}

}
