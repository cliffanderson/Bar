package net.cliffanderson.obj;

/**
 * Created by cliff on 4/16/17.
 */
public class Person {

    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }
}
