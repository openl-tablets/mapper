package org.openl.rules.mapping.to;

public class CreateMethodClass {

    public static B createMethod() {
        B b = new B();
        b.setAString("b-custom");
        return b;
    }
}
