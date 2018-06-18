package interpreter;

import parser.ast.*;
import parser.parse.CuteParser;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import lexer.TokenType;
import parser.parse.NodePrinter;

import java.io.File;

public class CuteInterpreter {
	static HashMap<String, Node> insertT = new HashMap<String, Node>();
	String lambdakey;

	public void insertTable(Node a, Node b) {
		insertT.put(a.toString(), b);
	}

	public Node lookupTable(String id) { // ( ( lambda ( x ) ( + x 1 ) ) 1 )
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
			} else if (x instanceof ListNode) {
				return runExpr(x);
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
		else if (rootExpr instanceof QuoteNode)
			return rootExpr;
		else
			errorLog("run Expr error");
		return null;
	}

	private Node runList(ListNode list) {
		// Node lambda = lookupTable(list.car().toString());

		if (list.equals(ListNode.EMPTYLIST) || list.equals(ListNode.ENDLIST))
			return list;
		if (list.car() instanceof FunctionNode) {
			if (((FunctionNode) list.car()).value.equals(FunctionNode.FunctionType.LAMBDA)) {

				return list;
			}
			return runFunction((FunctionNode) list.car(), list.cdr());
		}
		if (list.car() instanceof BinaryOpNode) {
			return runBinary(list);
		}
		if (list.car() instanceof ListNode) {
			if (((ListNode) list.car()).car() instanceof FunctionNode) {
				ListNode x = (ListNode) list.car();
				IntNode y = (IntNode) list.cdr().car();
				if (((FunctionNode) x.car()).value.equals(FunctionNode.FunctionType.LAMBDA)) {
					insertTable(((ListNode) x.cdr().car()).car(), y);
					lambdakey = ((ListNode) x.cdr().car()).car().toString();
					return runFunction(/* new FunctionNode(TokenType.LAMBDA) */(FunctionNode) x.car(), x.cdr());
				}
			}
			if (list instanceof ListNode) {

				if (!(((ListNode) list).car() instanceof FunctionNode)) {
					Node rl = ListNode.ENDLIST, rl1 = ListNode.ENDLIST, rl2 = ListNode.ENDLIST;
					if (list != ListNode.ENDLIST) {
						if (list.car() != ListNode.ENDLIST) {
							rl1 = runExpr(list.car());
						}
						if (list.cdr() != ListNode.ENDLIST) {
							rl2 = runExpr(list.cdr());
						}
					}
					rl = ListNode.cons(rl1, (ListNode) rl2);
					return (ListNode) rl;
				}
				if (((FunctionNode) ((ListNode) list).car()).value.equals(FunctionNode.FunctionType.LAMBDA)) {
					ListNode x = (ListNode) list;
					IntNode y = (IntNode) list.cdr().car();
					insertTable(((ListNode) x.cdr().car()).car(), y);
					lambdakey = ((ListNode) x.cdr().car()).car().toString();
					return runFunction((FunctionNode) ((ListNode) list).car(), x.cdr());
				}
			}
			return list;
		}
		if (list.car() instanceof IdNode) {
			ListNode lambda = list;

			while (!(lambda.equals(ListNode.ENDLIST))) {
				if ((lambda).car() instanceof IdNode) {
					lambda = (ListNode) lookupTable(lambda.car().toString());
					if (lambda == null) {
						lambda = list.cdr();
					}
					break;
				} else {
					lambda = lambda.cdr();
				}
			}
			if (((FunctionNode) ((ListNode) lambda).car()).value.equals(FunctionNode.FunctionType.LAMBDA)) {
				ListNode x = (ListNode) lambda;
				IntNode y = (IntNode) list.cdr().car();
				insertTable(((ListNode) x.cdr().car()).car(), y);
				lambdakey = ((ListNode) x.cdr().car()).car().toString();
				return runFunction((FunctionNode) ((ListNode) lambda).car(), x.cdr());
			}
			System.out.println(insertT);
			return runExpr(list.car());
		}

		return list;
	}

	private Node runFunction(FunctionNode operator, ListNode operand) {
		Node Fx = operand.car();
		Node Fy = operand.cdr().car();
		if (Fx instanceof IdNode) {
			switch (operator.value) {
			case DEFINE:
				break;
			default:
				Fx = lookupTable(Fx.toString());
			}
		}
		if (Fy instanceof IdNode) {
			Fy = lookupTable(Fy.toString());
		}
		Fy = ListNode.cons(Fy, ListNode.ENDLIST);
		ListNode op = ListNode.cons(Fx, (ListNode) Fy);
		switch (operator.value) {
		// CAR, CDR, CONS� ���� ���� ����
		case CAR:
			ListNode ln1 = (ListNode) runQuote(op);
			return ln1.car();
		case CDR:
			ListNode ln2 = (ListNode) runQuote(op);
			QuoteNode qn1 = new QuoteNode(runList(ln2.cdr()));
			return qn1;
		case CONS:
			Node head = op.car();
			if (head instanceof QuoteNode) {
				head = ((QuoteNode) head).nodeInside();
			} else {
				head = runExpr(head);
				if (head instanceof QuoteNode) {
					head = ((QuoteNode) head).nodeInside();
				}
			}

			if (op.cdr().car() instanceof QuoteNode) {
				ListNode tail = op.cdr();
				Node n1 = runQuote(tail);
				ListNode ln3 = ListNode.cons(head, (ListNode) n1);
				QuoteNode qn2 = new QuoteNode(ln3);
				return qn2;
			} else {
				QuoteNode tail = (QuoteNode) runExpr(op.cdr().car());
				Node n1 = tail.nodeInside();
				ListNode ln3 = ListNode.cons(head, (ListNode) n1);
				QuoteNode qn2 = new QuoteNode(ln3);
				return qn2;
			}
		case COND:
			while (!op.equals(ListNode.ENDLIST)) {
				if (runExpr(((ListNode) op.car()).car()).equals(BooleanNode.TRUE_NODE)) {
					return runExpr(((ListNode) op.car()).cdr().car());
				} else {
					op = op.cdr();
				}
			}
			return null;
		case NOT:
			Node not = op.car();
			if (not instanceof ListNode) {
				not = runBinary((ListNode) not);
			}
			if (not == BooleanNode.TRUE_NODE) {
				return BooleanNode.FALSE_NODE;
			} else {
				return BooleanNode.TRUE_NODE;
			}
		case NULL_Q:
			Node nq = runQuote(op);
			if (((ListNode) nq).car() == null && ((ListNode) nq).cdr() == null) {
				return BooleanNode.TRUE_NODE;
			} else {
				return BooleanNode.FALSE_NODE;
			}
		case EQ_Q:
			if (op.car() instanceof QuoteNode && op.cdr().car() instanceof QuoteNode) {
				Node eq1 = op.car();
				Node eq2 = ((QuoteNode) op.cdr().car()).nodeInside();
				if (eq1.toString().equals(eq2.toString())) {
					return BooleanNode.TRUE_NODE;
				} else
					return BooleanNode.FALSE_NODE;
			} else {
				if (op.car().toString().equals(op.cdr().car().toString())) {
					return BooleanNode.TRUE_NODE;
				} else
					return BooleanNode.FALSE_NODE;
			}

		case ATOM_Q:
			Node aq = runQuote(op);
			if (aq instanceof ListNode) {
				return BooleanNode.FALSE_NODE;
			} else {
				return BooleanNode.TRUE_NODE;
			}
		case LAMBDA:
			return runBinary((ListNode) op.cdr().car());
		case DEFINE:
			Node x = op.car();
			Node y = op.cdr().car();
			if (((FunctionNode) ((ListNode) y).car()).value.equals(FunctionNode.FunctionType.LAMBDA)) {
				runExpr(y);
			}
			if (y instanceof ListNode) {
				if (((ListNode) y).car() instanceof FunctionNode) {
					insertTable(x, y);
				} else {
					y = runExpr(y);
				}
			}
			insertTable(x, y);
		default:
			break;
		}
		System.out.println(insertT);
		return null;
	}

	private Node runBinary(ListNode list) {
		// System.out.println(insertT);
		BinaryOpNode operator = (BinaryOpNode) list.car(); // 바꿈.

		Node newTrue = BooleanNode.TRUE_NODE;
		Node newFalse = BooleanNode.FALSE_NODE;

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
			Node newplus = new IntNode(Integer.toString(((IntNode) runExpr(x)).value + ((IntNode) runExpr(y)).value));
			insertT.remove(lambdakey);
			System.out.println(insertT);
			return newplus;
		case MINUS:
			Node newminus = new IntNode(Integer.toString(((IntNode) runExpr(x)).value - ((IntNode) runExpr(y)).value));
			insertT.remove(lambdakey);
			return newminus;
		case DIV:
			Node newdiv = new IntNode(Integer.toString(((IntNode) runExpr(x)).value / ((IntNode) runExpr(y)).value));
			insertT.remove(lambdakey);
			return newdiv;
		case TIMES:
			Node newtimes = new IntNode(Integer.toString(((IntNode) runExpr(x)).value * ((IntNode) runExpr(y)).value));
			insertT.remove(lambdakey);
			return newtimes;
		case LT:
			if (((IntNode) runExpr(x)).value < ((IntNode) runExpr(y)).value) {
				insertT.remove(lambdakey);
				return newTrue;
			} else {
				insertT.remove(lambdakey);
				return newFalse;
			}
				
		case GT:
			if (((IntNode) runExpr(x)).value > ((IntNode) runExpr(y)).value) {
				insertT.remove(lambdakey);
				return newTrue;
			}else {
				insertT.remove(lambdakey);
				return newFalse;
			}
		case EQ:
			if (((IntNode) runExpr(x)).value.equals(((IntNode) runExpr(y)).value)) {
				return newTrue;
			} else {
				insertT.remove(lambdakey);
				return newFalse;
			}

		default:
			break;
		}
		return null;
	}

	private Node runQuote(ListNode node) {
		return ((QuoteNode) node.car()).nodeInside();
	}
}