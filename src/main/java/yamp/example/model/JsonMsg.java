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
package yamp.example.model;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static yamp.msg.util.Utils.ifNull;

public class JsonMsg {
    public String messageClass;
    public String messageId;
    public String submitTime;
    public String deliveryTime;
    public String receiptTime;
    public String senderName;
    public String senderOriginalName;
    public String senderEmailAddress;
    public String senderSmtpAddress;
    public String to;
    public String cc;
    public String bcc;
    public String subject;
    public String body;
    public String bodyRtf;
    public String bodyHtml;
    public String bodyRtfAsPlainText;
    public Boolean hasAttachments;
    public Integer numAttachments;
    public Integer numAttachmentsFound;
    public Integer numRecipients;
    public Set<String> propertyNames;
    public List<JsonRecipient> recipients;
    public List<JsonAttachment> attachments;
    public JsonNameId namedProperties;

    public String dateString(Date date) {
        return ifNull(date, Date::toString);
    }

    public String iso(Date date) {
        return ifNull(date, data -> DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
                .withZone(ZoneOffset.UTC)
                .format(Instant.ofEpochMilli(data.getTime())));
    }
}
