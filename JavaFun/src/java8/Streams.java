package java8;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Streams {

    static void modifyList(List<Integer> theList) {
        theList = theList.stream()
                .filter(i -> i % 2 == 0).collect(Collectors.toList());

        // theList.add(9);  // OK
        // theList = new ArrayList<>(Arrays.asList(8, 9));  // NOK

        System.out.println(theList);
    }

    public static long measure(Function<void, String> f, List<String> list) {
        long startTime = System.nanoTime();

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public static void testReferenceParam() {
        List<Integer> l = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        modifyList(l);
        System.out.println(l);
    }

    public static void reducers(List<String> strings) {
        //BiFunction<String> addStrings = (s)

        // Same type accumulator
        //BinaryOperator<String> appendStrings = ;

        // Different type accumulator
        BiFunction<Integer, String, Integer> addStringLength = (len, s) -> len + s.length();

        // Combiner
        BinaryOperator<Integer> combiner = (i1, i2) -> i1 + i2;


        System.out.println(strings);

        System.out.println("S reduce ((S, S) -> S); \t\t\t"
                + Arrays.asList("a", "b", "c").stream().reduce((s1, s2) -> s1 + s2));

        System.out.println("S reduce (S, (S, S) -> S): \t\t\t"
                + Arrays.asList("a", "b", "c").stream().reduce("z", (s1, s2) -> s1 + s2));

        System.out.println("par. S reduce ((S, S) -> S): \t\t"
                + Arrays.asList("1", "2", "3").parallelStream().reduce((s1, s2) -> s1 + s2));

        System.out.println("par. S reduce (S, (S, S) -> S): \t"
                + Arrays.asList("a", "b", "c").parallelStream().reduce("z", (s1, s2) -> s1 + s2));

        System.out.println("par S reduce (I, (I, S) -> I, (I, I) -> I): \t"
                + Arrays.asList("1", "2", "3").parallelStream().reduce(0,
                (len, s1) -> len + s1.length(),
                (len1, len2) -> len1 + len2));

        System.out.println("par S reduce (I, (I, S) -> I, (I, I) -> I): \t"
                + Arrays.asList("1", "2", "3").parallelStream().reduce(1,
                (len, s1) -> len + s1.length(),
                (len1, len2) -> len1 + len2));


        try {
            Scanner s = null;
            s = new Scanner(new File("d:\\MyCode\\Playground\\JavaFun\\src\\java8\\Concurrency.java"));

            ArrayList<String> list = new ArrayList<String>();
            while (s.hasNext()){
                list.add(s.next());
            }
            s.close();

            List<String> newList = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");

            long startTime = System.nanoTime();
            String s1 = list.stream().reduce("", String::concat, String::concat);
            long endTime = System.nanoTime();
            System.out.println("1: \t\t" + (endTime - startTime));// + " " + s1);

            startTime = System.nanoTime();
            String s2 = list.parallelStream().reduce("", String::concat, String::concat);
            endTime = System.nanoTime();
            System.out.println("1 par: \t" + (endTime - startTime));// + " " + s2);

            startTime = System.nanoTime();
            String s3 = list.stream().reduce("", String::concat);
            endTime = System.nanoTime();
            System.out.println("2: \t\t" + (endTime - startTime));// + " " + s3);

            startTime = System.nanoTime();
            String s4 = list.parallelStream().reduce("", String::concat);
            endTime = System.nanoTime();
            System.out.println("2 par: \t" + (endTime - startTime));// + " " + s4);

            System.out.println(s1.equals(s2));

            startTime = System.nanoTime();
            String s5 = list.stream().reduce("0", String::concat, String::concat);
            endTime = System.nanoTime();
            System.out.println("3: \t\t" + (endTime - startTime));// + " " + s5);

            startTime = System.nanoTime();
            String s6 = list.parallelStream().reduce("0", String::concat, String::concat);
            endTime = System.nanoTime();
            System.out.println("3 par: \t" + (endTime - startTime));// + " " + s6);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
