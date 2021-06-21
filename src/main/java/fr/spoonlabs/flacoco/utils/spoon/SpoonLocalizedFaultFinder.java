package fr.spoonlabs.flacoco.utils.spoon;

import org.apache.log4j.Logger;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtType;

import java.util.List;

/**
 * Processor to find the best-fit statement according to a lineNumber and a fully qualified class name
 *
 * @author andre15silva
 */
public class SpoonLocalizedFaultFinder extends AbstractProcessor<CtType<?>> {

	private static final Logger logger = Logger.getLogger(SpoonLocalizedFaultFinder.class);

	public static String fullyQualifiedClassName;
	public static int lineNumber;

	public static CtStatement found;

	@Override
	public boolean isToBeProcessed(CtType<?> candidate) {
		return candidate.getQualifiedName().equals(fullyQualifiedClassName);
	}

	@Override
	public void process(CtType<?> ctType) {
		List<CtStatement> result = ctType.filterChildren(new SpoonLineFilter(lineNumber)).list();
		found = filterResult(result);
	}

	/**
	 * Given a list of CtStatement, returns the best fit, i.e.:
	 * - If possible, the top-most CtStatement whose position is just the given lineNumber
	 * - If not, the CtStatement whose position includes the given lineNumber and amplitude is smallest
	 *
	 * @param list List of CtStatement to filter
	 * @return The best-fit CtStatement
	 */
	private CtStatement filterResult(List<CtStatement> list) {
		CtStatement bestFit = null;
		SourcePosition curPos = null;

		for (CtStatement ctStatement : list) {
			SourcePosition pos = ctStatement.getPosition();
			if (bestFit == null || curPos == null) {
				bestFit = ctStatement;
				curPos = pos;
			} else {
				int curStart = curPos.getLine();
				int start = pos.getLine();
				int curEnd = curPos.getEndLine();
				int end = pos.getEndLine();
				if (curStart < start) {
					// In this case, we always want the statement that begins closest to the identified line
					bestFit = ctStatement;
					curPos = pos;
				} else if (curStart == lineNumber && curEnd == lineNumber &&
					start == lineNumber && end == lineNumber) {
					// They are both in the same line, keep the top-most
					if (curPos.getSourceStart() >= pos.getSourceStart() &&
					    pos.getSourceEnd() >= curPos.getSourceEnd() &&
						curPos.getSourceStart() + curPos.getSourceEnd() != pos.getSourceStart() + pos.getSourceEnd()) {
						bestFit = ctStatement;
						curPos = pos;
					}
				} else if (end < curEnd) {
					// New one is tighter
					bestFit = ctStatement;
					curPos = pos;
				}
			}
		}

		return bestFit;
	}

}
