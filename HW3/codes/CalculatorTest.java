import java.io.*;
import java.util.Stack;

public class CalculatorTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
//				System.out.println(e.toString());
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input) throws Exception {
		// done_TODO : trim input to distinguish with 'n'
		input = input.trim();
		for (int i = 0; i<input.length(); i++) {
			input = input.replaceAll("  ", " ");
		}
		input = input.replaceAll(" ", "n");
		input = input.replaceAll("\t", "n");
		for (int i = 0; i < input.length(); i++) {
			input = input.replaceAll("nn", "n");
		}

		// done_TODO : check for bracket balancing
		if (!bracketCheck(input)) {
			throw new Exception("Wrong Bracket");
		}

		// done_TODO : check for right infix expression
		if (!expressionCheck(input)) {
			throw new Exception("Wrong Expression");
		}

		input = input.replaceAll("n", "");

		// done_TODO : change unary from - to ~
		input = unaryCheck(input);

		// done_TODO : save infix -> postfix expression
		String postfixExp;
		try {
			postfixExp = infixToPostfix(input);
		}catch (Exception e) {
			throw new Exception("Postfix Transfer Exception");
		}

		// done_TODO : evaluate
		long result;
		try {
			result = evaluate(postfixExp);
		}catch (Exception e) {
			throw new Exception("Evaluation Exception");
		}

		System.out.println(postfixExp);
		System.out.println(result);

	}

	public static String infixToPostfix(String expression) {
		StringBuilder result = new StringBuilder();
		Stack<Character> stack = new Stack<>();

		for (int i = 0; i < expression.length(); i++) {
			char ch = expression.charAt(i);

			if (Character.isDigit(ch)) {
				// if the character is a digit, add it to the result
				result.append(ch);

				// check if the next character is also a digit, if so, continue to append it
				while (i+1 < expression.length() && Character.isDigit(expression.charAt(i+1))) {
					i++;
					result.append(expression.charAt(i));
				}

				// add a space to the result after each operand
				result.append(' ');

			} else if (ch == '(') {
				// if the character is an opening parenthesis, push it to the stack
				stack.push(ch);

			} else if (ch == ')') {
				// if the character is a closing parenthesis, pop elements from the stack and add them to the result
				// until we reach an opening parenthesis
				while (!stack.isEmpty() && stack.peek() != '(') {
					result.append(stack.pop()).append(' ');
				}
				// pop the opening parenthesis from the stack
				stack.pop();

			} else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' ) {
				// if the character is an operator, pop elements from the stack and add them to the result
				// as long as they have higher or equal precedence than the current operator
				if (stack.isEmpty()) {
					stack.push(ch);
					continue;
				}

				while (!stack.isEmpty() &&  operatorPriority(ch) <= operatorPriority(stack.peek()) ) {
					result.append(stack.pop()).append(' ');
				}
				stack.push(ch);

			} else if (ch == '~') {
				// if the character is '~', push it to the stack
				stack.push(ch);

			} else if (ch == '^') {
				while (!stack.isEmpty() && operatorPriority(ch) < operatorPriority(stack.peek())) {
					result.append(stack.pop()).append(' ');
				}
				stack.push(ch);
			}
		}

		// pop any remaining elements from the stack and add them to the result
		while (!stack.isEmpty()) {
			result.append(stack.pop()).append(' ');
		}
		String resultString = result.toString();
		resultString = resultString.trim();

		return resultString;
	}

	private static long evaluate (String input) throws Exception {
		String[] postfix_elements = input.split(" ");
		Stack<Long> stack = new Stack<>();

		for (String token : postfix_elements) {
			if (Character.isDigit(token.charAt(0))) {
				stack.push(Long.parseLong(token));
			}
			//unary operator
			else if(token.equals("~")) {
				Long operand1 = stack.pop();
				operand1 = (-1) * operand1;
				stack.push(operand1);
			}
			//binary operator
			else {
				Long operand2 = stack.pop();
				Long operand1 = stack.pop();
				long result = 0;

				switch(token) {
					case "^" : {
						if (operand2<0 && operand1==0) throw new Exception("Cannot Evaluate");
						else result = (long)Math.pow(operand1, operand2);
						break;
					}
					case "+" :
						result = operand1 + operand2;
						break;
					case "-" :
						result = operand1 - operand2;
						break;
					case "*" :
						result = operand1 * operand2;
						break;
					case "%" : {
						if (operand2 == 0) throw new Exception("Cannot Evaluate");
						else result = operand1 % operand2;
						break;
					}
					case "/" : {
						if (operand2 == 0) throw new Exception("Cannot Evalaute");
						else result = operand1 / operand2;
						break;
					}
				}
				stack.push(result);
			}
		}
		long final_result;
		try {
			final_result = stack.pop();
			if (!stack.isEmpty()) {
				throw new Exception("Wrong Evaluation Process");
			}
		} catch (Exception e){
			throw e;
		}
		return final_result;
		//return stack.pop();
	}

	private static boolean bracketCheck (String input) {
		Stack<Character> stack = new Stack<>();
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if (ch == '(') {
				stack.push(ch);
			} else if (ch == ')') {
				if (stack.isEmpty()) {
					return false;
				}
				stack.pop();
			}
		}
		return stack.isEmpty();
	}

	private static boolean expressionCheck (String input) {
		for (int i = 0; i < input.length()-1; i++) {
			char c1 = input.charAt(i);
			char c2 = input.charAt(i+1);

			if (i==0 && !(c1 == '-' || c1 == '(' || Character.isDigit(c1) )) {		//wrong start
				return false;
			}

			if (i>0 && c1 == 'n') {
				if (Character.isDigit(input.charAt(i-1)) && Character.isDigit(c2)) {		//consecutive numbers
					return false;
				}
			}

			if (c1 != 'n' && c1 != '(' && c1 != ')' && !Character.isDigit(c1)) {		//wrong operator
				if (operatorPriority(c1)== -1) {
					return false;
				}
			}
		}
		return true;
	}

	private static int operatorPriority (char input) {
		switch(input) {
			case '^' :
				return 4;
			case '~' :
				return 3;
			case '*' :
			case '%' :
			case '/' :
				return 2;
			case '+' :
			case '-' :
				return 1;
			case '(' :
				return 0;
			default :
				return -1;   // wrong operation symbol
		}
	}

	private static String unaryCheck (String input) {
		StringBuilder sb = new StringBuilder(input);
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (c == '-' && (i == 0 || sb.charAt(i-1) == '(' || sb.charAt(i-1) == '+' || sb.charAt(i-1) == '-' || sb.charAt(i-1) == '*' || sb.charAt(i-1) == '/' || sb.charAt(i-1) == '%' || sb.charAt(i-1) == '^')) {
				// found unary minus
				sb.setCharAt(i, '~');
				if (sb.charAt(i+1) == '-') {
					i++;
					while (i < sb.length() && sb.charAt(i) == '-') {
						sb.setCharAt(i, '~');
						i++;
					}
					i--;
				}
			}
		}
		return sb.toString();
	}

}
