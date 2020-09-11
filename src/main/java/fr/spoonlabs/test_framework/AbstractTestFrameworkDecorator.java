package fr.spoonlabs.test_framework;

import fr.spoonlabs.test_framework.implementations.NoneTestFrameworkSupport;
import spoon.reflect.declaration.CtMethod;

/**
 * created by Benjamin DANGLOT benjamin.danglot@inria.fr on 24/02/19
 *
 * Decorator design pattern around TestFrameworkSupport
 *
 */
public abstract class AbstractTestFrameworkDecorator extends AbstractTestFramework {

	private TestFrameworkSupport innerTestFramework;

	public AbstractTestFrameworkDecorator(String qualifiedNameOfAssertClass) {
		super(qualifiedNameOfAssertClass);
		this.innerTestFramework = new NoneTestFrameworkSupport();
	}

	public AbstractTestFrameworkDecorator(String qualifiedNameOfAssertClass, TestFrameworkSupport innerTestFramework) {
		super(qualifiedNameOfAssertClass);
		this.innerTestFramework = innerTestFramework;
	}

	public void setInnerTestFramework(TestFrameworkSupport innerTestFramework) {
		this.innerTestFramework = innerTestFramework;
	}

	@Override
	public boolean isTest(CtMethod<?> candidate) {
		return this.innerTestFramework.isTest(candidate);
	}
}
