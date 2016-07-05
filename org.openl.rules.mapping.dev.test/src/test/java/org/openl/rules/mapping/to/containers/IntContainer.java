package org.openl.rules.mapping.to.containers;

public class IntContainer {

    private int anInt;

    public IntContainer() {
    }

    public IntContainer(String string) {
        this.anInt = Integer.valueOf(string);
    }

    public int getAnInt() {
        return anInt;
    }

}
