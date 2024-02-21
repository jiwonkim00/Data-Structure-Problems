import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Wrong Input";
  
    // implement this
    static String regex_ex = "([+-]?[0-9]{1,100})([+*-])([+-]?[0-9]{1,100})";
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile(regex_ex);

    //Features of BigInteger
    public boolean minus;
    public int[] number_part;
  
    public BigInteger(boolean minus, int[] num1) {  //store already reversed numbers
        this.minus = minus;
        number_part = Arrays.copyOfRange(num1, 0, num1.length);
    }
  
    public BigInteger(String s) {   //store already reversed numbers
        minus = s.contains("-");

        if (minus || s.charAt(s.length()-1) == '+') {
            number_part = new int[s.length()-1];
            for (int i = 0; i< s.length()-1; i++) {
                number_part[i] = Integer.parseInt(s.charAt(i)+"");
            }
        }
        else {
            number_part = new int[s.length()];
            for (int i = 0; i<s.length(); i++) {
                number_part[i] = Integer.parseInt(s.charAt(i)+"");
            }
        }
    }

    @Override
    public String toString() { //need to reverse to produce the correct number
        StringBuilder st = new StringBuilder();

        for (int digit : number_part) {
            st.append(digit);
        }
        if (minus) {
            st.append("-");
        }
        return st.reverse().toString();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static int first_bigger(BigInteger num1, BigInteger num2) {  // 1: num1>num2, -1: num2>num1, 0: num1==num2
        if (num1.number_part.length > num2.number_part.length) {
            return 1;
        }
        else if (num2.number_part.length > num1.number_part.length) {
            return -1;
        }
        else {
            for (int i = num1.number_part.length-1; i>=0; i--) {
                if (num1.number_part[i] > num2.number_part[i]) return 1;
                else if (num2.number_part[i] > num1.number_part[i]) return -1;
            }
            return 0;
        }
    }

    // + : (+A) + (+B) / (-A) + (-B)
    // - : (+A) - (-B) / (-A) - (+B)
    public static BigInteger add(BigInteger big, BigInteger small) {  //has only case where the two boo-ho are the same
        int abs_compare = first_bigger(big, small);
        if (abs_compare == -1) {    //SWAP
            BigInteger temp = big;
            big = small;
            small = temp;
        }

        int carry = 0;
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i <= small.number_part.length-1) {
            int added = big.number_part[i] + small.number_part[i] + carry;
            int digit = (added >= 10)? added-10 : added;
            result.append(digit);
            carry = (added >= 10)? 1 : 0;
            i++;
        }
        while (i <= big.number_part.length-1) {
            int added = big.number_part[i] + carry;
            int digit = (added >= 10)? added-10 : added;
            result.append(digit);
            carry = (added >= 10)? 1 : 0;
            i++;
        }
        if (carry == 1) {
            result.append(1);
        }

        if (big.minus) {
            result.append("-");
        }

        return new BigInteger(result.toString());
    }

    // + : (+A) + (-B) / (-A) + (+B)
    // - : (+A) - (+B) / (-A) - (-B)
    public static BigInteger subtract(BigInteger big, BigInteger small) {   //only + (no minus numbers)
        int abs_compare = first_bigger(big, small);
        if (abs_compare == 0) {
            return new BigInteger("0");
        }
        boolean result_minus = false;
        if (abs_compare == -1) {        //result minus & swap
            result_minus = true;
            BigInteger temp = big;
            big = small;
            small = temp;
        }

        // do absolute value subtraction
        StringBuilder result = new StringBuilder();
        int carry = 0;
        int i = 0;

        while (i <= small.number_part.length-1) {
            if (big.number_part[i] - carry < small.number_part[i]) {
                result.append(10 + big.number_part[i] - carry - small.number_part[i]);
                carry = 1;
            }
            else {
                result.append(big.number_part[i] - carry - small.number_part[i]);
                carry = 0;
            }
            i++;
        }
        while (i <= big.number_part.length-1) {
            int sub = big.number_part[i] - carry;
            int digit = (sub >=0) ? sub : 10 - (-1*sub);
            if (digit == 0 & i == big.number_part.length-1) { break; }
            result.append(digit);
            carry = (sub >= 0) ? 0: 1;
            i++;
        }
        i--;
        while (result.charAt(i) == '0') {   // removing 0 at the front
            result.deleteCharAt(i);
            i--;
        }

        if (result_minus) {
            result.append("-");
        }
        return new BigInteger(result.toString());
    }
  
    public static BigInteger multiply(BigInteger big, BigInteger small) {

        if ((big.number_part.length==1 & big.number_part[0]==0) || (small.number_part.length==1 & small.number_part[0]==0)) {
            return new BigInteger("0");
        }

        int abs_compare = first_bigger(big, small);
        if (abs_compare == -1) {    //SWAP
            BigInteger temp = big;
            big = small;
            small = temp;
        }

        //determine boo-ho first
        boolean result_minus = big.minus != small.minus;

        //do the multiply by matrix
        int[][] matrix = new int[big.number_part.length * 2][small.number_part.length];

        int c = 0;
        //int[row][column], i = column
        while (c <= small.number_part.length-1) {
            int r = 0;
            int carry = 0;
            while (r <= big.number_part.length-1) {
                matrix[r+c][c] = (big.number_part[r] * small.number_part[c] + carry) % 10;
                carry = (big.number_part[r] * small.number_part[c] + carry) / 10 ;
                r++;
            }
            if (carry > 0) {
                matrix[r+c][c] = carry;
            }
            c++;

        }

//        //test print
//        for (int j =0; j< small.number_part.length; j++) {
//            for (int i = 0; i< big.number_part.length*2; i++) {
//                System.out.print(matrix[i][j]);
//            }
//            System.out.println();
//        }

        //add up the matrix to get the result
        int carry = 0;
        StringBuilder result = new StringBuilder();
        for (int r = 0; r < big.number_part.length * 2; r++) {
            int added = 0;
            for (int col = 0; col < small.number_part.length ; col++) {
                added += matrix[r][col];
            }
            added += carry;
            result.append(added % 10);
            carry = added / 10;
        }



        int trim = result.length()-1;
        while (result.charAt(trim) == '0') {   // removing 0 at the front
            result.deleteCharAt(trim);
            trim--;
        }

        if (result_minus) {
            result.append("-");
        }
        return new BigInteger(result.toString());
    }
  

  
    static BigInteger evaluate(String input) throws IllegalArgumentException {

        input = input.replaceAll("\\s+", "");  //get rid of white space
        StringBuilder num1 = null, num2= null;
        String operator = "";

        Matcher matcher = EXPRESSION_PATTERN.matcher(input);
        if (matcher.find()) {
            num1 = new StringBuilder(matcher.group(1));
            operator = matcher.group(2);
            num2 = new StringBuilder(matcher.group(3));
        }
        BigInteger big = new BigInteger(num1.reverse().toString());
        BigInteger small = new BigInteger(num2.reverse().toString());


        //////////////////////////////////////////////////////

        switch (operator) {
            case "*" : {
                return multiply(big, small);
            }
            case "+" : {
                if (big.minus == small.minus) {
                    return add(big, small);
                } else {  //operand1.minus != operand2.minus
                    if (big.minus) {
                        return subtract(small, new BigInteger(false, big.number_part));
                    } else {  //small.minus
                        return subtract(big, new BigInteger(false, small.number_part));
                    }
                }
            }
            case "-" : {
                if (big.minus == small.minus) {
                    if (big.minus) {
                        return subtract(new BigInteger(false, small.number_part), new BigInteger(false, big.number_part));
                    } else {
                        return subtract(big, small);
                    }

                } else {  //operand1.minus != operand2.minus
                    if (big.minus) {
                        return add(big, new BigInteger(true, small.number_part));
                    } else {  //small.minus
                        return add(big, new BigInteger(false, small.number_part));
                    }
                }
            }
            default : throw new IllegalStateException("Unexpected value: " + operator);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) throws Exception {

        try (InputStreamReader isr = new InputStreamReader(System.in)) {

            try (BufferedReader reader = new BufferedReader(isr)) {

                boolean done = false;
                while (!done) {

                    String input = reader.readLine();

                    try {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e) {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }

    static boolean processInput(String input) throws IllegalArgumentException {
        boolean quit = isQuitCmd(input);

        if (quit) {
            return true;
        }
        else {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());

            return false;
        }
    }

    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    } //if 'quit'-> return true
}
