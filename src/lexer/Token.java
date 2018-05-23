package lexer;

import java.util.HashMap;
import java.util.Map;

import lexer.Char.CharacterType;

public class Token {
	private final TokenType type;
	private final String lexme;
	/*이 ofName클래스들이 이 프로그램에서 많은 역할을 한다고 생각한다.*/
	static Token ofName(String lexme) {
		TokenType type = KEYWORDS.get(lexme);
		/* 첫번째로 밑에 구현된 Keywords들을 가져와 그타입이 아니면 바로 Keyword와 lexme을 사용한다는 조건문을 사용한다.*/
		if (type != null) {
			return new Token(type, lexme);
		} 
		/*받아온 lexme이 마지막에 ?로 끝나는 경우를 나누어준다. 맞으면 Question상태로 반환해준다.
		 * 그리고 마지막에 물음표로 끝나지만 중간에 물음표를 가지고 있는 것을 예외처리해준다.*/
		else if (lexme.endsWith("?")) {
			if (lexme.substring(0, lexme.length() - 1).contains("?")) {
				throw new ScannerException("invalid ID=" + lexme);
			}
			return new Token(TokenType.QUESTION, lexme);
		}
		/*여기부터는 Token.java 파일을 수정한 부분이다. DFA를 기반으로 문자열 사이에 특수문자를 가지고 있다면
		 * 밑에 예외 처리가 없으면 ID로 상태가 지정된다. 그러므로 예외 처리를 해주기 위해 밑에 조건문을 건다.
		 * 조건문은 문자열 사이에 특수문자를 가지고 있으면 ScannerException을 주는 것을 의미한다.*/
		else if (lexme.contains("?")) {
			throw new ScannerException("invalid ID=" + lexme);
		}
		else if(lexme.contains("*")) {
			throw new ScannerException("invalid ID=" + lexme);
		}else if(lexme.contains("/")) {
			throw new ScannerException("invalid ID=" + lexme);
		}else if(lexme.contains("(")) {
			throw new ScannerException("invalid ID=" + lexme);
		}else if(lexme.contains(")")) {
			throw new ScannerException("invalid ID=" + lexme);
		}else if(lexme.contains("<")) {
			throw new ScannerException("invalid ID=" + lexme);
		}
		else if(lexme.contains("=")) {
			throw new ScannerException("invalid ID=" + lexme);
		}
		else if(lexme.contains(">")) {
			throw new ScannerException("invalid ID=" + lexme);
		}
		else if(lexme.contains("\'")) {
			throw new ScannerException("invalid ID=" + lexme);
		}
		else if(lexme.contains("#")) {
			throw new ScannerException("invalid ID=" + lexme);
		}
		/*나머지는 ID로 반환한다.*/
		 else {
			return new Token(TokenType.ID, lexme);
		}
	}

	Token(TokenType type, String lexme) {
		this.type = type;
		this.lexme = lexme;
	}

	public TokenType type() {
		return this.type;
	}

	public String lexme() {
		return this.lexme;
	}

	@Override
	public String toString() {
		return String.format("%s(%s)", type, lexme);
	}

	private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
	static {
		/*이부분은 주어진 부분이지만, 밑에 경우들에 Keywords로 지정하는 부분을 의미한다.
		 * 위에 ofName 함수에서 사용한다.*/
		KEYWORDS.put("define", TokenType.DEFINE);
		KEYWORDS.put("lambda", TokenType.LAMBDA);
		KEYWORDS.put("cond", TokenType.COND);
		KEYWORDS.put("quote", TokenType.QUOTE);
		KEYWORDS.put("not", TokenType.NOT);
		KEYWORDS.put("cdr", TokenType.CDR);
		KEYWORDS.put("car", TokenType.CAR);
		KEYWORDS.put("cons", TokenType.CONS);
		KEYWORDS.put("eq?", TokenType.EQ_Q);
		KEYWORDS.put("null?", TokenType.NULL_Q);
		KEYWORDS.put("atom?", TokenType.ATOM_Q);
	}
}