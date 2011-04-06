package org.openl.rules.mapping.to;

public class ExternalMapper {

    public void map(A src, C dest) {
        
        dest.setAString(src.getAString());
        
        B b = new B();
        b.setAString(src.getE().getAString());
        b.setAnInteger(src.getE().getD().getAnInt());
        
        dest.setB(b);
    }
}
