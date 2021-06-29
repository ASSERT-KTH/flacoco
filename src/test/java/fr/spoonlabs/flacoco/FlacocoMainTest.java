package fr.spoonlabs.flacoco;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;

@SuppressWarnings("unused")
class FlacocoMainTest {

	@Test
	void testMain() throws IOException {

		// It's a smoke test

		FlacocoMain.main(new String[] { "--projectpath", "examples/exampleFL1/FLtest1", "--formula",
				SpectrumFormula.OCHIAI.name(), "--coverTest", "--framework", FlacocoConfig.TestFramework.JUNIT4.name()

		});
	}

}
