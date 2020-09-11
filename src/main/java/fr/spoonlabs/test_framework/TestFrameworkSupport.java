package fr.spoonlabs.test_framework;

import fr.spoonlabs.test_framework.assertions.IsAssertInvocationFilter;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

/**
 * created by Benjamin DANGLOT benjamin.danglot@inria.fr on 07/11/18
 *
 * Implementation of this interface should be stateless.
 *
 * If you implement a new TestFrameworkSupport, you should update the list of
 * frameworks of the {@link TestFramework}
 *
 */
public interface TestFrameworkSupport {

	/**
	 * @param invocation checkEnum if this CtInvocation is directly an assertion
	 *                   call or an invocation to a method that contains assertion
	 *                   calls.
	 * @return true if the candidate is an assertion or call a method that contains
	 *         assertions.
	 */
	public boolean isAssert(CtInvocation<?> invocation);

	/**
	 * This method is a proxy for
	 * {@link IsAssertInvocationFilter#isAssert(CtInvocation)} for CtStatement.
	 * 
	 * @param candidate checkEnum if this CtStatement is directly an assertion call
	 *                  or an invocation to a method that contains assertion calls.
	 * @return true if the candidate is an assertion or call a method that contains
	 *         assertions.
	 */
	public boolean isAssert(CtStatement candidate);

	/**
	 * Check that the given element in inside an assertion
	 * 
	 * @param candidate the element
	 * @return true if a parent is an invocation to an assertion
	 */
	public boolean isInAssert(CtElement candidate);

	/**
	 * @param candidate the potential test method
	 * @return true of the given candidate is a test method, false otherwise
	 */
	public boolean isTest(CtMethod<?> candidate);

	public boolean isIgnored(CtElement candidate);
}
