package parser.ast;

public class BooleanNode implements ValueNode {
	Boolean value;

	@Override
	public String toString() {
		return value ? "True: #T" : "False: #F";
	}

	public static BooleanNode FALSE_NODE = new BooleanNode(false);
	public static BooleanNode TRUE_NODE = new BooleanNode(true);

	private BooleanNode(Boolean b) {
		value = b;
	}
}