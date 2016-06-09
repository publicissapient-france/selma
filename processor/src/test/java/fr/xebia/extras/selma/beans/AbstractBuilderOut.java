package fr.xebia.extras.selma.beans;

/**
 * Bean used to test Builder-style properties.
 */
public abstract class AbstractBuilderOut<T extends AbstractBuilderOut<T>>{
    private String str;

    public String getStr() {
        return str;
    }

    /**
     * Should work with inherited Builder properties.
     * @param str
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setStr(String str) {
        this.str = str;
        return (T) this;
    }
}
