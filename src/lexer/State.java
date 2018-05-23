package lexer;

import static lexer.TokenType.FALSE;
import static lexer.TokenType.ID;
import static lexer.TokenType.INT;
import static lexer.TokenType.TRUE;
import static lexer.TransitionOutput.GOTO_ACCEPT_ID;
import static lexer.TransitionOutput.GOTO_ACCEPT_INT;
import static lexer.TransitionOutput.GOTO_EOS;
import static lexer.TransitionOutput.GOTO_FAILED;
import static lexer.TransitionOutput.GOTO_FALSE;
import static lexer.TransitionOutput.GOTO_MATCHED;
import static lexer.TransitionOutput.GOTO_MINUS;
import static lexer.TransitionOutput.GOTO_PLUS;
import static lexer.TransitionOutput.GOTO_SHARP;
import static lexer.TransitionOutput.GOTO_SIGN;
import static lexer.TransitionOutput.GOTO_SPECIAL_CHAR;
import static lexer.TransitionOutput.GOTO_START;
import static lexer.TransitionOutput.GOTO_TRUE;

enum State {
	/* 이부분에서 모든 상태 결정이 일어난다고 해도 과언이 아니다. */
	START {
		/*
		 * 이부분은 첫부분이다. 처음 시작을 알리는데, 모든 케이스와 동일하게 그다음 문자가 무엇이 오는지에 따라 GoTo로 나누고 그 Goto는
		 * 다른 함수로 가는것을 의미한다.
		 */
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			case LETTER:
				context.append(v);
				return GOTO_ACCEPT_ID;
			case DIGIT:
				context.append(v);
				return GOTO_ACCEPT_INT;
			case SIGN: {
				if(v == '+') {
				context.append(v);
				return GOTO_PLUS;
				}
				else {
					context.append(v);
					return GOTO_MINUS;
				}
//				context.getCharStream().pushBack(v);
			}
			case SPECIAL_CHAR:
				/*
				 * 이부분을 수정하였다. #T/F를 결정하기 위해서 Special Char의 하나인 #을 #함수를 새로 만들어 그쪽으로 보내도록 만들었다.
				 */
				if (v == '#') {
					context.append(v);
					return GOTO_SHARP;
				} else {
					context.append(v);
					return GOTO_SPECIAL_CHAR;
				}
			case WS:
				return GOTO_START;
			case END_OF_STREAM:
				return GOTO_EOS;
			default:
				throw new AssertionError();
			}
		}
	},
	ACCEPT_ID {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * 이부분을 문자,숫자, 특수문자가 왔을때 다시 재귀로 돌리는 모습임을 알수있다. 여기서 특수문자는 아닌데 왜 돌리느냐는 질문이 있겠지만,
			 * ?인것은 Question이나 Keyword로 사용하기 때문에 Token클래스에서 ofName함수부분이 처리를 해준다.
			 */
			case LETTER:
			case DIGIT:
			case SPECIAL_CHAR:
				context.append(v);
				return GOTO_ACCEPT_ID;
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(Token.ofName(context.getLexime()));
			/*
			 * 이부분이 WS이면서 마지막일때 Token.ofName함수를 호출해 조건문을 돌려준다. 이것은 Keyword, Question, 예외처리까지
			 * 할 수 있어야 하기때문에 한다. (DFA에 근거 ID이후로 ?외에 다른것은 올 수 없다.)
			 */
			default:
				throw new AssertionError();
			}
		}
	},
	ACCEPT_INT {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			switch (ch.type()) {
			/*
			 * 이부분은 주어진 부분이지만, 숫자다음에 다시 숫자가 나오면 Fail이고 마지막이라면 상태를 INT 그리고 옆에 입력값을 출력하는 것을
			 * 의미한다.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(ch.value());
				return GOTO_ACCEPT_INT;
			case SPECIAL_CHAR:
				return GOTO_FAILED;
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(INT, context.getLexime());
			default:
				throw new AssertionError();
			}
		}
	},
	SIGN {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * 이부분은 SIGN일때를 설명한다. Sign인부분이 +와 -부분인데, 마지막부분에 조건문을 걸어주어 +일때 PLUS와 -일때 MINUS를
			 * 설명한다. 그렇다면 양수와 음수는 어떻게 설명을 하는가? 밑에 Digit 부분을 보면 Accept int로 넘어가기때문에 상태는 INT로
			 * 나오게 된다.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(v);
				return GOTO_ACCEPT_INT;
			case WS:
			case END_OF_STREAM:
				return GOTO_FAILED;
			default:
				throw new AssertionError();
			}
		}
	},
	PLUS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * 이부분은 SIGN일때를 설명한다. Sign인부분이 +와 -부분인데, 마지막부분에 조건문을 걸어주어 +일때 PLUS와 -일때 MINUS를
			 * 설명한다. 그렇다면 양수와 음수는 어떻게 설명을 하는가? 밑에 Digit 부분을 보면 Accept int로 넘어가기때문에 상태는 INT로
			 * 나오게 된다.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(v);
				return GOTO_ACCEPT_INT;
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(TokenType.PLUS, context.getLexime());

			default:
				throw new AssertionError();
			}
		}
	},	
	MINUS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * 이부분은 SIGN일때를 설명한다. Sign인부분이 +와 -부분인데, 마지막부분에 조건문을 걸어주어 +일때 PLUS와 -일때 MINUS를
			 * 설명한다. 그렇다면 양수와 음수는 어떻게 설명을 하는가? 밑에 Digit 부분을 보면 Accept int로 넘어가기때문에 상태는 INT로
			 * 나오게 된다.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(v);
				return GOTO_ACCEPT_INT;
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(TokenType.MINUS, context.getLexime());
			default:
				throw new AssertionError();
			}
		}
	},
	SPECIAL_CHAR {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * 이부분이 중요하다. Special character 다음에 다른것이온다면 무조건 fail 마지막이라면 넘기는 것을 이야기한다.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(v);
				return GOTO_FAILED;
			case WS:
			case END_OF_STREAM:
				/*
				 * fromSpecialCahractor를 사용하여 switch문을 돌린다음 반환된 상태를 이용하는 것이다. chatAt를 사용하는 것은
				 * String 값을 char로 변환해주기 위함이다.
				 */
				String temp = context.getLexime();
				return GOTO_MATCHED(TokenType.fromSpecialCharactor(temp.charAt(0)), temp);
			default:
				throw new AssertionError();
			}
		}
	},
	SHARP {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * 이부분은 새로 만든 부분인데, #을 받았을 때 여기로 넘어온다. 그다음이 문자열이 아니면 전부 fail, 그리고 문자열 중에서도 T를
			 * 받으면 True로 가고 F를 받으면 False로 간다.
			 */
			case LETTER:
				if (v == 'T') {
					context.append(v);
					return GOTO_TRUE;
				} else if (v == 'F') {
					context.append(v);
					return GOTO_FALSE;
				}
			case DIGIT:
				return GOTO_FAILED;
			case WS:
			case END_OF_STREAM:
				return GOTO_FAILED;

			default:
				throw new AssertionError();
			}
		}
	},
	TRUE {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * 이부분은 마지막만 있으면 된다. 마지막이 TokenType에서 switch문으로 설정을 안하였기 때문에 그냥 TokenType.True를
			 * 사용하여 상태를 설정하고 매치 시킨다.
			 */
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(TokenType.TRUE, context.getLexime());
			default:
				throw new AssertionError();
			}
		}
	},
	FALSE {
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * 이부분은 마지막만 있으면 된다. 마지막이 TokenType에서 switch문으로 설정을 안하였기 때문에 그냥 TokenType.False를
			 * 사용하여 상태를 설정하고 매치 시킨다.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				return GOTO_FAILED;
			case SPECIAL_CHAR:
				return GOTO_FAILED;
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(TokenType.FALSE, context.getLexime());
			default:
				throw new AssertionError();
			}
		}
	},
	MATCHED {
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	FAILED {
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	EOS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			return GOTO_EOS;
		}
	};

	abstract TransitionOutput transit(ScanContext context);
}
