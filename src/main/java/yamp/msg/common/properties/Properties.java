/*
 * Outlook .MSG file parser
 * Copyright (c) 2020 Scalable Solutions
 *
 * author: Marius Gligor <marius.gligor@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111, USA.
 */
package yamp.msg.common.properties;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.util.LittleEndian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static yamp.msg.Msg.PROPERTIES;
import static yamp.msg.Msg.STREAM;
import static yamp.msg.util.Utils.copyBytes;

public class Properties {

    private static final Logger logger = LoggerFactory.getLogger(Properties.class);
    protected final DirectoryEntry data;
    private final Map<String, Property> properties = new HashMap<>();
    private Set<String> entries = new TreeSet<>();
    private Header header;

    public Properties(DirectoryEntry data, int offset) {
        this.data = data;

        if (data == null) {
            logger.warn("Null value for directory data!");
        }

        makeProperties(offset);
    }

    public Header header() {
        return header;
    }

    public byte[] get(String tag, String typ) {
        return get(propertyTag(tag, typ));
    }

    public Entry findStream(String tag, String typ) {
        return this.findStream(makePropertyStream(propertyTag(tag, typ)));
    }

    public Entry findStream(String propertyTag) {
        if (data != null) {
            try {
                return data.getEntry(propertyTag);
            } catch (FileNotFoundException e) {
                logger.debug("Cannot find {} in {}", propertyTag, data.getName());
            }
        }
        return null;
    }

    public byte[] get(String propertyTag) {
        String streamName = makePropertyStream(propertyTag);

        if (entries.contains(streamName)) {
            Entry entry = findStream(streamName);
            if (entry instanceof DocumentEntry) {
                return read((DocumentEntry) entry);
            }
        } else {
            Property prop = properties.get(propertyTag);
            if (prop != null) {
                return prop.value;
            }
        }

        return null;
    }

    private byte[] getProperties() {
        if (data != null) {
            this.entries = data.getEntryNames();

            Entry entry = findStream(PROPERTIES);
            if (entry instanceof DocumentEntry) {
                return read((DocumentEntry) entry);
            }
        }

        return null;
    }

    private void makeProperties(int offset) {
        byte[] props = getProperties();

        if (props != null) {
            this.header = new Header(copyBytes(props, 0, offset));
            for (int i = offset; i < props.length; i += 16) {
                Property prop = new Property();
                prop.tag = String.format("%02X%02X%02X%02X",
                        props[i + 3], props[i + 2], props[i + 1], props[i]);
                prop.flags = LittleEndian.getInt(props, i + 4);
                prop.value = copyBytes(props, i + 8, i + 16);
                properties.put(prop.tag, prop);
            }
        }
    }

    private byte[] read(DocumentEntry doc) {
        if (doc != null) {
            try (DocumentInputStream stm = new DocumentInputStream(doc)) {
                byte[] bytes = new byte[doc.getSize()];
                stm.readFully(bytes);
                return bytes;
            } catch (Exception e) {
                logger.error("An error occurred while reading document", e);
            }
        }

        return null;
    }

    private String makePropertyStream(String propertyTag) {
        return String.format("%s%s", STREAM, propertyTag);
    }

    private String propertyTag(String tag, String typ) {
        return String.format("%s%s", tag, typ);
    }
}
