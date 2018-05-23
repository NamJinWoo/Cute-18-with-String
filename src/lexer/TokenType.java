package lexer;
import java.util.*;

import lexer.Char.CharacterType;


public enum TokenType {
	ID,
	INT, QUESTION,
	TRUE, FALSE, NOT,
	PLUS, MINUS, TIMES, DIV,
	LT, GT, EQ, APOSTROPHE,
	L_PAREN, R_PAREN, SHARP,
	DEFINE, LAMBDA, COND, QUOTE,
	CAR, CDR, CONS,
	ATOM_Q, NULL_Q, EQ_Q;
	static TokenType fromSpecialCharactor(char ch) {
		/*여기서는 함수 이름 그대로 Special Charactor로 지정된 것들을 세부적으로 나눠준다.
		 * 이것들은 State클래스에서 이용이 될건데 각자 가지는 상태를 프린트해야 되기때문에
		 * State클래스에서 Start에서 Special Character를 받았을때 TokenType.fromSpecialCharacter를 
		 * 호출해주면 여기 switch문을 돌면서 Goto Match 부분에서 어떻게 최종적으로 반환을 해 줄 것인지를 결정한다.*/
		/*몇가지들을 case문에 넣지 못하는 경우들이 있기때문에 반환해주지 않았지만 State클래스에서 충분히 구현 가능하다.
		 * 하지만 결국에는 TokenType.True등 여기 클래스의 상태를 호출해주어야한다.*/
		switch ( ch ) {
			case '-':
				return MINUS;
			case '+':
				return PLUS;
			case '*':
				return TIMES;
			case '/':
				return DIV;
			case '(':
				return L_PAREN;
			case ')':
				return R_PAREN;
			case '<':
				return LT;
			case '=':
				return EQ;
			case '>':
				return GT;
			case '`':
				return APOSTROPHE;
			case '#':
				return SHARP;
		//정규 표현식을 참고하여 ch와 매칭되는 keyword를 반환하는 case문 작성
		default:
			throw new IllegalArgumentException("unregistered char: " + ch);
		}
	}
}
