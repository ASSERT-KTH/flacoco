package fr.spoonlabs.FLtest1;

public class Calculator {

	public Calculator() {
	}

	String op;

	public int calculate(String op, int op1, int op2) {

		if (op.equals("+")) {
			return op1 + op2;
		} else if (op.equals("-")) {
			return op1 - op2;
		} else if (op.equals("*")) {
			return op1 * op2;
		} else if (op.equals("/")) {
			return op1 / op2;
		} else {
			System.out.println("Other cases:" + op.toString());
			if (this.op.equals("%")) { //NPE
				return op1 / op2;
			}
		}
		throw new UnsupportedOperationException(op);
	}
}
