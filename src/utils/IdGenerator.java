package utils;

public class IdGenerator {
    private int counter;

    public IdGenerator(){
        this.counter = 0;
    }

    public IdGenerator(int number){
        this.counter = number;
    }

    public int generateId(){
        return counter++;
    }
}
