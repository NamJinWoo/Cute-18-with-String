package parser.ast;

import java.util.HashMap;
import java.util.Map;

import lexer.TokenType;

public class FunctionNode implements Node { // binaryOpNode클래스를 보고 참고해서 작성
	//우선 binaryOpNode에서와 같이 enum을 만들어준다.
	public enum FunctionType {
		ATOM_Q { //3주차 과제처럼 AtomQ일 때 토큰타입을 AtomQ로 설정
			TokenType tokenType() {
				return TokenType.ATOM_Q;
			}
		},
		CAR { //CAR일 때 토큰타입을 CAR로 설정
			TokenType tokenType() {
				return TokenType.CAR;
			}
		},
		CDR { //CAR일 때 토큰타입을 CDR로 설정
			TokenType tokenType() {
				return TokenType.CDR;
			}
		},
		COND { //CDR일 때 토큰타입을 COND로 설정
			TokenType tokenType() {
				return TokenType.COND;
			}
		},
		CONS { //CONS일 때 토큰타입을 CONS로 설정
			TokenType tokenType() {
				return TokenType.CONS;
			}
		},
		DEFINE { //DEFINE일 때 토큰타입을 DEFINE로 설정
			TokenType tokenType() {
				return TokenType.DEFINE;
			}
		},
		EQ_Q { //EQQ일 때 토큰타입을 EQQ로 설정
			TokenType tokenType() {
				return TokenType.EQ_Q;
			}
		},
		LAMBDA { //LAMBDA일 때 토큰타입을 LAMBDA로 설정
			TokenType tokenType() {
				return TokenType.LAMBDA;
			}
		},
		NOT { //NOT일 때 토큰타입을 NOT로 설정
			TokenType tokenType() {
				return TokenType.NOT;
			}
		},
		NULL_Q { //NULLQ일 때 토큰타입을 NULLQ로 설정
			TokenType tokenType() {
				return TokenType.NULL_Q;
			}
		};
		private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType, FunctionType>();
		static {
			for (FunctionType fType : FunctionType.values()) {
				fromTokenType.put(fType.tokenType(), fType);
			}
		}

		static FunctionType getFunctionType(TokenType tType) {
			return fromTokenType.get(tType);
		}
		abstract TokenType tokenType();
		/*이부분도 binaryopNode를 참고하여 동일하게 작성한다.*/
	
	}
	public FunctionType value;
	
	public FunctionNode(TokenType tType) {
		FunctionType fType = FunctionType.getFunctionType(tType);
		value = fType;// 내용 채우기
		// TODO Auto-generated constructor stub
	}
	@Override
	/*여기도 binaryOpNode부분과 똑같이하면된다.*/
	public String toString() {
		return value.name();// 내용 채우기
	}	
}