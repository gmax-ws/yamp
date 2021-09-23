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
package yamp.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamp.MsgParser;
import yamp.example.model.*;
import yamp.msg.Msg;
import yamp.msg.attachments.Attachment;
import yamp.msg.attachments.Attachments;
import yamp.msg.nameid.NameId;
import yamp.msg.recipients.Recipient;
import yamp.msg.recipients.Recipients;
import yamp.msg.root.Root;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

public class Example {

    private static final Logger logger = LoggerFactory.getLogger(Example.class);

    String jsonFileName;

    private boolean log = true;
    private boolean asJson = true;
    private boolean saveAttachments = true;
    private boolean saveBody = true;

    public Example() {
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            String jarName = new File(Example.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            System.out.printf("Usage: java -jar %s <sample.msg>\n", jarName);
        } else {
            System.out.printf("Parsing file %s", args[0]);
            new Example().parse(args[0]);
        }
    }

    private void toJson(Msg msg) {
        JsonMsg json = new JsonMsg();

        Root root = msg.getRoot();
        json.messageClass = root.getMessageClass();
        json.messageId = root.getMessageId();
        json.submitTime = json.dateString(root.getMessageSubmitTime());
        json.deliveryTime = json.dateString(root.getMessageDeliveryTime());
        json.receiptTime = json.dateString(root.getMessageReceiptTime());
        json.to = root.getDisplayTo();
        json.cc = root.getDisplayCc();
        json.bcc = root.getDisplayBcc();
        json.senderName = root.getSenderName();
        json.senderOriginalName = root.getSenderOriginalName();
        json.senderEmailAddress = root.getSenderEmailAddress();
        json.senderSmtpAddress = root.getSenderSmtpAddress();
        json.subject = root.getSubject();
        json.body = root.getBody();
        json.bodyRtf = root.getBodyRtf();
        json.bodyHtml = root.getBodyHtml();
        json.bodyRtfAsPlainText = root.getBodyRtfAsPlainText();

        json.hasAttachments = root.hasAttachments();
        json.numAttachments = root.getNumAttachments();
        json.numRecipients = root.getNumRecipients();
        json.propertyNames = root.getPropertyNames();

        if (saveBody) {
            root.saveToFile("body.txt", root.getBody());
            root.saveToFile("body_rtf.txt", root.getBodyRtfAsPlainText());
            root.saveToFile("body.html", root.getBodyHtml());
            root.saveToFile("gmax.rtf", root.getBodyRtf());
        }

        Recipients recs = msg.getRecipients();
        List<JsonRecipient> recipientsList = new LinkedList<>();

        for (Recipient rec : recs) {
            JsonRecipient jsonRecipient = new JsonRecipient();
            jsonRecipient.name = rec.getRecipientDisplayName();
            jsonRecipient.emailAddress = rec.getRecipientEmailAddress();
            jsonRecipient.smtpAddress = rec.getRecipientSmtpAddress();
            jsonRecipient.propertyNames = rec.getPropertyNames();
            jsonRecipient.folderName = rec.getRecipientFolderName();
            recipientsList.add(jsonRecipient);
        }

        json.recipients = recipientsList;

        Attachments atts = msg.getAttachments();
        json.numAttachmentsFound = atts.getNumAttachments();
        List<JsonAttachment> attachmentsList = new LinkedList<>();
        for (Attachment att : atts) {
            JsonAttachment jsonAttachment = new JsonAttachment();
            jsonAttachment.number = att.getAttachmentNumber();
            jsonAttachment.mime = att.getAttachmentMime();
            jsonAttachment.fileName = att.getAttachmentFileName();
            jsonAttachment.longFileName = att.getAttachmentLongFileName();
            jsonAttachment.size = att.getAttachmentSize();
            jsonAttachment.method = att.getAttachMethod();
            jsonAttachment.type = att.getObjectType();
            jsonAttachment.isEmbeddedMessage = att.isEmbeddedMessage();
            jsonAttachment.propertyNames = att.getPropertyNames();
            if (saveAttachments)
                att.download();
            attachmentsList.add(jsonAttachment);
        }

        json.attachments = attachmentsList;

        NameId nameId = msg.getNameId();
        json.namedProperties = new JsonNameId();
        json.namedProperties.propertyNames = nameId.getPropertyNames();

        List<JsonNamedProperty> mappings = new LinkedList<>();
        for (int i = 0; i < nameId.getNumEntries(); i++) {
            JsonNamedProperty prop = new JsonNamedProperty();
            prop.index = nameId.getPropertyIndex(i);
            prop.kind = nameId.getPropertyKind(i);
            prop.mappedName = nameId.getMappedName(i, 0x0102);
            prop.nameId = String.format("0x%04X", nameId.getPropertyNameId(i));
            prop.guid = prop.classId(nameId.getPropertyGuid(i));
            prop.name = nameId.getPropertyName(i);
            mappings.add(prop);
        }

        json.namedProperties.mappings = mappings;

        ObjectMapper objectMapper = new ObjectMapper();

        if (log) {
            try {
                logger.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
            } catch (Exception e) {
                logger.error("Fatal exception", e);
            }
        }

        if (asJson) {
            try (FileOutputStream fos = new FileOutputStream(jsonFileName)) {
                fos.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json).getBytes());
            } catch (Exception e) {
                logger.error("Fatal exception", e);
            }
        }
    }

    private void parse(String fileName) {
        jsonFileName = fileName + ".json";
        Msg msg = MsgParser.newInstance().parse(fileName);
        toJson(msg);
    }
}
