package parser.ast;

public interface ListNode extends Node {
	Node car();
	ListNode cdr();
	
	static ListNode cons(Node head, ListNode tail) {
		return new ListNode() {
			@Override
			public Node car() {
				return head;
			}

			@Override
			public ListNode cdr() {
				return tail;
			}
			@Override
			public String toString() {
				return head.toString() + tail.toString();
			}
		};
	}

	static ListNode EMPTYLIST = new ListNode() {
		public Node car() {
			return null;
		}

		@Override
		public ListNode cdr() {
			return null;
		}
		
	};
	static ListNode ENDLIST = new ListNode() {
		@Override
		public Node car() {
			return null;
		}

		@Override
		public ListNode cdr() {
			return null;
		}
	};
}
