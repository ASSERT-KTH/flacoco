package fr.spoonlabs.test_framework.implementations;

import fr.spoonlabs.test_framework.AbstractTestFramework;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

/**
 * created by Benjamin DANGLOT benjamin.danglot@inria.fr on 09/11/18
 */
public class AssertJTestFramework extends AbstractTestFramework {

	public AssertJTestFramework() {
		super("org.assertj.core.api.*");
	}

	@Override
	public boolean isTest(CtMethod<?> candidate) {
		return false;
	}

	@Override
	public boolean isIgnored(CtElement candidate) {
		return false;
	}
}
