public class Variance {
    public static class Animal {}
    public static class Cat extends Animal {}

    public static class AnimalShelter {
        public void putAnimal(Animal a) { System.out.println("Animal sheltered"); }
    }

    public static class CatShelter extends AnimalShelter {
        //public static void putAnimal(Cat c) { System.out.println("Cat sheltered"); }
        public void putAnimal(Cat c) { System.out.println("Animal sheltered from Cat"); }
    }


    // Param contravariance test
    public static void feedAnimal(Animal a) {}
    public static void feedCat(Cat c) {}

    // Return type covariance test
    public static Animal getAnimal() { return new Animal(); }
    public static Cat getCat() { return new Cat(); }

}
