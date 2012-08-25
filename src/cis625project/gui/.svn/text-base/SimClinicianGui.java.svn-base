package cis625project.gui;

import cis625project.IdGenerator;
import cis625project.messaging.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author platinix
 */
public class SimClinicianGui implements Gui {

  private long id = IdGenerator.getId ();

  public SimClinicianGui () {
  }

  public void run () {
    JmsMessaging m = JmsMessaging.getInstance ();
    String chanName = m.getPrivateChannelName ();
    Channel chan = m.getPrivateChannelByName (chanName);
    JmsMessage msg = new JmsMessage (MessageType.GET_MEDICAL_RECORDS, null);
    msg.setCallerId (id);
    msg.setChannelName (chanName);
    m.sendToEmr (msg);
    Message reply = chan.receive ();
    switch (reply.getMessageType ()) {
      case GET_MEDICAL_RECORDS_REPLY:
        @SuppressWarnings("unchecked") Map<Long, String> content = (Map<Long, String>) reply.getMessage ();
        Set<Entry<Long, String>> content1 = content.entrySet ();
        for (Entry<Long, String> entry : content1) {
          System.out.println ("ID: " + entry.getKey () + "\t\t" + entry.getValue ());
        }
        Set<Long> keySet = content.keySet ();
        for (Long emrId : keySet) {
          JmsMessage msg1 = new JmsMessage (MessageType.GET_MEDICAL_RECORD, emrId);
          msg1.setCallerId (id);
          String chan1Name = m.getPrivateChannelName ();
          Channel chan1 = m.getPrivateChannelByName (chan1Name);
          msg1.setChannelName (chan1Name);
          m.sendToEmr (msg1);
          Message reply1 = chan1.receive ();
          switch (reply1.getMessageType ()) {
            case GET_MEDICAL_RECORD_REPLY:
              @SuppressWarnings("unchecked") List<String> record = (List<String>) reply1.getMessage ();
              System.out.println ("Medical Record:");
              for (String r : record) {
                System.out.println (r);
              }
              break;
            case ERROR:
              System.out.println ("Error: Medical record couldn't be received.");
              break;
            case ACCESS_DENIED:
              System.out.println ("Access denied: Medical record can't be received.");
              break;
            default:
          }
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
