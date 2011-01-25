package org.openl.rules.mapping.to.inheritance;

import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.E;

public class ChildE extends E {

    private B b;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

}
