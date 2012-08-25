package cis625project.models;

import cis625project.md.MdManager;
import cis625project.md.MedicalDevice;
import cis625project.messaging.Channel;
import cis625project.messaging.Message;
import cis625project.messaging.MessageType;
import cis625project.messaging.Messaging;
import cis625project.messaging.Messaging.ChannelImpl;
import gov.nasa.jpf.jvm.Verify;

/**
 * @author platinix
 */
public class MedicalDeviceModel {

  public static void main (String[] args) {
    Verify.beginAtomic ();
    Channel mdChannel = new ChannelImpl ();
    Messaging.initialize (mdChannel, new ChannelImpl ());
    new Thread (new MdManager ()).start ();
    final Messaging m = Messaging.getInstance ();
    final MedicalDevice md1 = new MedicalDevice ("md1");
    Verify.endAtomic ();
    new Thread (new Runnable () {

      public void run () {
        m.sendToMd (new Message (MessageType.ADD_MEDICAL_DEVICE, md1));
      }
    }).start ();
    new Thread (new Runnable () {

      public void run () {
        final Message m1 = new Message (MessageType.START_MEDICAL_DEVICE, md1.getId ());
        final Message m2 = new Message (MessageType.STOP_MEDICAL_DEVICE, md1.getId ());
        final Channel reply = m.getPrivateChannel ();
        m1.setReplyChannel (reply);
        m.sendToMd (m1);
        for (int i = 0; i < 10; i++) {
          Message m3 = reply.receive ();
          if (m3.getMessageType () == MessageType.ERROR) {
            break;
          }
//          Verify.print (String.valueOf ((Integer) m3.getMessage ()));
        }
        m.sendToMd (m2);
      }
    }).start ();
  }
}
