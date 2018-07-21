package org.openl.rules.mapping.definition;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.dozer.CollectionItemDiscriminator;

public class CollectionItemDiscriminatorDescriptor {

    private String discriminatorId;
    private CollectionItemDiscriminator discriminator;

    public CollectionItemDiscriminatorDescriptor(String discriminatorId, CollectionItemDiscriminator discriminator) {
        this.discriminatorId = discriminatorId;
        this.discriminator = discriminator;
    }

    public String getDiscriminatorId() {
        return discriminatorId;
    }

    public CollectionItemDiscriminator getDiscriminator() {
        return discriminator;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("discriminatorId", discriminatorId)
            .append("discriminator", discriminator)
            .toString();
    }
}
