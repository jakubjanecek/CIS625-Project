package cis625project.models;

import cis625project.security.AccessMatrix;
import cis625project.security.EmrRight;
import cis625project.security.Right;
import gov.nasa.jpf.jvm.Verify;

/**
 * @author platinix
 */
public class AccessMatrixModel {

  public static void main (String[] args) {
    Verify.beginAtomic ();
    final Long s1 = new Long (1);
    final Long s2 = new Long (2);
    final Long s3 = new Long (3);
    final Long s4 = new Long (4);
    final Long o1 = new Long (5);
    final Long o2 = new Long (6);
    final Long o3 = new Long (7);
    final Long o4 = new Long (8);
    final Right r1 = EmrRight.READ;
    final Right r2 = EmrRight.ADD;
    final Right r3 = EmrRight.REMOVE;
    final AccessMatrix matrix = AccessMatrix.getInstance ();
    matrix.addTestingGrantRights (new Long (1));
    Verify.endAtomic ();
    new Thread (new Runnable () {

      public void run () {
        matrix.enter (s1, o1, r1, new Long (1));
        matrix.enter (s2, o2, r2, new Long (1));
        matrix.enter (s3, o3, r3, new Long (1));
        matrix.enter (s4, o4, r1, new Long (1));
      }
    }).start ();
    new Thread (new Runnable () {

      public void run () {
        matrix.checkRight (s4, o4, r3);
        matrix.checkRight (s1, o1, r1);
        matrix.checkRight (s2, o3, r1);
        matrix.checkRight (s2, o2, r2);
        matrix.checkRight (s4, o1, r3);
        matrix.checkRight (s3, o3, r3);
        matrix.checkRight (s4, o4, r1);
      }
    }).start ();
    new Thread (new Runnable () {

      public void run () {
        matrix.delete (s1, o1, r1, new Long (1));
        matrix.delete (s2, o2, r2, new Long (1));
        matrix.delete (s3, o3, r3, new Long (1));
        matrix.delete (s4, o4, r1, new Long (1));
      }
    }).start ();
    new Thread (new Runnable () {

      public void run () {
        matrix.destroyObject (o3, new Long (1));
        matrix.destroyObject (o4, new Long (1));
        matrix.destroySubject (s1, new Long (1));
        matrix.destroySubject (s2, new Long (1));
        matrix.destroySubject (s3, new Long (1));
        matrix.destroySubject (s4, new Long (1));
        matrix.destroyObject (o1, new Long (1));
        matrix.destroyObject (o2, new Long (1));
      }
    }).start ();
  }
}
