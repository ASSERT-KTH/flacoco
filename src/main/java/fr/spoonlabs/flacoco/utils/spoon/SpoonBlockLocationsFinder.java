package fr.spoonlabs.flacoco.utils.spoon;

import org.apache.log4j.Logger;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtType;

import java.util.List;

public class SpoonBlockLocationsFinder extends AbstractProcessor<CtType<?>> {

    private static final Logger logger = Logger.getLogger(SpoonBlockLocationsFinder.class);

    public static String fullyQualifiedClassName;
    public static int lineNumber;

    public static CtBlock<?> found;

    @Override
    public boolean isToBeProcessed(CtType<?> candidate) {
        return candidate.getQualifiedName().equals(fullyQualifiedClassName);
    }

    @Override
    public void process(CtType<?> ctType) {
        List<CtBlock<?>> result = ctType.filterChildren(new SpoonBlockFilter(lineNumber)).list();

        CtBlock<?> bestFit = null;
        SourcePosition curPos = null;
        for (CtBlock<?> ctBlock : result) {
            SourcePosition pos = ctBlock.getPosition();
            if (bestFit == null || curPos == null) {
                bestFit = ctBlock;
                curPos = pos;
            } else {
                int curStart = curPos.getLine();
                int start = pos.getLine();
                int curEnd = curPos.getEndLine();
                int end = pos.getEndLine();
                if (curStart < start) {
                    // In this case, we always want the block that begins closest to the identified line
                    bestFit = ctBlock;
                    curPos = pos;
                } else if (curStart == lineNumber && curEnd == lineNumber &&
                        start == lineNumber && end == lineNumber) {
                    // They are both in the same line, keep the top-most
                    if (curPos.getSourceStart() >= pos.getSourceStart() &&
                            pos.getSourceEnd() >= curPos.getSourceEnd() &&
                            curPos.getSourceStart() + curPos.getSourceEnd() != pos.getSourceStart() + pos.getSourceEnd()) {
                        bestFit = ctBlock;
                        curPos = pos;
                    }
                } else if (end < curEnd) {
                    // New one is tighter
                    bestFit = ctBlock;
                    curPos = pos;
                }
            }
        }

        found = bestFit;
    }
}