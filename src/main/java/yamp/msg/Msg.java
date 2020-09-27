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
package yamp.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamp.msg.attachments.Attachments;
import yamp.msg.nameid.NameId;
import yamp.msg.recipients.Recipients;
import yamp.msg.root.Root;

public class Msg {
    public static final String ROOT_DIR = "Root Entry";
    public static final String RECIPIENT_DIR = "__recip_version1.0_#";
    public static final String ATTACHMENT_DIR = "__attach_version1.0_#";
    public static final String NAME_ID_DIR = "__nameid_version1.0";
    public static final String STREAM = "__substg1.0_";
    public static final String PROPERTIES = "__properties_version1.0";
    public static final String MESSAGE_RFC822 = "message/rfc822";

    public static final int OFFSET_MSG = 32;
    public static final int OFFSET_EMBED = 24;
    public static final int OFFSET_ATT = 8;
    public static final int OFFSET_REC = 8;
    public static final int OFFSET_NAME = 0;

    private static final Logger logger = LoggerFactory.getLogger(Msg.class);

    private final Root root;
    private final Recipients recipients;
    private final Attachments attachments;
    private final NameId nameId;

    public Msg(Root root, Recipients recipients, Attachments attachments, NameId nameId) {
        this.root = root;
        this.recipients = recipients;
        this.attachments = attachments;
        this.nameId = nameId;
        validate();
    }

    public Root getRoot() {
        return root;
    }

    public Recipients getRecipients() {
        return recipients;
    }

    public Attachments getAttachments() {
        return attachments;
    }

    public NameId getNameId() {
        return nameId;
    }

    private void validate() {
        validateRecipients();
        validateAttachments();
    }

    private void validateAttachments() {
        int expected = root.getNumAttachments();
        int found = attachments.getNumAttachments();
        if (expected != found) {
            logger.error("Wrong number of attachments, expected {} found {}!", expected, found);
        }
    }

    private void validateRecipients() {
        int found = root.getNumRecipients();
        int expected = recipients.getNumRecipients();
        if (found != expected) {
            logger.error("Wrong number of recipients, expected {} found {}!", found, expected);
        }
    }
}
