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
package yamp.msg.root;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import yamp.msg.common.Common;
import yamp.msg.rtf.Rtf;

import java.util.Date;

import static yamp.msg.mapi.Tag.*;
import static yamp.msg.mapi.Typ.*;
import static yamp.msg.util.Utils.ifNull;

public class Root extends Common {

    public Root(DirectoryEntry data, int offset) {
        super(data, offset);
    }

    public Date getMessageDeliveryTime() {
        return asDate(PidTagMessageDeliveryTime, PtypTime);
    }

    public Date getMessageSubmitTime() {
        return asDate(PidTagClientSubmitTime, PtypTime);
    }

    public Date getMessageReceiptTime() {
        return asDate(PidTagReceiptTime, PtypTime);
    }

    public String getMessageClass() {
        return asUtf16(PidTagMessageClass, PtypString);
    }

    public String getMessageId() {
        return asUtf16(PidTagInternetMessageId, PtypString);
    }

    public String getBody() {
        return asUtf16(PidTagBody, PtypString);
    }

    public String getBodyRtf() {
        return asRtfDecompressed(get(PidTagRtfCompressed, PtypBinary));
    }

    public String getBodyHtml() {
        byte[] bytes = get(PidTagBodyHtml, PtypString);
        return bytes == null ? Rtf.rtfToHtml(getBodyRtf()) : asUtf16(bytes);
    }

    public String getBodyRtfAsPlainText() {
        String rtf = getBodyRtf();
        return ifNull(rtf, data -> Rtf.rtfToPlainText(data.getBytes()));
    }

    public String getSubject() {
        return asUtf16(PidTagSubject, PtypString);
    }

    public String getDisplayTo() {
        return asUtf16(PidTagDisplayTo, PtypString);
    }

    public String getDisplayCc() {
        return asUtf16(PidTagDisplayCc, PtypString);
    }

    public String getDisplayBcc() {
        return asUtf16(PidTagDisplayBcc, PtypString);
    }

    public String getSenderName() {
        return asUtf16(PidTagSenderName, PtypString);
    }

    public String getSenderOriginalName() {
        return asUtf16(PidTagOriginalSenderName, PtypString);
    }

    public String getSenderEmailAddress() {
        return asUtf16(PidTagSenderEmailAddress, PtypString);
    }

    public String getSenderSmtpAddress() {
        return asUtf16(PidTagSenderSmtpAddress, PtypString);
    }

    public Boolean hasAttachments() {
        return asBoolean(PidTagHasAttachments, PtypBoolean);
    }

    public Integer getNumAttachments() {
        return header().getNumAttachments();
    }

    public Integer getNumRecipients() {
        return header().getNumRecipients();
    }
}
