package misc;

import java.util.Vector;

public class Variance {
    public static class Animal { public void breathe() {} }
    public static class Cat extends Animal { public void purr() {} }

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


    public static void feedAnimals(Animal[] animals) {}
    public static void feedCats(Cat[] cats) {}


    public static void feedAnimals(Vector<Animal> animals) {}
    public static void feedCats(Vector<Cat> cats) {}


    public static void testVariance() {
        Animal a = new Animal();
        Cat c = new Cat();


        // Subtypes
        a.breathe();
        c.breathe();    // a <= c


        // Return type covariance
        a = getAnimal();
        a = getCat();   // getAnimal <= getCat

        c = getCat();
        // c = getAnimal(); // getAnimal </= getCat => not contravariant


        // Param. contravatiance
        feedCat(c);
        feedAnimal(c);  // feedCat <= feedAnimal

        feedAnimal(a);
        //feedCat(a);   // feedAnimal </= feedCat => not covariant


        // Array covariance
        Animal[] animals = new Animal[4];
        Cat[] cats = new Cat[2];

        feedAnimals(animals);
        feedAnimals(cats);

        feedCats(cats);
        // feedCats(animals);   // Ka-Boom


        // Collections invariance
        Vector<Animal> animalsV = new Vector<Animal>();
        Vector<Cat> catsV = new Vector<Cat>();
        feedAnimals(animalsV);
        //feedAnimals(catsV);
        feedCats(catsV);
        //feedCats(animalsV);


        CatShelter cs = new CatShelter();
        cs.putAnimal(a);
        //CatShelter.putAnimal(c);  // Ka-Boom
    }

}
