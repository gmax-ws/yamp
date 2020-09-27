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
package yamp;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamp.msg.Msg;
import yamp.msg.attachments.Attachments;
import yamp.msg.nameid.NameId;
import yamp.msg.recipients.Recipients;
import yamp.msg.root.Root;

import java.io.FileInputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static yamp.msg.Msg.*;

/**
 * Outlook message parser.
 */
public class MsgParser {

    private static final Logger logger = LoggerFactory.getLogger(MsgParser.class);

    private final Map<String, DirectoryEntry> folders = new TreeMap<>();

    private MsgParser() {
    }

    private Map<String, DirectoryEntry> filterByKey(String filter) {
        return folders.entrySet().stream()
                .filter(e -> e.getKey().startsWith(filter))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Root getRoot(boolean isEmbedded) {
        return new Root(folders.get(ROOT_DIR), isEmbedded ? OFFSET_EMBED : OFFSET_MSG);
    }

    private NameId getNameId() {
        return new NameId(folders.get(NAME_ID_DIR), OFFSET_NAME);
    }

    private Attachments getAttachments() {
        return new Attachments(filterByKey(ATTACHMENT_DIR));
    }

    private Recipients getRecipients() {
        return new Recipients(filterByKey(RECIPIENT_DIR));
    }

    private void scan(DirectoryEntry dir) {
        for (Entry entry : dir) {
            if (entry instanceof DirectoryEntry) {
                folders.put(entry.getName(), (DirectoryEntry) entry);
                scan((DirectoryEntry) entry);
            }
        }
    }

    public Msg parse(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            return parse(fs.getRoot(), false);
        } catch (Exception e) {
            logger.error("An error occurred opening message {}", fileName, e);
            return null;
        }
    }

    public Msg parse(DirectoryEntry root, boolean isEmbedded) {
        folders.put(root.getName(), root);
        scan(root);
        return new Msg(getRoot(isEmbedded),
                getRecipients(),
                getAttachments(),
                getNameId());
    }

    public static MsgParser newInstance() {
        return new MsgParser();
    }
}
