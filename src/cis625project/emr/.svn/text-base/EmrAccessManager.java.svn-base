package cis625project.emr;

import cis625project.messaging.JmsMessaging;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author platinix
 */
public class EmrAccessManager implements Runnable {

  private JmsMessaging m = JmsMessaging.getInstance ();
  private final EmrDatabase emrDatabase;
  private ExecutorService pool = Executors.newCachedThreadPool ();

  public EmrAccessManager (EmrDatabase emrDatabase) {
    this.emrDatabase = emrDatabase;
  }

  public void run () {
    while (true) {
      pool.execute (new EmrAccess (m.receiveFromEmr (), emrDatabase));
    }
  }
}
