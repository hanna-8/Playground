package java8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class Streams {


    public static long measure(List<String> list, Function<List<String>, String> f) {
        long startTime = System.nanoTime();
        String s = f.apply(list);
        //System.out.println(s);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    public static void reducers(List<String> strings) {

        try {
            Scanner s = null;
            s = new Scanner(new File("d:\\MyCode\\Playground\\JavaFun\\src\\java8\\Concurrency.java"));

            ArrayList<String> list = new ArrayList<String>();
            while (s.hasNext()){
                list.add(s.next());
            }
            s.close();


            //List<String> newList = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
            ArrayList<String> clonedList = new ArrayList<>(list);


            for(int i = 0; i < 100; ++i) list.addAll(clonedList);

            while(1 == 1) {
                System.out.println("c: " + measure(list, (l) -> l.stream().reduce("", String::concat, String::concat)));
                System.out.println("r: " + measure(list, (l) -> l.stream().reduce("", String::concat)));
            }

//            System.out.println(measure(list,       (l) -> l.stream().reduce("",
//                    (s1, s2) -> {
//                        //System.out.println("r: " + s1 + " " + s2);
//                        return s1.concat(s2);
//                    },
//                    (s1, s2) -> {
//                        System.out.println("c: " + s1 + " " + s2);
//                        return s1.concat(s2);
//                    })));

//            System.out.println(measure(list, (l) -> l.parallelStream().reduce("", String::concat, String::concat)));
//            System.out.println(measure(clonedList, (l) -> l.parallelStream().reduce("", String::concat)));
//            System.out.println(measure(list, (l) -> l.parallelStream().reduce("0", String::concat, String::concat)));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
