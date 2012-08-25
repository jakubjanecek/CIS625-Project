package cis625project.gui;

import cis625project.IdGenerator;
import cis625project.messaging.Channel;
import cis625project.messaging.JmsMessage;
import cis625project.messaging.JmsMessaging;
import cis625project.messaging.Message;
import cis625project.messaging.MessageType;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author platinix
 */
public class SimNurseGui implements Gui {

  private long id = IdGenerator.getId ();

  public SimNurseGui () {
  }

  public void run () {
    JmsMessaging m = JmsMessaging.getInstance ();
    JmsMessage msg = new JmsMessage (MessageType.GET_MEDICAL_DEVICES, null);
    String chanName = m.getPrivateChannelName ();
    msg.setCallerId (id);
    msg.setChannelName (chanName);
    m.sendToMd (msg);
    Channel chan = m.getPrivateChannelByName (chanName);
    Message reply = chan.receive ();
    switch (reply.getMessageType ()) {
      case GET_MEDICAL_DEVICES_REPLY:
        @SuppressWarnings(value = "unchecked") Map<Long, String> content = (Map<Long, String>) reply.getMessage ();
        Set<Entry<Long, String>> content1 = content.entrySet ();
        for (Entry<Long, String> entry : content1) {
          System.out.println ("ID: " + entry.getKey () + "\t\t" + entry.getValue ());
        }
        Set<Long> keySet = content.keySet ();
        for (Long mdId : keySet) {
          JmsMessage msg1 = new JmsMessage (MessageType.START_MEDICAL_DEVICE, mdId);
          msg1.setCallerId (id);
          String chan1Name = m.getPrivateChannelName ();
          Channel chan1 = m.getPrivateChannelByName (chan1Name);
          msg1.setChannelName (chan1Name);
          m.sendToMd (msg1);
          Message reply1 = chan1.receive ();
          switch (reply1.getMessageType ()) {
            case MEDICAL_DEVICE_REPLY:
              reply1 = chan1.receive ();
              System.out.println ((Integer) reply1.getMessage ());
              for (int i = 0; i < 4; i++) {
                reply1 = chan1.receive ();
                System.out.println ((Integer) reply1.getMessage ());
              }
              break;
            case ERROR:
              System.out.println ("Error: Device couldn't be started.");
              break;
            case ACCESS_DENIED:
              System.out.println ("Access denied: Device can't be started.");
              break;
            default:
          }
        }
        for (Long mdId : keySet) {
          JmsMessage msg1 = new JmsMessage (MessageType.STOP_MEDICAL_DEVICE, mdId);
          msg1.setCallerId (id);
          String chan1Name = m.getPrivateChannelName ();
          Channel chan1 = m.getPrivateChannelByName (chan1Name);
          msg1.setChannelName (chan1Name);
          m.sendToMd (msg1);
          Message reply1 = chan1.receive ();
          switch (reply1.getMessageType ()) {
            case SUCCESS:
              System.out.println ("Success: Device stopped.");
              break;
            case ERROR:
              System.out.println ("Error: Device couldn't be stopped.");
              break;
            case ACCESS_DENIED:
              System.out.println ("Access denied: Device can't be stopped.");
              break;
            default:
          }
        }
        break;
      case ERROR:
        System.out.println ("Error: Devices couldn't be listed.");
        break;
      case ACCESS_DENIED:
        System.out.println ("Access denied: Devices can't be listed.");
        break;
      default:
    }
    chanName = m.getPrivateChannelName ();
    chan = m.getPrivateChannelByName (chanName);
    msg = new JmsMessage (MessageType.GET_MEDICAL_RECORDS, null);
    msg.setCallerId (id);
    msg.setChannelName (chanName);
    m.sendToEmr (msg);
    reply = chan.receive ();
    switch (reply.getMessageType ()) {
      case GET_MEDICAL_RECORDS_REPLY:
        @SuppressWarnings("unchecked") Map<Long, String> content = (Map<Long, String>) reply.getMessage ();
        Set<Entry<Long, String>> content1 = content.entrySet ();
        for (Entry<Long, String> entry : content1) {
          System.out.println ("ID: " + entry.getKey () + "\t\t" + entry.getValue ());
        }
        break;
      case ERROR:
        System.out.println ("Error: Medical records couldn't be listed.");
        break;
      case ACCESS_DENIED:
        System.out.println ("Access denied: Medical records can't be listed.");
        break;
      default:
    }
  }

  public long getId () {
    return id;
  }
}
