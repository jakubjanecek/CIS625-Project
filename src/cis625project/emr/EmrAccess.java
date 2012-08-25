package cis625project.emr;

import cis625project.messaging.Channel;
import cis625project.messaging.JmsMessage;
import cis625project.messaging.JmsMessaging;
import cis625project.messaging.Message;
import cis625project.messaging.MessageType;
import cis625project.security.AccessMatrix;
import cis625project.security.EmrRight;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author platinix
 */
public class EmrAccess implements Runnable {

  private EmrDatabase emrDatabase;
  private Message msg;
  private AccessMatrix matrix = AccessMatrix.getInstance ();
  private JmsMessaging m = JmsMessaging.getInstance ();

  public EmrAccess (Message msg, EmrDatabase emrDatabase) {
    this.emrDatabase = emrDatabase;
    this.msg = msg;
  }

  public void run () {
    long recordId;
    MedicalRecord mr;
    switch (msg.getMessageType ()) {
      case ADD_MEDICAL_RECORD:
        if (msg.getCallerId () != null && msg.getMessage () instanceof MedicalRecord) {
          if (matrix.checkRight (msg.getCallerId (), new Long (0), EmrRight.ADD)) {
            emrDatabase.addEmr ((MedicalRecord) msg.getMessage ());
            sendSuccess (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
          }
          else {
            sendAccessDenied (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
          }
        }
        else {
          sendError (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
        }
        break;
      case REMOVE_MEDICAL_RECORD:
        if (msg.getCallerId () != null && msg.getMessage () instanceof Long) {
          recordId = (Long) msg.getMessage ();
          if (matrix.checkRight (msg.getCallerId (), recordId, EmrRight.REMOVE)) {
            emrDatabase.removeEmr (recordId);
            sendSuccess (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
          }
          else {
            sendAccessDenied (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
          }
        }
        else {
          sendError (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
        }
        break;
      case GET_MEDICAL_RECORDS:
        if (msg.getCallerId () != null) {
          if (matrix.checkRight (msg.getCallerId (), new Long (0), EmrRight.LIST)) {
            Map<Long, String> msgContent = new HashMap<Long, String> ();
            List<MedicalRecord> list = emrDatabase.getEmrs ();
            for (MedicalRecord mr1 : list) {
              msgContent.put (mr1.getId (), mr1.getName ());
            }
            Message reply = new Message (MessageType.GET_MEDICAL_RECORDS_REPLY, msgContent);
            m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()).send (reply);
          }
          else {
            sendAccessDenied (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
          }
        }
        else {
          sendError (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
        }
        break;
      case GET_MEDICAL_RECORD:
        if (msg.getCallerId () != null && msg.getMessage () instanceof Long) {
          recordId = (Long) msg.getMessage ();
          if (matrix.checkRight (msg.getCallerId (), recordId, EmrRight.READ)) {
            mr = emrDatabase.getEmr (recordId);
            if (mr != null) {
              List<String> msgContent = new ArrayList<String> ();
              for (Iterator<String> it = mr.getRecords (); it.hasNext ();) {
                String item = it.next ();
                msgContent.add (item);
              }
              Message reply1 = new Message (MessageType.GET_MEDICAL_RECORD_REPLY, msgContent);
              m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()).send (reply1);
            }
            else {
              sendError (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
            }
          }
          else {
            sendAccessDenied (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
          }
        }
        else {
          sendError (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
        }
        break;
      default:
    }
  }

  private void sendSuccess (Channel replyChannel) {
    if (replyChannel != null) {
      replyChannel.send (new Message (MessageType.SUCCESS, null));
    }
  }

  private void sendError (Channel replyChannel) {
    if (replyChannel != null) {
      replyChannel.send (new Message (MessageType.ERROR, null));
    }
  }

  private void sendAccessDenied (Channel replyChannel) {
    if (replyChannel != null) {
      replyChannel.send (new Message (MessageType.ACCESS_DENIED, null));
    }
  }
}
