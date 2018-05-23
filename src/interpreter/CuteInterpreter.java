package interpreter;

import parser.ast.*;
import parser.parse.CuteParser;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import parser.parse.NodePrinter;

import java.io.File;

public class CuteInterpreter {
	static HashMap<String, Node> insertT = new HashMap<String, Node>();

	public void insertTable(Node a, Node b) {
		insertT.put(a.toString(), b);
	}

	public Node lookupTable(String id) {
		if (insertT.containsKey(id)) {
			return insertT.get(id);
		}
		return null;
	}

	private void errorLog(String err) {
		System.out.println(err);
	}

	public Node runExpr(Node rootExpr) {
		if (rootExpr == null)
			return null;

		if (rootExpr instanceof IdNode) {
			Node x = rootExpr;
			x = lookupTable(x.toString());
			if (x == null) {
				return rootExpr;
			} else {
				runExpr(x);
				return x;
			}
		} else if (rootExpr instanceof IntNode)
			return rootExpr;
		else if (rootExpr instanceof BooleanNode)
			return rootExpr;
		else if (rootExpr instanceof ListNode)
			return runList((ListNode) rootExpr);
		else
			errorLog("run Expr error");
		return null;
	}

	private Node runList(ListNode list) {
		if (list.equals(ListNode.EMPTYLIST))
			return list;
		if (list.car() instanceof FunctionNode) {
			return runFunction((FunctionNode) list.car(), list.cdr());
		}
		if (list.car() instanceof BinaryOpNode) {
			return runBinary(list);
		}
		return list;
	}

	private Node runFunction(FunctionNode operator, ListNode operand) {
		Node Fx = operand.car();
		Node Fy = operand.cdr().car();
		// if (!(operand.car() instanceof IdNode) && !(operand.car() instanceof IntNode)
		// && !(operand.car() instanceof QuoteNode)) {
		// if (((ListNode) runQuote(operand)).equals(ListNode.ENDLIST)) {
		// Fy = null;
		// }
		// else {
		// Fy = lookupTable(operand.cdr().car().toString());
		// }
		// }
		if (operand.car() instanceof IdNode) {
			Fx = lookupTable(operand.car().toString());
			if (operand.car() instanceof QuoteNode) {
				if (((ListNode) runQuote(operand)).equals(ListNode.ENDLIST)) {
					Fy = null;
				}
			} else {
				if (!(operand.cdr().car() == null)) {
					Fy = lookupTable(operand.cdr().car().toString());
				} else {
					Fy = null;
				}
			}
		} else {
			Fx = operand.car();
			if (operand.car() instanceof QuoteNode) {
				if (runQuote(operand) instanceof ListNode) {
					if (((ListNode) runQuote(operand)).equals(ListNode.ENDLIST)) {
						Fy = null;
					} else {
						if (!(operand.cdr().car() == null)) {
							Fy = lookupTable(operand.cdr().car().toString());
						} else {
							Fy = null;
						}
					}
				}
			} 
			else if (operand.car() instanceof BooleanNode) {
				Fy = null;
			}
			else {
				if (!(operand.cdr().car() == null)) {
					Fy = lookupTable(operand.cdr().car().toString());
				} else {
					Fy = null;
				}
			}
		}

		if (Fx == null) {
			Fx = operand.car();
		}
		if (Fy == null) {
			Fy = operand.cdr().car();
		}
		switch (operator.value) {// 바꿈.
		// CAR, CDR, CONS등에 대한 동작 구현
		case CAR: // (((QuoteNode)Fx).nodeInside())
			return ((ListNode) (((QuoteNode) Fx).nodeInside())).car();
		case CDR:
			return new QuoteNode(((ListNode) (((QuoteNode) Fx).nodeInside())).cdr());
		case CONS:
			if ((Node) Fx instanceof QuoteNode) {
				ListNode a = (ListNode) (((QuoteNode) Fx).nodeInside());
				ListNode x = (ListNode) (((QuoteNode) Fy).nodeInside());

				return new QuoteNode(ListNode.cons(a, x));
			} else
				return new QuoteNode(ListNode.cons((Node) Fx, (ListNode) ((QuoteNode) Fy).nodeInside()));
		case NULL_Q:
			if (((ListNode) runQuote(operand)).equals(ListNode.ENDLIST)) {
				return BooleanNode.TRUE_NODE;
			} else {
				return BooleanNode.FALSE_NODE;
			}
		case ATOM_Q:
			if (runQuote(operand) instanceof ListNode) {
				return BooleanNode.FALSE_NODE;
			} else
				return BooleanNode.TRUE_NODE;
		case EQ_Q:
			Node x = (((QuoteNode) Fx).nodeInside());
			Node y = ((QuoteNode) Fy).nodeInside();
			if (x.toString().equals(y.toString())) {
				return BooleanNode.TRUE_NODE;
			} else
				return BooleanNode.FALSE_NODE;
		case COND:
			ListNode c = operand;
			while (!c.equals(ListNode.ENDLIST)) {
				if (runExpr(((ListNode) c.car()).car()).equals(BooleanNode.TRUE_NODE)) {
					return ((ListNode) c.car()).cdr().car();
				} else {
					c = c.cdr();
				}
			}
			return null;
		case NOT:
			if (runExpr(operand.car()).equals(BooleanNode.TRUE_NODE)) {
				return BooleanNode.FALSE_NODE;
			} else
				return BooleanNode.TRUE_NODE;
		case DEFINE:
			if (Fx instanceof ListNode || Fx instanceof QuoteNode) {
				return null;
			} else {
				if (operand.cdr().car() instanceof QuoteNode) {
					insertTable(Fx, operand.cdr().car());
				} else {
					insertTable(Fx, runExpr(operand.cdr().car()));
				}
			}
		default:
			break;
		}
		System.out.println(insertT);

		return null;
	}

	private Node runBinary(ListNode list) {
		BinaryOpNode operator = (BinaryOpNode) list.car(); // 바꿈.

		Node x = list.cdr().car();
		Node y = list.cdr().cdr().car();

		x = lookupTable(x.toString());
		y = lookupTable(y.toString());

		if (x == null) {
			x = list.cdr().car();
		} 
		if (y == null) {
			y = list.cdr().cdr().car();
		}

		// 구현과정에서 필요한 변수 및 함수 작업 가능
		switch (operator.value) {

		// +,-,/ 등에 대한 바이너리 연산 동작 구현
		case PLUS:
			return new IntNode(Integer.toString(((IntNode) runExpr(x)).value + ((IntNode) runExpr(y)).value));
		case MINUS:
			return new IntNode(Integer.toString(((IntNode) runExpr(x)).value - ((IntNode) runExpr(y)).value));
		case DIV:
			return new IntNode(Integer.toString(((IntNode) runExpr(x)).value / ((IntNode) runExpr(y)).value));
		case TIMES:
			return new IntNode(Integer.toString(((IntNode) runExpr(x)).value * ((IntNode) runExpr(y)).value));
		case LT:
			if (((IntNode) runExpr(x)).value < ((IntNode) runExpr(y)).value) {
				return BooleanNode.TRUE_NODE;
			} else
				return BooleanNode.FALSE_NODE;
		case GT:
			if (((IntNode) runExpr(x)).value > ((IntNode) runExpr(y)).value) {
				return BooleanNode.TRUE_NODE;
			} else
				return BooleanNode.FALSE_NODE;
		case EQ:
			if (((IntNode) runExpr(x)).value.equals(((IntNode) runExpr(y)).value)) {
				return BooleanNode.TRUE_NODE;
			} else
				return BooleanNode.FALSE_NODE;

		default:
			break;
		}
		return null;
	}

	private Node runQuote(ListNode node) {
		return ((QuoteNode) node.car()).nodeInside();
	}
}