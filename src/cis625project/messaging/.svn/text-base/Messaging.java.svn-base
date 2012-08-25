package cis625project.messaging;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author platinix
 */
public class Messaging {

  private static Messaging instance;
  private static AtomicBoolean initialized = new AtomicBoolean (false);
  private Channel mdChannel;
  private Channel emrChannel;

  private Messaging (Channel mdChannel, Channel emrChannel) {
    this.mdChannel = mdChannel;
    this.emrChannel = emrChannel;
  }

  public static void initialize (Channel mdChannel, Channel emrChannel) {
    if (initialized.get ()) {
      throw new IllegalStateException ();
    }
    else {
      instance = new Messaging (mdChannel, emrChannel);
      initialized.set (true);
    }
  }

  public static Messaging getInstance () {
    if (!initialized.get ()) {
      throw new IllegalStateException ();
    }
    return instance;
  }

  public void sendToMd (Message m) {
    mdChannel.send (m);
  }

  public Message receiveFromMd () {
    return mdChannel.receive ();
  }

  public void sendToEmr (Message m) {
    emrChannel.send (m);
  }

  public Message receiveFromEmr () {
    return emrChannel.receive ();
  }

  public Channel getPrivateChannel () {
    return new ChannelImpl ();
  }


  public static class ChannelImpl implements Channel {

    public static final int CAPACITY = 20;
    private BlockingQueue<Message> queue = new LinkedBlockingQueue<Message> (CAPACITY);

    public ChannelImpl () {
    }

    public void send (Message m) {
      try {
        queue.put (m);
      }
      catch (InterruptedException ex) {
        ex.printStackTrace ();

      }
    }

    public Message receive () {
      try {
        return queue.take ();
      }
      catch (InterruptedException ex) {
        ex.printStackTrace ();
      }
      return null;
    }
  }
}
