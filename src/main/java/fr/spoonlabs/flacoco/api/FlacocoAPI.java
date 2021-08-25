package fr.spoonlabs.flacoco.api;

import fr.spoonlabs.flacoco.api.result.FlacocoResult;
import fr.spoonlabs.flacoco.api.result.Suspiciousness;
import spoon.reflect.code.CtStatement;

import java.util.Map;

public interface FlacocoAPI {

	FlacocoResult run();

}
