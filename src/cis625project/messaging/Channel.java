package cis625project.messaging;

/**
 * @author platinix
 */
public interface Channel {

  public void send (Message m);

  public Message receive ();
}
