package parser.parse;

import java.io.File;
import java.util.Scanner;

import interpreter.CuteInterpreter;
import parser.ast.Node;

public class ParserMain {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		while(true) {
			System.out.print(">  ");
			String x = input.nextLine();
			CuteParser cuteParser = new CuteParser(x);
			Node parseTree = cuteParser.parseExpr();
			CuteInterpreter i = new CuteInterpreter();
			Node resultNode = i.runExpr(parseTree);
			System.out.print("...");
			NodePrinter.getPrinter(System.out).prettyPrint(resultNode);
			System.out.println("");
		}
	}
}
