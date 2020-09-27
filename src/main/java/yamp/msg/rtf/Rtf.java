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
package yamp.msg.rtf;

import org.apache.poi.hmef.attribute.MAPIRtfAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.sql.Types.BINARY;
import static org.apache.poi.hsmf.datatypes.MAPIProperty.RTF_COMPRESSED;

/**
 * RTF helper class
 */
public class Rtf {

    private static final Logger logger = LoggerFactory.getLogger(Rtf.class);

    /**
     * Decompress RTF bytes
     *
     * @param rtfBytes input rtfBytes
     * @return decompressed RTF as string
     */
    public static String decompress(byte[] rtfBytes) {
        try {
            return new MAPIRtfAttribute(RTF_COMPRESSED, BINARY, rtfBytes).getDataString();
        } catch (IOException e) {
            logger.error("An error occurred while decompressing RTF bytes", e);
            return null;
        }
    }

    /**
     * Convert RTF bytes to HTML (better version
     *
     * @param rtfText input rtfStream
     * @return RTF converted into HTML
     */
    public static String rtfToHtml(String rtfText) {
        if (rtfText != null) {
            rtfText = rtfText.replaceAll("\\{\\\\\\*\\\\[m]?htmltag[\\d]*(.*)}", "$1")
                    .replaceAll("\\\\htmlrtf[1]?(.*)\\\\htmlrtf0", "")
                    .replaceAll("\\\\htmlrtf[01]?", "")
                    .replaceAll("\\\\htmlbase", "")
                    .replaceAll("\\\\par", "\n")
                    .replaceAll("\\\\tab", "\t")
                    .replaceAll("\\\\line", "\n")
                    .replaceAll("\\\\page", "\n\n")
                    .replaceAll("\\\\sect", "\n\n")
                    .replaceAll("\\\\emdash", "&#2014;")
                    .replaceAll("\\\\endash", "&#2013;")
                    .replaceAll("\\\\emspace", "&#2003;")
                    .replaceAll("\\\\enspace", "&#2002;")
                    .replaceAll("\\\\qmspace", "&#2005;")
                    .replaceAll("\\\\bullet", "&#2022;")
                    .replaceAll("\\\\lquote", "&#2018;")
                    .replaceAll("\\\\rquote", "&#2019;")
                    .replaceAll("\\\\ldblquote", "&#201C;")
                    .replaceAll("\\\\rdblquote", "&#201D;")
                    .replaceAll("\\\\row", "\n")
                    .replaceAll("\\\\cell", "|")
                    .replaceAll("\\\\nestcell", "|")
                    .replaceAll("([^\\\\])\\{", "$1")
                    .replaceAll("([^\\\\])}", "$1")
                    .replaceAll("[\\\\](\\{)", "$1")
                    .replaceAll("[\\\\](})", "$1")
                    .replaceAll("\\\\u([0-9]{2,5})", "&#$1;")
                    .replaceAll("\\\\'([0-9A-Fa-f]{2})", "&#x$1;")
                    .replaceAll("\"cid:(.*)@.*\"", "\"$1\"");

            int index = rtfText.indexOf("<html");
            if (index != -1) {
                return rtfText.substring(index);
            }
        }

        return null;
    }

    /**
     * Convert RTF bytes to plain text.
     *
     * @param rtfBytes input bytes
     * @return RTF as plain text
     */
    public static String rtfToPlainText(byte[] rtfBytes) {
        try (ByteArrayInputStream rtfStream = new ByteArrayInputStream(rtfBytes)) {
            RTFEditorKit rtfParser = new RTFEditorKit();
            Document document = rtfParser.createDefaultDocument();
            rtfParser.read(rtfStream, document, 0);
            return document.getText(0, document.getLength());
        } catch (Exception e) {
            logger.error("An error occurred while converting RTF to plain text", e);
            return null;
        }
    }
}
