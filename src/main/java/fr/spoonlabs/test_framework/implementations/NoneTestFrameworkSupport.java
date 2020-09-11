package fr.spoonlabs.test_framework.implementations;

import fr.spoonlabs.test_framework.TestFrameworkSupport;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

/**
 * created by Benjamin DANGLOT benjamin.danglot@inria.fr on 24/02/19
 */
public class NoneTestFrameworkSupport implements TestFrameworkSupport {

	@Override
	public boolean isAssert(CtInvocation<?> invocation) {
		return false;
	}

	@Override
	public boolean isAssert(CtStatement candidate) {
		return false;
	}

	@Override
	public boolean isInAssert(CtElement candidate) {
		return false;
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
