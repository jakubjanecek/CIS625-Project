package cis625project;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author platinix
 */
public class IdGenerator {

  private static final AtomicLong counter = new AtomicLong (1);

  private IdGenerator () {
  }

  public static long getId () {
    return counter.getAndIncrement ();
  }
}
