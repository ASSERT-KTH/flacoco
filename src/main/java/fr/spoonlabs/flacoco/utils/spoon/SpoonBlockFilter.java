package fr.spoonlabs.flacoco.utils.spoon;

import spoon.reflect.code.CtBlock;
import spoon.reflect.visitor.Filter;

/**
 * Filters CtBlocks according to a given source code line number.
 */
public class SpoonBlockFilter implements Filter<CtBlock<?>> {

    private int lineNumber;

    public SpoonBlockFilter(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * @param ctBlock The CtBlock to be analyzed
     * @return true if the line number is comprehended in the ctBlock, false otherwise
     */
    @Override
    public boolean matches(CtBlock<?> ctBlock) {
        return ctBlock.getPosition() != null && ctBlock.getPosition().isValidPosition() && lineNumber >= ctBlock.getPosition().getLine() && lineNumber <= ctBlock.getPosition().getEndLine();
    }
}