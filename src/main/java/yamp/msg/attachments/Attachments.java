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
import yamp.msg.common.AbstractContainer;

import java.util.Map;

import static yamp.msg.Msg.ATTACHMENT_DIR;

public class Attachments extends AbstractContainer<Attachment> {

    public Attachments(Map<String, DirectoryEntry> attachments) {
        super(attachments);
    }

    public int getNumAttachments() {
        return size();
    }

    public Attachment getAttachment(int index) {
        return new Attachment(get(folderName(ATTACHMENT_DIR, index)));
    }

    @Override
    public Attachment next() {
        return getAttachment(nextIndex());
    }
}
