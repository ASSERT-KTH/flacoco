package fr.spoonlabs.flacoco.utils.spoon;

import spoon.reflect.code.CtBlock;
import spoon.reflect.visitor.Filter;

public class SpoonBlockFilter implements Filter<CtBlock<?>> {

    private int lineNumber;

    public SpoonBlockFilter(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean matches(CtBlock<?> ctBlock) {
        return lineNumber >= ctBlock.getPosition().getLine() && lineNumber <= ctBlock.getPosition().getEndLine();
    }
}