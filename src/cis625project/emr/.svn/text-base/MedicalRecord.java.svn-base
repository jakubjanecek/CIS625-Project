package cis625project.emr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import cis625project.IdGenerator;

/**
 * @author rashmi
 */
public class MedicalRecord {

  private String name;
  private int age;
  private long id = -1;// IdGenerator.getId ();
  private List<String> anamnesis = new ArrayList<String> ();

  public MedicalRecord (String name, int age) {
    this.name = name;
    this.age = age;
  }

  public void setId (long id) {
    this.id = id;
  }

  public long getId () {
    if (id == -1L) {
      id = IdGenerator.getId ();
    }
    return id;
  }

  public int getAge () {
    return age;
  }

  public String getName () {
    return name;
  }

  public void addRecord (String record) {
    if (record != null) {
      anamnesis.add (record);
    }
  }

  public Iterator<String> getRecords () {
    return anamnesis.iterator ();
  }
}
