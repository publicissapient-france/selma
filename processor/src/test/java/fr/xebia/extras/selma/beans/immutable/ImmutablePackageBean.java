/**
 *
 */
package fr.xebia.extras.selma.beans.immutable;

/**
 * ImmutableBean in an immutable package
 */
public class ImmutablePackageBean {

    public String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
