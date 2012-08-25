package cis625project.messaging;

/**
 * @author platinix
 */
public class JmsMessage extends Message {

  public static final long serialVersionUID = 2L;
  private String channelName = null;

  public JmsMessage (MessageType messageType, Object message) {
    super (messageType, message);
  }

  public String getChannelName () {
    return channelName;
  }

  public void setChannelName (String channelName) {
    this.channelName = channelName;
  }
}
