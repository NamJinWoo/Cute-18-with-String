package lexer;

class Char {
	private final char value;
	private final CharacterType type;

	enum CharacterType { 
		LETTER, DIGIT, 
		SPECIAL_CHAR, WS, END_OF_STREAM,SIGN
	}
	
	static Char of(char ch) {
		return new Char(ch, getType(ch));
	}
	
	static Char end() {
		return new Char(Character.MIN_VALUE, CharacterType.END_OF_STREAM);
	}
	
	private Char(char ch, CharacterType type) {
		this.value = ch;
		this.type = type;
	}
	
	char value() {
		return this.value;
	}
	
	CharacterType type() {
		return this.type;
	}
	
	private static CharacterType getType(char ch) {
		/*받아온 ch를 이용하여 그 ch가 조건문을 통해 문자인지 숫자인지를 판별한다.*/
		int code = (int)ch;
		if ( (code >= (int)'A' && code <= (int)'Z')
			|| (code >= (int)'a' && code <= (int)'z')) {
			return CharacterType.LETTER;
		}
		if ( Character.isDigit(ch) ) {
			return CharacterType.DIGIT;
		}
		/*이부분에서는 Special Char를 구하는 switch문이다. 특수 기호에는 여러가지들이있다. 하지만 모두를 special char로 반환하면 안된다.
		 * 우선 -와+는 바로 Signdl이라는 상태를 주어진다. 그이유는 나머지는 letter로 인식하지만 -와 +는 Digit으로 인식하기 때문에 서로 다르게 상태를 지정해줘야한다.
		 * 나머지는 Special Character로 지정을 해준다. 이것들은 TokenType클래스에서 또 세부적으로 나눠줄 것이다.*/
		switch ( ch ) {
			case '-': 
				return CharacterType.SIGN;
			case '+':
				return CharacterType.SIGN;
			case '/':
				return CharacterType.SPECIAL_CHAR;
			case '(':
				return CharacterType.SPECIAL_CHAR;
			case ')':
				return CharacterType.SPECIAL_CHAR;
			case '*':
				return CharacterType.SPECIAL_CHAR;
			case '`':
				return CharacterType.SPECIAL_CHAR;
			case '<':
				return CharacterType.SPECIAL_CHAR;
			case '=':
				return CharacterType.SPECIAL_CHAR;
			case '>':
				return CharacterType.SPECIAL_CHAR;
			case '\'':
				return CharacterType.SPECIAL_CHAR;
			case '#':
				return CharacterType.SPECIAL_CHAR;
			case '?':
				return CharacterType.SPECIAL_CHAR;
		}
		/*이부분은 Character.isWhitespace라는 기본적으로 주어지는 함수를 이용해
		 * 만약에 여백이라면, WS상태로 반환해준다.*/
		if ( Character.isWhitespace(ch) ) {
			return CharacterType.WS;
		}
		
		throw new IllegalArgumentException("input=" + ch);
	}
}
