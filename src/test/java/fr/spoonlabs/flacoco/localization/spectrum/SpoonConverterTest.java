package fr.spoonlabs.flacoco.localization.spectrum;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.utils.spoon.SpoonConverter;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import spoon.reflect.code.CtStatement;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpoonConverterTest {

	@Before
	public void setUp() {
		LogManager.getRootLogger().setLevel(Level.DEBUG);

		FlacocoConfig config = FlacocoConfig.getInstance();
		String dep1 = new File("./examples/libs/junit-4.12.jar").getAbsolutePath();
		String dep2 = new File("./examples/libs/hamcrest-core-1.3.jar").getAbsolutePath();
		config.setClasspath(dep1 + File.separator + dep2);
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
		config.setProjectPath("./examples/exampleFL3/FLtest1");
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Init mapping
		Map<String, Double> map = new HashMap<>();
		map.put("fr/spoonlabs/FLtest1/Calculator@-@5", 1.0);
		map.put("fr/spoonlabs/FLtest1/Calculator@-@6", 0.95);
		map.put("fr/spoonlabs/FLtest1/Calculator@-@12", 0.90);
		map.put("fr/spoonlabs/FLtest1/Calculator@-@8", 0.85);
		map.put("fr/spoonlabs/FLtest1/Calculator@-@14", 0.80);
		map.put("fr/spoonlabs/FLtest1/Calculator@-@15", 0.75);
		map.put("fr/spoonlabs/FLtest1/Calculator@-@26", 0.70);
		map.put("fr/spoonlabs/FLtest1/Calculator@-@21", 0.65);

		Map<CtStatement, Double> converted = SpoonConverter.convert(map);

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
