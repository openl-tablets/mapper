package org.openl.rules.mapping.performance.to.model1;

public class UniqueBaseEntity extends BaseEntity {

    private String uid;
    private EntityStatus status;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

}
