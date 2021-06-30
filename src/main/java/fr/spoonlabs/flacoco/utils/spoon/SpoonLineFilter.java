package fr.spoonlabs.flacoco.utils.spoon;

import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.visitor.Filter;

/**
 * Filters CtStatements that have the given lineNumber in its position
 *
 * @author andre15silva
 */
public class SpoonLineFilter implements Filter<CtStatement> {

	private int lineNumber;

	public SpoonLineFilter(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**s
	 * @param ctStatement The CtStatement to be analyzed
	 * @return True if CtStatements position contains the given lineNumber
	 */
	@Override
	public boolean matches(CtStatement ctStatement) {
		SourcePosition pos = ctStatement.getPosition();
		return pos != null && pos.isValidPosition() &&
				pos.getLine() <= this.lineNumber && this.lineNumber <= pos.getEndLine();
	}

}
