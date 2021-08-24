package fr.spoonlabs.flacoco.core.config;

import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Config manager for Flacoco.
 *
 * @author andre15silva
 */
public class FlacocoConfig {

	public enum FaultLocalizationFamily {
		SPECTRUM_BASED,
	}

	public enum TestDetectionStrategy {
		TEST_RUNNER,
		CLASSLOADER
	}

	private static FlacocoConfig instance;

	private String workspace;
	private String projectPath;
	private List<String> srcJavaDir;
	private List<String> srcTestDir;
	private List<String> binJavaDir;
	private List<String> binTestDir;
	private String classpath;
	private String customJUnitClasspath;
	private String customJacocoClasspath;
	private String mavenHome;
	private boolean coverTests;
	private boolean testRunnerVerbose;
	private int testRunnerTimeoutInMs;
	private String testRunnerJVMArgs;
	private double threshold;
	private boolean includeZeros;
	private int complianceLevel;

	private TestDetectionStrategy testDetectionStrategy;
	private Set<String> ignoredTests;
	private Set<String> jUnit4Tests;
	private Set<String> jUnit5Tests;
	private Set<String> jacocoIncludes;
	private Set<String> jacocoExcludes;

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
		this.classpath = "";
		this.srcJavaDir = new ArrayList<>(); // will default to {this.projectPath}/src/main/java if not set; see getter
		this.srcTestDir = new ArrayList<>(); // will default to {this.projectPath}/src/test if not set; see getter
		this.binJavaDir = new ArrayList<>(); // will default to {this.projectPath}/target/classes if not set; see getter
		this.binTestDir = new ArrayList<>(); // will default to {this.projectPath}/target/test-classes if not set; see getter
		this.customJUnitClasspath = null;
		this.customJacocoClasspath = null;
		this.mavenHome = System.getProperty("user.home") + "/.m2/repository/";
		this.coverTests = false;
		this.testRunnerVerbose = false;
		this.testRunnerTimeoutInMs = 1000000;
		this.testRunnerJVMArgs = null;
		this.threshold = 0.0;
		this.includeZeros = false;
		this.complianceLevel = 8;

		this.testDetectionStrategy = TestDetectionStrategy.CLASSLOADER;
		this.ignoredTests = new HashSet<>();
		this.jUnit4Tests = new HashSet<>();
		this.jUnit5Tests = new HashSet<>();
		this.jacocoIncludes = new HashSet<>();
		this.jacocoExcludes = new HashSet<>();

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
		this.projectPath = new File(projectPath).getAbsolutePath();
	}

	public String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public List<String> getSrcJavaDir() {
		if (srcJavaDir.isEmpty()) {
			return Collections.singletonList(
					new File(getProjectPath() + File.separatorChar + "src/main/java/")
							.getAbsolutePath()
			);
		} else {
			return srcJavaDir;
		}
	}

	public void setSrcJavaDir(List<String> srcJavaDir) {
		this.srcJavaDir = listOfPathsToListOfAbsolutePaths(srcJavaDir);
	}

	public List<String> getSrcTestDir() {
		if (srcTestDir.isEmpty()) {
			return Collections.singletonList(
					new File(getProjectPath() + File.separatorChar + "src/test/")
							.getAbsolutePath()
			);
		} else {
			return srcTestDir;
		}
	}

	public void setSrcTestDir(List<String> srcTestDir) {
		this.srcTestDir = listOfPathsToListOfAbsolutePaths(srcTestDir);
	}

	public List<String> getBinJavaDir() {
		if (binJavaDir.isEmpty()) {
			return Collections.singletonList(
					new File(getProjectPath() + File.separatorChar + "target/classes/")
							.getAbsolutePath()
			);
		} else {
			return binJavaDir;
		}
	}

	public void setBinJavaDir(List<String> binJavaDir) {
		this.binJavaDir = listOfPathsToListOfAbsolutePaths(binJavaDir);
	}

	public List<String> getBinTestDir() {
		if (binTestDir.isEmpty()) {
			return Collections.singletonList(
					new File(getProjectPath() + File.separatorChar + "target/test-classes/")
							.getAbsolutePath()
			);
		} else {
			return binTestDir;
		}
	}

	public void setBinTestDir(List<String> binTestDir) {
		this.binTestDir = listOfPathsToListOfAbsolutePaths(binTestDir);
	}

	public String getCustomJUnitClasspath() {
		return customJUnitClasspath;
	}

	public void setCustomJUnitClasspath(String customJUnitClasspath) {
		this.customJUnitClasspath = customJUnitClasspath;
	}

	public String getCustomJacocoClasspath() {
		return customJacocoClasspath;
	}

	public void setCustomJacocoClasspath(String customJacocoClasspath) {
		this.customJacocoClasspath = customJacocoClasspath;
	}

	public String getMavenHome() {
		return mavenHome;
	}

	public void setMavenHome(String mavenHome) {
		this.mavenHome = new File(mavenHome).getAbsolutePath();
	}

	public boolean isCoverTests() {
		return coverTests;
	}

	public void setCoverTests(boolean coverTests) {
		this.coverTests = coverTests;
	}

	public boolean isTestRunnerVerbose() {
		return testRunnerVerbose;
	}

	public void setTestRunnerVerbose(boolean testRunnerVerbose) {
		this.testRunnerVerbose = testRunnerVerbose;
	}

	public int getTestRunnerTimeoutInMs() {
		return testRunnerTimeoutInMs;
	}

	public void setTestRunnerTimeoutInMs(int testRunnerTimeoutInMs) {
		this.testRunnerTimeoutInMs = testRunnerTimeoutInMs;
	}

	public String getTestRunnerJVMArgs() {
		return testRunnerJVMArgs;
	}

	public void setTestRunnerJVMArgs(String testRunnerJVMArgs) {
		this.testRunnerJVMArgs = testRunnerJVMArgs;
	}

	public TestDetectionStrategy getTestDetectionStrategy() {
		return testDetectionStrategy;
	}

	public void setTestDetectionStrategy(TestDetectionStrategy testDetectionStrategy) {
		this.testDetectionStrategy = testDetectionStrategy;
	}

	public Set<String> getIgnoredTests() {
		return ignoredTests;
	}

	public void setIgnoredTests(Set<String> ignoredTests) {
		this.ignoredTests = ignoredTests;
	}

	public Set<String> getjUnit4Tests() {
		return jUnit4Tests;
	}

	public void setjUnit4Tests(Set<String> jUnit4Tests) {
		this.jUnit4Tests = jUnit4Tests;
	}

	public Set<String> getjUnit5Tests() {
		return jUnit5Tests;
	}

	public void setjUnit5Tests(Set<String> jUnit5Tests) {
		this.jUnit5Tests = jUnit5Tests;
	}

	public Set<String> getJacocoIncludes() {
		return jacocoIncludes;
	}

	public void setJacocoIncludes(Set<String> jacocoIncludes) {
		this.jacocoIncludes = jacocoIncludes;
	}

	public Set<String> getJacocoExcludes() {
		return jacocoExcludes;
	}

	public void setJacocoExcludes(Set<String> jacocoExcludes) {
		this.jacocoExcludes = jacocoExcludes;
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

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public boolean isIncludeZeros() {
		return includeZeros;
	}

	public void setIncludeZeros(boolean includeZeros) {
		this.includeZeros = includeZeros;
	}

	public int getComplianceLevel() {
		return complianceLevel;
	}

	public void setComplianceLevel(int complianceLevel) {
		this.complianceLevel = complianceLevel;
	}

	@Override
	public String toString() {
		return "FlacocoConfig{" +
				"workspace='" + workspace + '\'' +
				", projectPath='" + projectPath + '\'' +
				", srcJavaDir=" + getSrcJavaDir() +
				", srcTestDir=" + getSrcTestDir() +
				", binJavaDir=" + getBinJavaDir() +
				", binTestDir=" + getBinTestDir() +
				", classpath='" + classpath + '\'' +
				", customJUnitClasspath='" + customJUnitClasspath + '\'' +
				", customJacocoClasspath='" + customJacocoClasspath + '\'' +
				", mavenHome='" + mavenHome + '\'' +
				", coverTests=" + coverTests +
				", testRunnerVerbose=" + testRunnerVerbose +
				", testRunnerTimeoutInMs=" + testRunnerTimeoutInMs +
				", testRunnerJVMArgs='" + testRunnerJVMArgs + '\'' +
				", threshold=" + threshold +
				", includeZeros=" + includeZeros +
				", complianceLevel=" + complianceLevel +
				", testDetectionStrategy=" + testDetectionStrategy +
				", ignoredTests=" + ignoredTests +
				", jUnit4Tests=" + jUnit4Tests +
				", jUnit5Tests=" + jUnit5Tests +
				", jacocoIncludes=" + jacocoIncludes +
				", jacocoExcludes=" + jacocoExcludes +
				", family=" + family +
				", spectrumFormula=" + spectrumFormula +
				'}';
	}

	// For test purposes only
	public static void deleteInstance() {
		instance = null;
	}

	private static List<String> listOfPathsToListOfAbsolutePaths(List<String> paths) {
		return paths.stream().map(x -> new File(x).getAbsolutePath()).collect(Collectors.toList());
	}
}
