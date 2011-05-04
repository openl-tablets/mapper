package org.openl.rules.mapping.plugin.serialize;

import java.util.List;

/**
 * Root object in domain model which is used to encapsulate required
 * information.
 * 
 * Intended for internal use.
 */
public class XmlDocument {

    private List<BeanEntry> types;
    private List<MessageEntry> messages;

    public List<BeanEntry> getTypes() {
        return types;
    }

    public void setTypes(List<BeanEntry> types) {
        this.types = types;
    }

    public List<MessageEntry> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntry> messages) {
        this.messages = messages;
    }

}
