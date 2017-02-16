package java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ibarcan on 10-Feb-17.
 */
public class Streams {

    static void modifyList(List<Integer> theList) {
        theList = theList.stream()
                .filter(i -> i % 2 == 0).collect(Collectors.toList());

        // theList.add(9);  // OK
        // theList = new ArrayList<>(Arrays.asList(8, 9));  // NOK

        System.out.println(theList);
    }

    public static void testReferenceParam() {
        List<Integer> l = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        modifyList(l);
        System.out.println(l);
    }

}
