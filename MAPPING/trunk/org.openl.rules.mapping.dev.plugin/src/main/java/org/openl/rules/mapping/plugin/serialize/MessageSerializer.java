package org.openl.rules.mapping.plugin.serialize;

import java.util.ArrayList;
import java.util.List;

import org.openl.message.OpenLMessage;

public class MessageSerializer {

    public static MessageEntry serialize(OpenLMessage message) {

        MessageEntry entry = new MessageEntry();
        entry.setMessage(message.getSummary());

//        if (message instanceof OpenLErrorMessage) {
//            OpenLErrorMessage errorMessage = (OpenLErrorMessage) message;
//            // etc processing
//        }

        return entry;
    }

    public static List<MessageEntry> serialize(List<OpenLMessage> messages) {
        List<MessageEntry> msgs = new ArrayList<MessageEntry>(messages.size());

        for (OpenLMessage msg : messages) {
            msgs.add(serialize(msg));
        }

        return msgs;
    }
}
