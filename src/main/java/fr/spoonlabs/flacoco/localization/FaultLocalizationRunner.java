package fr.spoonlabs.flacoco.localization;

import spoon.reflect.declaration.CtElement;

import java.util.Map;

public interface FaultLocalizationRunner {

	public Map<String, Double> runDefault();

	public Map<CtElement, Double> runSpoon();

}