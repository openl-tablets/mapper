package org.openl.rules.mapping.plugin.serialize;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openl.message.OpenLErrorMessage;
import org.openl.message.OpenLMessage;
import org.openl.message.OpenLWarnMessage;
import org.openl.source.IOpenSourceCodeModule;

/**
 * Serializes {@link OpenLMessage} objects into internal domain model.
 * 
 * Intended for internal use.
 */
public class MessageSerializer {

    public static MessageEntry serialize(OpenLMessage message) {

        MessageEntry entry = new MessageEntry();
        entry.setMessage(message.getSummary());
        entry.setSeverity(message.getSeverity().toString());

        if (message instanceof OpenLErrorMessage) {
            OpenLErrorMessage errorMessage = (OpenLErrorMessage) message;
            IOpenSourceCodeModule module = errorMessage.getError().getSourceModule();

            if (module != null && StringUtils.isNotBlank(module.getUri())) {
                Map<String, String> params = getURIParams(module.getUri());
                entry.setFilename(params.get("path"));
                entry.setSheet(params.get("sheet"));
                entry.setCell(params.get("cell"));
            }
        }

        if (message instanceof OpenLWarnMessage) {
            OpenLWarnMessage warnMessage = (OpenLWarnMessage) message;
            IOpenSourceCodeModule module = warnMessage.getSource().getModule();

            if (module != null && StringUtils.isNotBlank(module.getUri())) {
                Map<String, String> params = getURIParams(module.getUri());
                entry.setFilename(params.get("path"));
                entry.setSheet(params.get("sheet"));
                entry.setCell(params.get("cell"));
            }
        }

        return entry;
    }

    public static List<MessageEntry> serialize(Collection<OpenLMessage> messages) {
        List<MessageEntry> msgs = new ArrayList<MessageEntry>(messages.size());

        for (OpenLMessage msg : messages) {
            msgs.add(serialize(msg));
        }

        return msgs;
    }

    private static Map<String, String> getURIParams(String uri) {
        Map<String, String> params = new HashMap<String, String>();

        try {
            URI u = new URI(uri);
            params.put("path", u.getPath());
            params.putAll(getQueryParams(u.getQuery()));
        } catch (URISyntaxException e) {
        }

        return params;
    }

    private static Map<String, String> getQueryParams(String query) {
        Map<String, String> params = new HashMap<String, String>();

        if (StringUtils.isNotBlank(query)) {
            for (String str : query.split("&")) {
                String[] pair = str.split("=");
                params.put(pair[0], pair[1]);
            }
        }

        return params;
    }
}
