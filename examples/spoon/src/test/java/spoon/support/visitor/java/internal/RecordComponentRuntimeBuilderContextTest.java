package spoon.support.visitor.java.internal;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 25/11/2021
 */
public class RecordComponentRuntimeBuilderContextTest {

    @Test
    public void test() {
        final RecordComponentRuntimeBuilderContext recordComponentRuntimeBuilderContext = new RecordComponentRuntimeBuilderContext();
        assertTrue(recordComponentRuntimeBuilderContext instanceof AbstractRuntimeBuilderContext);
    }
}
