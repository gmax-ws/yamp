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
package yamp.msg.common;

import org.apache.poi.hpsf.Filetime;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.util.LittleEndian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamp.msg.common.properties.Properties;
import yamp.msg.rtf.Rtf;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_16LE;
import static yamp.msg.util.Utils.ifNullBytes;
import static yamp.msg.util.Utils.ifNull;

public abstract class Common extends Properties {

    private static final Logger logger = LoggerFactory.getLogger(Common.class);

    protected Common(DirectoryEntry data, int offset) {
        super(data, offset);
    }

    protected DirectoryEntry getData() {
        return data;
    }

    protected byte[] asBytes(String tag, String typ) {
        return get(tag, typ);
    }

    protected byte[] asBytes(String streamName) {
        return get(streamName);
    }

    protected String asRtfDecompressed(byte[] bytes) {
        return ifNullBytes(bytes, Rtf::decompress);
    }

    protected String asUtf16(String tag, String typ) {
        return asUtf16(get(tag, typ));
    }

    protected String asUtf16(byte[] bytes) {
        return ifNullBytes(bytes, text -> new String(text, UTF_16LE));
    }

    protected Integer asInt(String tag, String typ) {
        return asInt(get(tag, typ));
    }

    protected Integer asInt(byte[] bytes) {
        return ifNullBytes(bytes, LittleEndian::getInt);
    }

    private boolean bytes2boolean(byte[] bytes) {
        return bytes.length != 0 && bytes[0] != 0;
    }

    protected Boolean asBoolean(String tag, String typ) {
        return ifNullBytes(get(tag, typ), this::bytes2boolean);
    }

    protected Date asDate(String tag, String typ) {
        return asDate(get(tag, typ));
    }

    protected Date asDate(byte[] bytes) {
        return ifNullBytes(bytes, date -> Filetime.filetimeToDate(LittleEndian.getLong(date)));
    }

    public void saveToFile(String fileName, String text) {
        if (text != null) {
            saveToFile(fileName, text.getBytes());
        }
    }

    public void saveToFile(String fileName, byte[] bytes) {
        try (FileOutputStream stream = new FileOutputStream(fileName)) {
            stream.write(bytes);
        } catch (Exception e) {
            logger.error("An error occurred while writing the file.", e);
        }
    }

    public Set<String> getPropertyNames() {
        return ifNull(data, DirectoryEntry::getEntryNames);
    }
}
