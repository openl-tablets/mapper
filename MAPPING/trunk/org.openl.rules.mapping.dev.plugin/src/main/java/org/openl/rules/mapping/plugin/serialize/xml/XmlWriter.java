package org.openl.rules.mapping.plugin.serialize.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.openl.rules.mapping.plugin.serialize.BeanEntry;
import org.openl.rules.mapping.plugin.serialize.FieldEntry;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.JavaClassConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlWriter {

    public void write(List<BeanEntry> beans, OutputStream out) throws IOException {

        XStream xStream = new XStream(new DomDriver());
        xStream.registerConverter(new ClassConverter());
        xStream.alias("types", BeanEntry[].class);
        xStream.alias("type", BeanEntry.class);
        xStream.useAttributeFor(BeanEntry.class, "name");
        xStream.useAttributeFor(BeanEntry.class, "extendedType");
        xStream.aliasField("extends", BeanEntry.class, "extendedType");

        xStream.alias("field", FieldEntry.class);
        xStream.useAttributeFor(FieldEntry.class, "name");
        xStream.useAttributeFor(FieldEntry.class, "type");
        xStream.useAttributeFor(FieldEntry.class, "collectionType");
        xStream.useAttributeFor(FieldEntry.class, "collectionItemType");
        xStream.aliasField("collection-type", FieldEntry.class, "collectionType");
        xStream.aliasField("collection-item-type", FieldEntry.class, "collectionItemType");

        out.write(xStream.toXML(beans.toArray(new BeanEntry[beans.size()])).getBytes());
    }
    
    private class ClassConverter extends JavaClassConverter {

        @Override
        public String toString(Object obj) {
            Class<?> clazz = (Class<?>) obj;
            if (clazz.isArray()) {
                return clazz.getCanonicalName();
            }
            
            return super.toString(obj);
        }
    }
}
