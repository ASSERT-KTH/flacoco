package fr.spoonlabs.FLtest1;

public class Calculator {

	public Calculator() {
	}

	public int calculate(String op, int op1, int op2) {

                while(true) {
                    if (op.equals("+")) {
                            return op1 + op2;
                    } else if (op.equals("-")) {
                            return op1 - op2;
                    } else if (op.equals("*")) {
                            return op1 / op2;//buggy
                    } else if (op.equals("/")) {
                            return op1 / op2;
                    } else if (op.equals("%")) {
                            return op1 % op2;
                    }
                    throw new UnsupportedOperationException(op);
                }

	}
}
