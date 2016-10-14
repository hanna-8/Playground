public class Main {

    public static void testVariance() {
        Variance.Animal a = Variance.getCat();
        //Cat c = getAnimal();
        Variance.Cat c = Variance.getCat();

        Variance.feedAnimal(c);
        //feedCat(a);

        Variance.CatShelter cs = new Variance.CatShelter();
        cs.putAnimal(a);
        //CatShelter.putAnimal(c);
    }



    public static void main(String[] args) {
        //testVariance();

        //Concurrency.test0();
        //Concurrency.sleepy();
        //Concurrency.executors();
        //Concurrency.callables();
        //Concurrency.invokeAll();
        //Concurrency.invokeAny();
        Concurrency.scheduled();
    }
}
