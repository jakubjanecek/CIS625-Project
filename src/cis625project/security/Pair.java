package cis625project.security;

/**
 * @author platinix
 */
public class Pair {

  private Long subject;
  private Long object;

  public Pair (Long subject, Long object) {
    this.subject = subject;
    this.object = object;
  }

  public Long getObject () {
    return object;
  }

  public Long getSubject () {
    return subject;
  }

  @Override
  public boolean equals (Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Pair)) {
      return false;
    }
    final Pair other = (Pair) obj;
    if (!(this.subject == null ? other.subject == null : this.subject.equals (other.subject))) {
      return false;
    }
    if (!(this.object == null ? other.object == null : this.object.equals (other.object))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode () {
    int hash = 17;
    hash = 31 * hash + this.subject.hashCode ();
    hash = 31 * hash + this.object.hashCode ();
    return hash;
  }
}
