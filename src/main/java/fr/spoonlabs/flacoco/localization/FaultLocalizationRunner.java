package fr.spoonlabs.flacoco.localization;

import fr.spoonlabs.flacoco.api.Suspiciousness;

import java.util.Map;

public interface FaultLocalizationRunner {

	Map<String, Suspiciousness> run();

}