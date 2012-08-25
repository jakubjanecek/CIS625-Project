package cis625project.security;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author platinix
 */
public class AccessMatrix {

  private static final AccessMatrix instance = new AccessMatrix ();
  private Map<Pair, Set<Right>> matrix = new ConcurrentHashMap<Pair, Set<Right>> ();

  private AccessMatrix () {
  }

  public static AccessMatrix getInstance () {
    return instance;
  }

  public boolean checkRight (Long subject, Long object, Right right) {
    Pair p = new Pair (subject, object);
    Set<Right> rights = matrix.get (p);
    if (rights == null || !rights.contains (right)) {
      return false;
    }
    return true;
  }

  public void enter (Long subject, Long object, Right right, Long who) {
    if (checkRight (who, new Long (0), MatrixRight.GRANT)) {
      Pair p = new Pair (subject, object);
      Set<Right> rights = matrix.get (p);
      if (rights == null) {
        rights = new HashSet<Right> ();
        rights.add (right);
        matrix.put (p, rights);
      }
      else {
        rights.add (right);
      }
    }
  }

  public void delete (Long subject, Long object, Right right, Long who) {
    if (checkRight (who, new Long (0), MatrixRight.REVOKE)) {
      Pair p = new Pair (subject, object);
      Set<Right> rights = matrix.get (p);
      if (rights != null && rights.contains (right)) {
        rights.remove (right);
      }
    }
  }

  public void destroySubject (Long subject, Long who) {
    if (checkRight (who, new Long (0), MatrixRight.DESTROY)) {
      Set<Pair> keys = matrix.keySet ();
      for (Pair pair : keys) {
        if (pair.getSubject ().equals (subject)) {
          matrix.remove (pair);
        }
      }
    }
  }

  public void destroyObject (Long object, Long who) {
    if (checkRight (who, new Long (0), MatrixRight.DESTROY)) {
      Set<Pair> keys = matrix.keySet ();
      for (Pair pair : keys) {
        if (pair.getObject ().equals (object)) {
          matrix.remove (pair);
        }
      }
    }
  }

  public void addTestingGrantRights (Long subject) {
    HashSet<Right> rights = new HashSet<Right> ();
    rights.add (MatrixRight.GRANT);
    rights.add (MatrixRight.REVOKE);
    rights.add (MatrixRight.DESTROY);
    matrix.put (new Pair (subject, new Long (0)), rights);
  }
}
