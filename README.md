[![GPL V2 License](https://img.shields.io/badge/license-GPL%20(%3E%3D%202)-blue)](LICENSE) 
## yamp - Yet Another Message Parser
author: Marius Gligor  
contact: <marius.gligor@gmail.com>  

A complete Microsoft Outlook .msg file parser.  

>Example:  
```
Msg message = MsgParser.newInstance().parse("test.msg");

Root root = message.getRoot();
System.out.println(root.getMessageClass());
System.out.println(root.getMessageId());
System.out.println(root.getMessageSubmitTime());
System.out.println(root.getMessageDeliveryTime());
System.out.println(root.getMessageReceiptTime());
System.out.println(root.getDisplayTo());
System.out.println(root.getDisplayCc());
System.out.println(root.getDisplayBcc());
System.out.println(root.getSenderName());
System.out.println(root.getSenderOriginalName());
System.out.println(root.getSenderEmailAddress());
System.out.println(root.getSenderSmtpAddress());
System.out.println(root.getSubject());
System.out.println(root.getBody());
System.out.println(root.getBodyRtf());
System.out.println(root.getBodyHtml());        

Recipients recipients = message.getRecipients();

for (Recipient recipient : recipients) {
    System.out.println(recipient.getRecipientDisplayName());
    System.out.println(recipient.getRecipientEmailAddress());
    System.out.println(recipient.getRecipientSmtpAddress());
}

Attachments attachments = message.getAttachments();
System.out.println(attachments.getNumAttachments());

for (Attachment attachment : attachments) {
    System.out.println(attachment.getAttachmentNumber());
    System.out.println(attachment.getAttachmentMime());
    System.out.println(attachment.getAttachmentFileName());
    System.out.println(attachment.getAttachmentLongFileName());
    System.out.println(attachment.getAttachmentSize());
    System.out.println(attachment.getAttachMethod());
    System.out.println(attachment.getObjectType());
    System.out.println(attachment.isEmbeddedMessage());
    System.out.println(attachment.getPropertyNames());
    attachment.download();
}
```

