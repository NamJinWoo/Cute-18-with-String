package lexer;

import java.util.Optional;

class TransitionOutput {
	private final State nextState;
	private final Optional<Token> token;

	static TransitionOutput GOTO_START = new TransitionOutput(State.START);
	static TransitionOutput GOTO_ACCEPT_ID = new TransitionOutput(State.ACCEPT_ID);
	static TransitionOutput GOTO_ACCEPT_INT = new TransitionOutput(State.ACCEPT_INT);
	static TransitionOutput GOTO_SIGN = new TransitionOutput(State.SIGN);
	static TransitionOutput GOTO_FAILED = new TransitionOutput(State.FAILED);
	static TransitionOutput GOTO_EOS = new TransitionOutput(State.EOS);
	/*Sharp인경우와 True,False를 구현하고 SpecialChar로 가는 부분을 구현한다.*/
	static TransitionOutput GOTO_SHARP = new TransitionOutput(State.SHARP);
	static TransitionOutput GOTO_TRUE = new TransitionOutput(State.TRUE);
	static TransitionOutput GOTO_FALSE = new TransitionOutput(State.FALSE);
	static TransitionOutput GOTO_SPECIAL_CHAR = new TransitionOutput(State.SPECIAL_CHAR);
	static TransitionOutput GOTO_PLUS = new TransitionOutput(State.PLUS);
	static TransitionOutput GOTO_MINUS = new TransitionOutput(State.MINUS);
	
	static TransitionOutput GOTO_MATCHED(TokenType type, String lexime) {
		return new TransitionOutput(State.MATCHED, new Token(type, lexime));
	}
	static TransitionOutput GOTO_MATCHED(Token token) {
		return new TransitionOutput(State.MATCHED, token);
	}
	
	TransitionOutput(State nextState, Token token) {
		this.nextState = nextState;
		this.token = Optional.of(token);
	}
	
	TransitionOutput(State nextState) {
		this.nextState = nextState;
		this.token = Optional.empty();
	}
	
	State nextState() {
		return this.nextState;
	}
	
	Optional<Token> token() {
		return this.token;
	}
}