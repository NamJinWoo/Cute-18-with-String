package parser.ast;

import java.util.HashMap;
import java.util.Map;

import lexer.TokenType;

public class BinaryOpNode implements Node {
	public enum BinType {
		MINUS {
			TokenType tokenType() {
				return TokenType.MINUS;
			}
			@Override
			public String toString() {
				return "-";
			}
		},
		PLUS {
			TokenType tokenType() {
				return TokenType.PLUS;
			}
			@Override
			public String toString() {
				return "+";
			}
		},
		TIMES {
			TokenType tokenType() {
				return TokenType.TIMES;
			}
			@Override
			public String toString() {
				return "*";
			}
		},
		DIV {
			TokenType tokenType() {
				return TokenType.DIV;
			}
			@Override
			public String toString() {
				return "/";
			}
		},
		LT {
			TokenType tokenType() {
				return TokenType.LT;
			}
			@Override
			public String toString() {
				return "<";
			}
		},
		GT {
			TokenType tokenType() {
				return TokenType.GT;
			}
			@Override
			public String toString() {
				return ">";
			}
		},
		EQ {
			TokenType tokenType() {
				return TokenType.EQ;
			}
			@Override
			public String toString() {
				return "=";
			}
		};
		private static Map<TokenType, BinType> fromTokenType = new HashMap<TokenType, BinType>();
		static {
			for (BinType bType : BinType.values()) {
				fromTokenType.put(bType.tokenType(), bType);
			}
		}

		static BinType getBinType(TokenType tType) {
			return fromTokenType.get(tType);
		}

		abstract TokenType tokenType();
	}

	public BinType value;

	public BinaryOpNode(TokenType tType) {
		BinType bType = BinType.getBinType(tType);
		value = bType;
	}

	@Override
	public String toString() {
		return "BinaryOp: "+value.toString();
	}
}
