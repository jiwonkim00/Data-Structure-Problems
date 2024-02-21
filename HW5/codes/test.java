import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public class test {
    public static void main(String[] args) throws IOException {
//        LinkedList<Pair2> list = new LinkedList<>();
//        list.add(new Pair2(2, 3));
//        list.add(new Pair2(4, 2));
//        list.add(new Pair2(2, 76));
//
//        for (Pair2 pair : list) {
//            System.out.print(pair.toString(pair));
//        }
//
//        System.out.println(list.contains(new Pair2(2, 3)));
//        list.remove(new Pair2(2, 3));
//        for (Pair2 pair : list) {
//            System.out.print(pair.toString(pair));
//        }

//        BufferedWriter bfw = new BufferedWriter(new FileWriter("./test"));
//        bfw.write("this is a boy. hello, boy.");
//        bfw.newLine();
//        bfw.write("it is more important to avoid using a bad data structure.");
//        bfw.newLine();
//        bfw.write("i am a boyboy. boys be ambitious!");
//        bfw.newLine();
//        bfw.write("boyboyoboyboyboy");
//        bfw.newLine();
//        bfw.write("more important to avoid it is more important to data");
//        bfw.close();

//        String input = "boyboyoboyboyboy";
//        String substring = "boyboy";
//        int index = input.indexOf(substring);
//        while (index >= 0) {
//            System.out.println("Starting index: " + index);
//            index = input.indexOf(substring, index + 1);
//        }

        String input = "abcxxxxxxxcdf";
        String substring = "xxxxxx";
        StringBuilder sb = new StringBuilder(input);

        ArrayList<Pair> p = patternIndex(input, substring);
//        for (Pair pair : p) {
//            System.out.println(pair.toString());
//        }
        String output = removePattern(input, p);
        System.out.println(output);

    }
    public static String removePattern (String input, ArrayList<Pair> patternIndex) {
        StringBuilder str = new StringBuilder(input);
        int i =0 ;
        for (Pair pair : patternIndex) {
            str.delete(pair.first - 6*i, pair.second - 6*i);
            i++;
        }
        return str.toString();
    }

    public static ArrayList<Pair> patternIndex (String input, String pattern) {
        ArrayList<Integer> index = new ArrayList<>();
        StringBuilder str = new StringBuilder(input);
        ArrayList<Pair> result = new ArrayList<>();

        int j = input.indexOf(pattern);
        while (j >=0) {
            index.add(j);
            j = input.indexOf(pattern, j+1);
        }


        int HOLD = -1;
        for (int i = 0; i<index.size()-1; i++) {
            int curr = index.get(i);

            if (index.get(i+1) - curr > 6) {  //get rid from [HOLD] ~ [i + 5]
                if (HOLD > 0) {
                    HOLD = -1;
                    result.add(new Pair(HOLD, i+6));
                } else {
                    result.add(new Pair(i, i+6));
                    HOLD = -1;
                }
            } else {
                if (HOLD == -1) {
                    HOLD = curr;
                }
            }
        }
        int k = index.get(index.size()-1);
        if (HOLD == -1) {
            result.add(new Pair(k,k+6 ));
        }else {
            result.add(new Pair(HOLD, k+6));
        }
        return result;
    }
    public String removePattern(String input, String pattern) {
        ArrayList<Integer> patternIndex = new ArrayList<>();
        StringBuilder str = new StringBuilder(input);

        int index = 0;
        while (index >=0) {
            patternIndex.add(input.indexOf(pattern));
            index = input.indexOf(pattern, index+1);
        }

        int HOLD = -1;
        for (int i = 0; i<patternIndex.size()-1; i++) {
            int curr = patternIndex.get(i);

            if (patternIndex.get(i+1) - curr > 6) {  //get rid from [HOLD] ~ [i + 5]
                if (HOLD > 0) {
                    HOLD = -1;
                    str.delete(HOLD, i+6);
                } else {
                    HOLD = curr;
                }
            } else {
                if (HOLD == -1) {
                    HOLD = curr;
                }
            }
        }
        int c = patternIndex.size()-1;
        if (HOLD == -1) {

        }

        return new String();
    }
}
class Pair2 implements Comparable<Pair2> {
    public int first;
    public int second;
    public Pair2(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Pair2 o) {
        if (first < o.first) return -1;
        if (first > o.first) return 1;
        if (second < o.second) return -1;
        if (second > o.second) return 1;
        return 0;
    }
    public String toString(Pair2 pair) {
        String str = "(" + first + ", " + second + ")";
        return str;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null || getClass() != obj.getClass()) {
//            return false;
//        }
//        Pair2 other = (Pair2) obj;
//        return Objects.equals(first, other.first) && Objects.equals(second, other.second);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(first, second);
//    }
}
