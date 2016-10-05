package com.company;

public class Main {

    static class Animal {}
    static class Cat extends Animal {}

    static class AnimalShelter {
        public void putAnimal(Animal a) { System.out.println("Animal sheltered"); }
    }

    static class CatShelter extends AnimalShelter {
        //public static void putAnimal(Cat c) { System.out.println("Cat sheltered"); }
        public void putAnimal(Cat c) { System.out.println("Animal sheltered from Cat"); }
    }

    // Param contravariance test
    static void feedAnimal(Animal a) {}
    static void feedCat(Cat c) {}

    // Return type covariance test
    static Animal getAnimal() { return new Animal(); }
    static Cat getCat() { return new Cat(); }

    public static void main(String[] args) {
        Animal a = getCat();
        //Cat c = getAnimal();
        Cat c = getCat();

        feedAnimal(c);
        //feedCat(a);

        CatShelter cs = new CatShelter();
        cs.putAnimal(a);
        //CatShelter.putAnimal(c);
    }
}
