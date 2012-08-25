package cis625project.md;

import cis625project.messaging.Channel;
import cis625project.messaging.JmsMessage;
import cis625project.messaging.JmsMessaging;
import cis625project.messaging.Message;
import cis625project.messaging.MessageType;
import cis625project.security.AccessMatrix;
import cis625project.security.MdRight;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author platinix
 */
public class MdManager implements Runnable {

  private final Set<MedicalDevice> devices = Collections.synchronizedSet (new HashSet<MedicalDevice> ());
  private JmsMessaging m = JmsMessaging.getInstance ();
  private ExecutorService pool = Executors.newCachedThreadPool ();
  private AccessMatrix matrix = AccessMatrix.getInstance ();

  public MdManager () {
  }

  public void run () {
    while (true) {
      pool.execute (new MessageProcessor (m.receiveFromMd ()));
    }
  }

  private void addDevice (MedicalDevice md) {
    devices.add (md);
  }

  private void removeDevice (MedicalDevice md) {
    devices.remove (md);
  }

  private Iterator<MedicalDevice> getDevices () {
    return devices.iterator ();
  }

  private class MessageProcessor implements Runnable {

    private Message msg;

    public MessageProcessor (Message msg) {
      this.msg = msg;
    }

    public void run () {
      long deviceId;
      MedicalDevice md;
      switch (msg.getMessageType ()) {
        case ADD_MEDICAL_DEVICE:
          if (msg.getCallerId () != null && msg.getMessage () instanceof MedicalDevice) {
            if (matrix.checkRight (msg.getCallerId (), new Long (0), MdRight.ADD)) {
              addDevice ((MedicalDevice) msg.getMessage ());
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
        case REMOVE_MEDICAL_DEVICE:
          if (msg.getCallerId () != null && msg.getMessage () instanceof Long) {
            deviceId = (Long) msg.getMessage ();
            if (matrix.checkRight (msg.getCallerId (), new Long (0), MdRight.REMOVE)) {
              md = getDevice (deviceId);
              if (md != null) {
                removeDevice (md);
                sendSuccess (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
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
        case GET_MEDICAL_DEVICES:
          if (msg.getCallerId () != null) {
            if (matrix.checkRight (msg.getCallerId (), new Long (0), MdRight.LIST)) {
              Map<Long, String> msgContent = new HashMap<Long, String> ();
              synchronized (devices) {
                for (Iterator<MedicalDevice> it = getDevices (); it.hasNext ();) {
                  MedicalDevice medicalDevice = it.next ();
                  msgContent.put (medicalDevice.getId (), medicalDevice.getName ());
                }
              }
              Message reply = new Message (MessageType.GET_MEDICAL_DEVICES_REPLY, msgContent);
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
        case START_MEDICAL_DEVICE:
          if (msg.getCallerId () != null && msg.getMessage () instanceof Long) {
            deviceId = (Long) msg.getMessage ();
            if (matrix.checkRight (msg.getCallerId (), deviceId, MdRight.START)) {
              md = getDevice (deviceId);
              if (md != null) {
                md.setChannel (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
                pool.execute (md);
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
        case STOP_MEDICAL_DEVICE:
          if (msg.getCallerId () != null && msg.getMessage () instanceof Long) {
            deviceId = (Long) msg.getMessage ();
            if (matrix.checkRight (msg.getCallerId (), deviceId, MdRight.STOP)) {
              md = getDevice (deviceId);
              if (md != null) {
                md.stop ();
                sendSuccess (m.getPrivateChannelByName (((JmsMessage) msg).getChannelName ()));
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

    private MedicalDevice getDevice (long id) {
      synchronized (devices) {
        for (MedicalDevice medicalDevice : devices) {
          if (medicalDevice.getId () == id) {
            return medicalDevice;
          }
        }
        return null;
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

  /**
   * This method is just for easier testing. It wouldn't be in real code!
   */
  public void addTestingDevices () {
    for (int i = 0; i < 10; i++) {
      addDevice (new MedicalDevice ("MedicalDevice" + (i + 1)));
    }
  }

  /**
   * This method is just for easier testing. It wouldn't be in real code!
   */
  public void addTestingRights1 (Long subject) {
    matrix.enter (subject, new Long (0), MdRight.LIST, subject);
    int i = 1;
    for (MedicalDevice medicalDevice : devices) {
      matrix.enter (subject, new Long (medicalDevice.getId ()), MdRight.START, subject);
      matrix.enter (subject, new Long (medicalDevice.getId ()), MdRight.STOP, subject);
      i++;
      if (i > 5) {
        break;
      }
    }
  }

  /**
   * This method is just for easier testing. It wouldn't be in real code!
   */
  public void addTestingRights2 (Long subject) {
    matrix.enter (subject, new Long (0), MdRight.ADD, subject);
    matrix.enter (subject, new Long (0), MdRight.REMOVE, subject);
  }
}

