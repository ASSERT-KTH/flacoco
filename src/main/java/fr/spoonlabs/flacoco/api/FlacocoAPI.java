package fr.spoonlabs.flacoco.api;

import spoon.reflect.code.CtStatement;

import java.util.Map;

public interface FlacocoAPI {

	Map<String, Double> runDefault();

	Map<CtStatement, Double> runSpoon();

}
