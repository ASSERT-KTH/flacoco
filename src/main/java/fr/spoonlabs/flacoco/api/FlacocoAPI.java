package fr.spoonlabs.flacoco.api;

import spoon.reflect.code.CtStatement;

import java.util.Map;

public interface FlacocoAPI {

	Map<String, Suspiciousness> runDefault();

	Map<CtStatement, Suspiciousness> runSpoon();

}
