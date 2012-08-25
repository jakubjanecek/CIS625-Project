package cis625project.gui;

import cis625project.IdGenerator;
import cis625project.md.MedicalDevice;
import cis625project.messaging.Channel;
import cis625project.messaging.JmsMessage;
import cis625project.messaging.JmsMessaging;
import cis625project.messaging.Message;
import cis625project.messaging.MessageType;

/**
 * @author platinix
 */
public class SimTechnicianGui implements Gui {

  private long id = IdGenerator.getId ();

  public SimTechnicianGui () {
  }

  public void run () {
    JmsMessaging m = JmsMessaging.getInstance ();
    String chanName = m.getPrivateChannelName ();
    Channel chan = m.getPrivateChannelByName (chanName);
    MedicalDevice md = new MedicalDevice ("MedicalDeviceAddedDynamically");
    JmsMessage msg = new JmsMessage (MessageType.ADD_MEDICAL_DEVICE, md);
    msg.setCallerId (id);
    msg.setChannelName (chanName);
    m.sendToMd (msg);
    Message reply = chan.receive ();
    switch (reply.getMessageType ()) {
      case SUCCESS:
        System.out.println ("Success: MedicalDevice added successfully.");
        break;
      case ERROR:
        System.out.println ("Error: MedicalDevice not added.");
        break;
      case ACCESS_DENIED:
        System.out.println ("Access denied: adding");
        break;
      default:
    }
    chanName = m.getPrivateChannelName ();
    chan = m.getPrivateChannelByName (chanName);
    msg = new JmsMessage (MessageType.REMOVE_MEDICAL_DEVICE, md.getId ());
    msg.setCallerId (id);
    msg.setChannelName (chanName);
    m.sendToMd (msg);
    reply = chan.receive ();
    switch (reply.getMessageType ()) {
      case SUCCESS:
        System.out.println ("Success: MedicalDevice removed successfully.");
        break;
      case ERROR:
        System.out.println ("Error: MedicalDevice not removed.");
        break;
      case ACCESS_DENIED:
        System.out.println ("Access denied: removing");
        break;
      default:
    }
  }

  public long getId () {
    return id;
  }
}
