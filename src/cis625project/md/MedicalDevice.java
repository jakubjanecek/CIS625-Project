package cis625project.md;

import cis625project.IdGenerator;
import cis625project.messaging.Channel;
import cis625project.messaging.Message;
import cis625project.messaging.MessageType;
import java.io.Serializable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author platinix
 */
public class MedicalDevice implements Runnable, Serializable {

  public static final long serialVersionUID = 3L;
  private long id = IdGenerator.getId ();
  private String name;
  private Channel reply = null;
  private boolean stop = false;

  public MedicalDevice (String name) {
    this.name = name;
  }

  public void run () {
    stop = false;
    Random r = new Random ();
    while (!stop) {
      if (reply != null) {
        Message m = new Message (MessageType.MEDICAL_DEVICE_REPLY, new Integer (80 + r.nextInt (80)));
        reply.send (m);
        try {
          Thread.sleep (300);
        }
        catch (InterruptedException ex) {
          ex.printStackTrace ();
        }
      }
    }
    reply = null;
  }

  public long getId () {
    return id;
  }

  public String getName () {
    return name;
  }

  public void setChannel (Channel ch) {
    reply = ch;
  }

  public void stop () {
    stop = true;
  }
}
