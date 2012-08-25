package cis625project.models;

import cis625project.messaging.Message;
import cis625project.messaging.MessageType;
import cis625project.messaging.Messaging;
import cis625project.messaging.Messaging.ChannelImpl;
import gov.nasa.jpf.jvm.Verify;

/**
 * @author platinix
 */
public class MessagingModel {

  public static void main (String[] args) {
    Verify.beginAtomic ();
    Messaging.initialize (new ChannelImpl (), new ChannelImpl ());
    final Messaging m = Messaging.getInstance ();
    final Message[] m1 = {
      new Message (MessageType.ERROR, null), new Message (MessageType.ERROR, null),
      new Message (MessageType.ERROR, null), new Message (MessageType.ERROR, null),
      new Message (MessageType.ERROR, null)
    };
    final Message[] m2 = {
      new Message (MessageType.ERROR, null), new Message (MessageType.ERROR, null),
      new Message (MessageType.ERROR, null), new Message (MessageType.ERROR, null),
      new Message (MessageType.ERROR, null)
    };
    Verify.endAtomic ();
    new Thread (new Runnable () {

      public void run () {
        for (int i = 0; i < m1.length; i++) {
          m.sendToMd (m1[i]);
        }
      }
    }).start ();
    new Thread (new Runnable () {

      public void run () {
        for (int i = 0; i < m1.length; i++) {
          Message mm = m.receiveFromMd ();
          assert (m1[i].equals (mm));
        }
      }
    }).start ();
    new Thread (new Runnable () {

      public void run () {
        for (int i = 0; i < m2.length; i++) {
          m.sendToEmr (m2[i]);
        }
      }
    }).start ();
    new Thread (new Runnable () {

      public void run () {
        for (int i = 0; i < m2.length; i++) {
          Message mm = m.receiveFromEmr ();
          assert (m2[i].equals (mm));
        }
      }
    }).start ();
  }
}
