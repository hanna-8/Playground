package java8;

public class Lambdas {

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
