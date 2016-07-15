/**
 *
 */
package fr.xebia.extras.selma.beans;

/**
 * Bean with circular reference
 */
public class CircularReference {

    private String value;

    private CircularReference ref;

    public CircularReference() {

    }

    public CircularReference(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CircularReference getRef() {
        return ref;
    }

    public void setRef(CircularReference ref) {
        this.ref = ref;
    }

}
