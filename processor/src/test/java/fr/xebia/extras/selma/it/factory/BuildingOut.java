package fr.xebia.extras.selma.it.factory;

/**
 * Bean with no public constructor.
 */
public class BuildingOut {
    private String name;
    private String street;
    private int number;
    private BuildingOut() {
    }

    public static BuildingOut create() {
        return new BuildingOut();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
