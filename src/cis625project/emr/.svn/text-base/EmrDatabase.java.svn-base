package cis625project.emr;

import cis625project.security.AccessMatrix;
import cis625project.security.EmrRight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author rashmi
 */
public class EmrDatabase {

  private final List<MedicalRecord> database = Collections.synchronizedList (new ArrayList<MedicalRecord> ());

  public EmrDatabase () {
  }

  public void addEmr (MedicalRecord mr) {
    if (mr != null) {
      database.add (mr);
    }
  }

  public void removeEmr (long id) {
    MedicalRecord medicalRecord = getEmr (id);
    if (medicalRecord != null) {
      database.remove (medicalRecord);
    }
  }

  public MedicalRecord getEmr (long id) {
    synchronized (database) {
      for (MedicalRecord medicalRecord : database) {
        if (medicalRecord.getId () == id) {
          return medicalRecord;
        }
      }
      return null;
    }
  }

  public List<MedicalRecord> getEmrs () {
    synchronized (database) {
      return new ArrayList<MedicalRecord> (database);
    }
  }

  /**
   * This method is just for easier testing. It wouldn't be in real code!
   */
  public void addTestingRecords1 () {
    for (int i = 0; i < 10; i++) {
      MedicalRecord m = new MedicalRecord ("Patient" + (i + 1), 18 + i);
      m.addRecord (String.valueOf (i));
      addEmr (m);
    }
  }

  /**
   * This method is just for easier testing. It wouldn't be in real code!
   */
  public void addTestingRights1 (Long subject) {
    AccessMatrix matrix = AccessMatrix.getInstance ();
    matrix.enter (subject, new Long (0), EmrRight.LIST, subject);
    int i = 1;
    List<MedicalRecord> emrList = getEmrs ();
    for (MedicalRecord medicalRecord : emrList) {
      matrix.enter (subject, new Long (medicalRecord.getId ()), EmrRight.READ, subject);
      i++;
      if (i > 5) {
        break;
      }
    }
  }
}
