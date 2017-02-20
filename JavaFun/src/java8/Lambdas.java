package java8;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Lambdas {

    public static void composition() {
        Function<Integer, Integer> times2 = e -> e * 2;
        Function<Integer, Integer> square = e -> e * e;

        System.out.println("times2 compose square = " + times2.compose(square).apply(4));
        System.out.println("times2 compose square = " + times2.andThen(square).apply(4));
    }


    public static void whyOnlyOneMethod() {
        // Good old style:
        Weather oldWeather = new Weather() {
                @Override
                public Boolean isSnowing() {
                    return true;
                }
        };

        // Lambdas:
        Weather newWeather = () -> true;

        System.out.println("We can build a snowman! (the sentence is " + newWeather.isSnowing() + ")");
        System.out.println("We can have a snow fight! (the sentence is " + oldWeather.isSnowing() + ")");
    }

}
