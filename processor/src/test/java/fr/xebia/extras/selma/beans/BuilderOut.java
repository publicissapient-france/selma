package fr.xebia.extras.selma.beans;

/**
 * Bean used to test Builder-style properties.
 */
public class BuilderOut extends AbstractBuilderOut<BuilderOut> {
    private int intVal;

    public int getIntVal() {
        return intVal;
    }

    public BuilderOut setIntVal(int intVal) {
        this.intVal = intVal;
        return this;
    }
}
