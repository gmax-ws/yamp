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
package yamp.msg.nameid;

import org.apache.poi.hpsf.ClassID;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.util.LittleEndian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamp.msg.common.Common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;

import static yamp.msg.mapi.Tag.*;
import static yamp.msg.mapi.Typ.PtypBinary;
import static yamp.msg.Msg.STREAM;
import static yamp.msg.util.Utils.copyBytes;

/**
 * Named properties.
 */
public class NameId extends Common {

    private static final Logger logger = LoggerFactory.getLogger(NameId.class);

    public static final int NUMERICAL_NAMED_PROPERTY = 0;
    public static final int STRING_NAMED_PROPERTY = 1;
    public static final int BASE_STREAM_ID = 0x1000;
    public static final int MIN_ID = 0x8000;
    public static final int MAX_ID = 0xFFFE;
    public static final int MIN_GUID = 3;

    static class NamedPropertyData {
        List<ClassID> guids = new LinkedList<>();
        List<NameIdEntry> entries = new ArrayList<>();
        byte[] string;
    }

    private final NamedPropertyData npd = new NamedPropertyData();

    public NameId(DirectoryEntry data, int offset) {
        super(data, offset);
        this.init();
    }

    private void init() {
        // GUID's
        byte[] data = asBytes(PidTagNameidStreamGuid, PtypBinary);
        if (data != null) {
            for (int i = 0; i < data.length - 16 + 1; i += 16) {
                ClassID uuid = new ClassID(copyBytes(data, i, i + 16), 0);
                npd.guids.add(uuid);
            }
        }
        // Entries
        byte[] entries = asBytes(PidTagNameidStreamEntry, PtypBinary);
        if (entries != null) {
            for (int i = 0; i < entries.length - 8 + 1; i += 8) {
                byte[] bytes = copyBytes(entries, i, i + 8);
                npd.entries.add(new NameIdEntry(LittleEndian.getInt(bytes),
                        LittleEndian.getShort(bytes, 4),
                        LittleEndian.getShort(bytes, 6)));
            }
        }
        // String
        npd.string = asBytes(PidTagNameidStreamString, PtypBinary);
    }

    private NameIdEntry entry(int index) {
        return npd.entries.get(index); // - MIN_ID);
    }

    private ClassID guid(int index) {
        int pos = index - MIN_GUID;
        if (pos >= 0) {
            return npd.guids.get(pos);
        }

        logger.error("Invalid GUID index={} Must be greater or equals to {}", index, MIN_GUID);
        return null;
    }

    private boolean isNumerical(NameIdEntry ent) {
        return ent.getKind() == NUMERICAL_NAMED_PROPERTY;
    }

    private String compute(NameIdEntry ent, int typ) {
        long name = isNumerical(ent) ? ent.getNameId() : crc32(this.name(ent.getNameId()));
        long streamId = BASE_STREAM_ID + (name ^ (ent.getGuidIndexOrig())) % 0x1F;
        return String.format("%s%04X%04X", STREAM, streamId, typ);
    }

    private long crc32(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }

    private byte[] name(int offset) {
        int beg = offset + 4;
        int size = asInt(copyBytes(npd.string, offset, beg));
        return copyBytes(npd.string, beg, beg + size);
    }

    public String getMappedName(int index, int typ) {
        return compute(entry(index), typ);
    }

    public String getPropertyName(int index) {
        NameIdEntry ent = entry(index);

        switch (ent.getKind()) {
            case NUMERICAL_NAMED_PROPERTY:
                return String.format("%04X", ent.getNameId());
            case STRING_NAMED_PROPERTY:
                return asUtf16(name(ent.getNameId()));
            default:
                return null;
        }
    }

    public ClassID getPropertyGuid(int index) {
        NameIdEntry ent = entry(index);
        return guid(ent.getGuidIndex());
    }

    public int getPropertyIndex(int index) {
        NameIdEntry ent = entry(index);
        return ent.getPropIndex();
    }

    public int getPropertyNameId(int index) {
        NameIdEntry ent = entry(index);
        return ent.getNameId();
    }

    public int getPropertyKind(int index) {
        NameIdEntry ent = entry(index);
        return ent.getKind();
    }

    public byte[] getData(int index, int typ) {
        String propertyTag = getMappedName(index, typ);
        return asBytes(propertyTag);
    }

    public byte[] getData(String propertyTag) {
        return asBytes(propertyTag);
    }

    public int getNumEntries() {
        return npd.entries.size();
    }
}
