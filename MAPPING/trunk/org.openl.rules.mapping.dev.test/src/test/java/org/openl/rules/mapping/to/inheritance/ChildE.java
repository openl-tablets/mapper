package org.openl.rules.mapping.to.inheritance;

import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.E;

public class ChildE extends E {

    private B b;
    private String base;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
