package fr.spoonlabs.flacoco.api;

import fr.spoonlabs.flacoco.core.test.method.TestMethod;

import java.util.List;

public class Suspiciousness implements Comparable<Suspiciousness> {

	private Double score;

	private List<TestMethod> passingTestCases;

	private List<TestMethod> failingTestCases;

	public Suspiciousness(Double score, List<TestMethod> passingTestCases, List<TestMethod> failingTestCases) {
		this.score = score;
		this.passingTestCases = passingTestCases;
		this.failingTestCases = failingTestCases;
	}

	public Double getScore() {
		return score;
	}

	public List<TestMethod> getPassingTestCases() {
		return passingTestCases;
	}

	public List<TestMethod> getFailingTestCases() {
		return failingTestCases;
	}

	@Override
	public int compareTo(Suspiciousness suspiciousness) {
		return Double.compare(this.score, suspiciousness.score);
	}

	@Override
	public String toString() {
		return "Suspiciousness{" +
				"score=" + score +
				", passingTestCases=" + passingTestCases +
				", failingTestCases=" + failingTestCases +
				'}';
	}
}
