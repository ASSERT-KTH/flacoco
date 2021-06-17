package fr.spoonlabs.flacoco.api;

import spoon.reflect.declaration.CtElement;

import java.util.Map;

public interface FlacocoAPI {

	Map<String, Double> runDefault();

	Map<CtElement, Double> runSpoon();

}
