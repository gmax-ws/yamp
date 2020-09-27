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
package yamp.msg.mapi;

/**
 * Property Tag constants
 */
public class Tag {
    public static final String PidTagNameidBucketCount = "0001";
    public static final String PidTagNameidStreamGuid = "0002";
    public static final String PidTagNameidStreamEntry = "0003";
    public static final String PidTagNameidStreamString = "0004";
    public static final String PidTagNameidBucketBase = "1000";
    public static final String PidTagItemTemporaryFlags = "1097";
    public static final String PidTagPstBestBodyProptag = "661D";
    public static final String PidTagPstIpmsubTreeDescendant = "6705";
    public static final String PidTagPstSubTreeContainer = "6772";
    public static final String PidTagLtpParentNid = "67F1";
    public static final String PidTagLtpRowId = "67F2";
    public static final String PidTagLtpRowVer = "67F3";
    public static final String PidTagPstPassword = "67FF";
    public static final String PidTagMapiFormComposeCommand = "682F";
    public static final String PidTagRecordKey = "0FF9";
    public static final String PidTagDisplayName = "3001";
    public static final String PidTagIpmSubTreeEntryId = "35E0";
    public static final String PidTagIpmWastebasketEntryId = "35E3";
    public static final String PidTagFinderEntryId = "35E7";
    public static final String PidTagContentCount = "3602";
    public static final String PidTagContentUnreadCount = "3603";
    public static final String PidTagSubfolders = "360A";
    public static final String PidTagReplItemid = "0E30";
    public static final String PidTagReplChangenum = "0E33";
    public static final String PidTagReplVersionHistory = "0E34";
    public static final String PidTagReplFlags = "0E38";
    public static final String PidTagContainerClass = "3613";
    public static final String PidTagPstHiddenCount = "6635";
    public static final String PidTagPstHiddenUnread = "6636";
    public static final String PidTagImportance = "0017";
    public static final String PidTagMessageClass = "001A";
    public static final String PidTagSensitivity = "0036";
    public static final String PidTagSubject = "0037";
    public static final String PidTagClientSubmitTime = "0039";
    public static final String PidTagSentRepresentingName = "0042";
    public static final String PidTagMessageToMe = "0057";
    public static final String PidTagMessageCcMe = "0058";
    public static final String PidTagConversationTopic = "0070";
    public static final String PidTagConversationIndex = "0071";
    public static final String PidTagDisplayCc = "0E03";
    public static final String PidTagDisplayTo = "0E04";
    public static final String PidTagDisplayBcc = "0E02";
    public static final String PidTagMessageDeliveryTime = "0E06";
    public static final String PidTagMessageFlags = "0E07";
    public static final String PidTagMessageSize = "0E08";
    public static final String PidTagMessageStatus = "0E17";
    public static final String PidTagReplCopiedfromVersionhistory = "0E3C";
    public static final String PidTagReplCopiedfromItemid = "0E3D";
    public static final String PidTagLastModificationTime = "3008";
    public static final String PidTagSecureSubmitFlags = "65C6";
    public static final String PidTagOfflineAddressBookName = "6800";
    public static final String PidTagSendOutlookRecallReport = "6803";
    public static final String PidTagOfflineAddressBookTruncatedProperties = "6805";
    public static final String PidTagViewDescriptorFlags = "7003";
    public static final String PidTagViewDescriptorLinkTo = "7004";
    public static final String PidTagViewDescriptorViewFolder = "7005";
    public static final String PidTagViewDescriptorName = "7006";
    public static final String PidTagViewDescriptorVersion = "7007";
    public static final String PidTagCreationTime = "3007";
    public static final String PidTagSearchKey = "300B";
    public static final String PidTagRecipientType = "0C15";
    public static final String PidTagResponsibility = "0E0F";
    public static final String PidTagObjectType = "0FFE";
    public static final String PidTagEntryID = "0FFF";
    public static final String PidTagAddressType = "3002";
    public static final String PidTagEmailAddress = "3003";
    public static final String PidTagDisplayType = "3900";
    public static final String PidTag7BitDisplayName = "39FF";
    public static final String PidTagSendRichInfo = "3A40";
    public static final String PidTagAttachmentSize = "0E20";
    public static final String PidTagAttachFilename = "3704";
    public static final String PidTagAttachMethod = "3705";
    public static final String PidTagRenderingPosition = "370B";
    public static final String PidTagSenderName = "0C1A";
    public static final String PidTagRead = "0E69";
    public static final String PidTagHasAttachments = "0E1B";
    public static final String PidTagBody = "1000";
    public static final String PidTagRtfCompressed = "1009";
    public static final String PidTagAttachDataBinary = "3701";
    public static final String PidTagAttachDataObject = "3701";
    public static final String PidTagOriginalDisplayTo = "0074";
    public static final String PidTagTransportMessageHeaders = "007D";
    public static final String PidTagSenderSmtpAddress = "5D01";
    public static final String PidTagSentRepresentingSmtpAddress = "5D02";
    public static final String PidTagAttachMimeTag = "370E";
    public static final String PidTagAttachExtension = "3703";
    public static final String PidTagAttachLongFilename = "3707";
    public static final String PidTagBodyHtml = "1013";
    public static final String PidTagBodyContentLocation = "1014";
    public static final String PidTagBodyContentId = "1015";
    public static final String PidTagAttachSize = "0E20";
    public static final String PidTagAttachNumber = "0E21";
    public static final String PidTagSmtpAddress = "39FE";
    public static final String PidTagOriginalSenderName = "005A";
    public static final String PidTagSentRepresentingEmailAddress = "0065";
    public static final String PidTagSenderEmailAddress = "0C1F";
    public static final String PidTagAttachContentId = "3712";
    public static final String PidTagRecipientDisplayName = "5FF6";
    public static final String PidTagRecipientEntryId = "5FF7";
    public static final String PidTagRtfInSync = "0E1F";
    public static final String PidTagInternetMessageId = "1035";
    public static final String PidTagReceiptTime = "002A";
}
