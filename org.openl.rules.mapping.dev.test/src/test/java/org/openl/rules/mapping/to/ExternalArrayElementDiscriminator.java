package org.openl.rules.mapping.to;

public class ExternalArrayElementDiscriminator {

    public static C discriminate(A source, C[] dest) {
        for (int i = 0; i < dest.length; i++) {
            if (dest[i].getAString().equals(source.getAString())) {
                return dest[i];
            }
        }
        return null;
    }
}
