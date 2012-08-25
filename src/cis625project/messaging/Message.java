package cis625project.messaging;

import java.io.Serializable;

/**
 * @author platinix
 */
public class Message implements Serializable {

  public static final long serialVersionUID = 1L;
  private MessageType messageType;
  private Object message;
  private Long callerId = null;
  private Channel replyChannel = null;

  public Message (MessageType messageType, Object message) {
    this.messageType = messageType;
    this.message = message;
  }

  public Object getMessage () {
    return message;
  }

  public MessageType getMessageType () {
    return messageType;
  }

  public Long getCallerId () {
    return callerId;
  }

  public void setCallerId (Long callerId) {
    this.callerId = callerId;
  }

  public Channel getReplyChannel () {
    return replyChannel;
  }

  public void setReplyChannel (Channel replyChannel) {
    this.replyChannel = replyChannel;
  }
}
