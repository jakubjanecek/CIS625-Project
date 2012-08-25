package cis625project.models;

import cis625project.emr.EmrDatabase;
import cis625project.emr.EmrAccessManager;
import cis625project.emr.MedicalRecord;
import cis625project.messaging.Channel;
import cis625project.messaging.Message;
import cis625project.messaging.MessageType;
import cis625project.messaging.Messaging;
import cis625project.messaging.Messaging.ChannelImpl;
import gov.nasa.jpf.jvm.Verify;
import java.util.List;
import java.util.Map;

/**
 * @author platinix
 */
public class MedicalRecordModel {

  public static void main (String[] args) {
    Verify.beginAtomic ();
    Channel emrChannel = new ChannelImpl ();
    Messaging.initialize (new ChannelImpl (), emrChannel);
    final Messaging m = Messaging.getInstance ();
    EmrDatabase db = new EmrDatabase ();
    new Thread (new EmrAccessManager (db)).start ();
    final MedicalRecord mr1 = new MedicalRecord ("mr1", 22);
    final MedicalRecord mr2 = new MedicalRecord ("mr2", 23);
    final MedicalRecord mr3 = new MedicalRecord ("mr3", 24);
    Verify.endAtomic ();
    new Thread (new Runnable () {

      public void run () {
        m.sendToEmr (new Message (MessageType.ADD_MEDICAL_RECORD, mr1));
        m.sendToEmr (new Message (MessageType.ADD_MEDICAL_RECORD, mr2));
        m.sendToEmr (new Message (MessageType.ADD_MEDICAL_RECORD, mr3));
      }
    }).start ();
    new Thread (new Runnable () {

      public void run () {
        for (int i = 0; i < 3; i++) {
          Message m1 = new Message (MessageType.GET_MEDICAL_RECORDS, null);
          Channel reply = m.getPrivateChannel ();
          m1.setReplyChannel (reply);
          m.sendToEmr (m1);
          Message m2 = reply.receive ();
          @SuppressWarnings("unchecked")
          Map<Long, String> map = (Map<Long, String>) m2.getMessage ();
        }
      }
    }).start ();
    new Thread (new Runnable () {

      public void run () {
        Message m1 = new Message (MessageType.GET_MEDICAL_RECORD, mr3.getId ());
        Channel reply = m.getPrivateChannel ();
        m1.setReplyChannel (reply);
        m.sendToEmr (m1);
        Message m2 = reply.receive ();
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) m2.getMessage ();
      }
    }).start ();
    new Thread (
        new Runnable () {

          public void run () {
            m.sendToEmr (new Message (MessageType.REMOVE_MEDICAL_RECORD, mr2.getId ()));
          }
        }).start ();
  }
}
