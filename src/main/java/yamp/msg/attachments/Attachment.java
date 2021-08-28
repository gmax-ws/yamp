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
package yamp.msg.attachments;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamp.MsgParser;
import yamp.msg.common.Common;
import yamp.msg.Msg;

import java.io.FileOutputStream;

import static yamp.msg.mapi.Tag.*;
import static yamp.msg.mapi.Typ.*;
import static yamp.msg.Msg.MESSAGE_RFC822;
import static yamp.msg.Msg.OFFSET_ATT;
import static yamp.msg.util.Utils.nvl;

public class Attachment extends Common {

    private static final int CHUNK_SIZE = 8192;

    private static final Logger logger = LoggerFactory.getLogger(Attachment.class);

    public Attachment(DirectoryEntry data) {
        super(data, OFFSET_ATT);
    }

    public byte[] getAttachmentDataBinary() {
        return asBytes(PidTagAttachDataBinary, PtypBinary);
    }

    public String getAttachmentFileName() {
        return asUtf16(PidTagAttachFilename, PtypString);
    }

    public String getAttachmentLongFileName() {
        return asUtf16(PidTagAttachLongFilename, PtypString);
    }

    public void download() {
        String fileName = getFileName();
        Entry doc = findStream(PidTagAttachDataBinary, PtypBinary);
        if (doc instanceof DocumentEntry) {
            try (FileOutputStream stream = new FileOutputStream(fileName);
                 DocumentInputStream dis = new DocumentInputStream((DocumentEntry) doc)) {
                byte[] chunk = new byte[CHUNK_SIZE];
                int bytesRead;
                while ((bytesRead = dis.read(chunk)) > 0) {
                    stream.write(chunk, 0, bytesRead);
                }
            } catch (Exception e) {
                logger.error("An error occurred while writing the file.", e);
            }
        }
    }

    public String getAttachmentMime() {
        return asUtf16(PidTagAttachMimeTag, PtypString);
    }

    public Integer getAttachmentSize() {
        Integer size = asInt(PidTagAttachSize, PtypInteger32);
        if (size != null) {
            return size;
        }

        Entry doc = findStream(PidTagAttachDataBinary, PtypBinary);
        return doc instanceof DocumentEntry ? ((DocumentEntry) doc).getSize() : null;
    }

    public Integer getAttachmentNumber() {
        return asInt(PidTagAttachNumber, PtypInteger32);
    }

    public Integer getObjectType() {
        return asInt(PidTagObjectType, PtypInteger32);
    }

    public Integer getAttachMethod() {
        return asInt(PidTagAttachMethod, PtypInteger32);
    }

    public boolean isEmbeddedMessage() {
        return MESSAGE_RFC822.equalsIgnoreCase(getAttachmentMime());
    }

    public Msg parseEmbeddedMessage() {
        if (isEmbeddedMessage()) {
            return MsgParser.newInstance().parse(this.getData(), true);
        } else {
            logger.warn("This attachment {} is not an embedded message ", getAttachmentLongFileName());
            return null;
        }
    }

    private String getDefaultFileName() {
        return String.format("unknown-%08d.bin", getAttachmentNumber());
    }

    private String getFileName() {
        String fileName = this.getAttachmentFileName();

        if (fileName == null) {
            fileName = this.getAttachmentLongFileName();
        }

        return nvl(fileName, getDefaultFileName());
    }
}
