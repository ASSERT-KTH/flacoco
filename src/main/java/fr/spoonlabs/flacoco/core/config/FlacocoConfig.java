package fr.spoonlabs.flacoco.core.config;

import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;

import java.io.File;

/**
 * Config manager for Flacoco.
 *
 * @author andre15silva
 */
public class FlacocoConfig {

	public enum FaultLocalizationFamily {
		SPECTRUM_BASED,
	}

	public enum TestFramework {
		JUNIT4,
		JUNIT5
	}

	private static FlacocoConfig instance;

	private String workspace;
	private String projectPath;
	private String classpath;
	private TestFramework testFramework;
	private boolean coverTests;

	private FaultLocalizationFamily family;
	//------Options for spectrum-based fault localization------
	private SpectrumFormula spectrumFormula;

	private FlacocoConfig() {
		initDefaults();
	}

	public static FlacocoConfig getInstance() {
		if (instance == null) {
			instance = new FlacocoConfig();
		}
		return instance;
	}

	private void initDefaults() {
		this.workspace = new File("./").getAbsolutePath();
		this.projectPath = new File("./").getAbsolutePath();
		this.classpath = new File("./").getAbsolutePath();
		this.testFramework = TestFramework.JUNIT4;
		this.coverTests = false;

		this.family = FaultLocalizationFamily.SPECTRUM_BASED;
		this.spectrumFormula = SpectrumFormula.OCHIAI;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public TestFramework getTestFramework() {
		return testFramework;
	}

	public void setTestFramework(TestFramework testFramework) {
		this.testFramework = testFramework;
	}

	public boolean isCoverTests() {
		return coverTests;
	}

	public void setCoverTests(boolean coverTests) {
		this.coverTests = coverTests;
	}

	public FaultLocalizationFamily getFamily() {
		return family;
	}

	public void setFamily(FaultLocalizationFamily family) {
		this.family = family;
	}

	public SpectrumFormula getSpectrumFormula() {
		return spectrumFormula;
	}

	public void setSpectrumFormula(SpectrumFormula spectrumFormula) {
		this.spectrumFormula = spectrumFormula;
	}

	@Override
	public String toString() {
		return "FlacocoConfig{" +
				"workspace='" + workspace + '\'' +
				", projectPath='" + projectPath + '\'' +
				", classpath='" + classpath + '\'' +
				", testFramework=" + testFramework +
				", coverTests=" + coverTests +
				", family=" + family +
				", spectrumFormula=" + spectrumFormula +
				'}';
	}

	// For test purposes only
	public static void deleteInstance() {
		instance = null;
	}
}
