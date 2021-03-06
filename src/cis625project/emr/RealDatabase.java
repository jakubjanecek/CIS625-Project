package cis625project.emr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author platinix
 */
public class RealDatabase extends EmrDatabase {

  private static final String driver = "com.mysql.jdbc.Driver";
  private static final String dbUrl = "jdbc:mysql://localhost:3306/cis625";
  private static final String user = "root";
  private static final String pass = "admin";
  private static Connection connection = null;

  static {
    try {
      Class.forName (driver);
      connection = DriverManager.getConnection (dbUrl, user, pass);
    }
    catch (SQLException ex) {
      ex.printStackTrace ();
    }
    catch (ClassNotFoundException ex) {
      ex.printStackTrace ();
    }
  }

  public RealDatabase () {
  }

  @Override
  public void addEmr (MedicalRecord mr) {
    if (mr != null) {
      try {
        PreparedStatement statement = connection.prepareStatement ("INSERT INTO cis625.MedicalRecord VALUES (?, ?, ?)");
        statement.setLong (1, mr.getId ());
        statement.setString (2, mr.getName ());
        statement.setInt (3, mr.getAge ());
        int result = statement.executeUpdate ();
        for (Iterator<String> it = mr.getRecords (); it.hasNext ();) {
          String item = it.next ();
          statement = connection.prepareStatement ("INSERT INTO cis625.Anamnesis VALUES (NULL, ?, ?)");
          statement.setLong (1, mr.getId ());
          statement.setString (2, item);
          statement.executeUpdate ();
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace ();
      }
    }
  }

  @Override
  public void removeEmr (long id) {
    try {
      PreparedStatement statement = connection.prepareStatement ("DELETE FROM cis625.MedicalRecord WHERE id = ?");
      statement.setLong (1, 1);
      int result = statement.executeUpdate ();
      statement = connection.prepareStatement ("DELETE FROM cis625.Anamnesis WHERE mdId = ?");
      statement.setLong (1, id);
      statement.executeUpdate ();
    }
    catch (SQLException ex) {
      ex.printStackTrace ();
    }
  }

  @Override
  public MedicalRecord getEmr (long id) {
    try {
      PreparedStatement statement = connection.prepareStatement ("SELECT * FROM cis625.MedicalRecord WHERE id = ?");
      statement.setLong (1, id);
      ResultSet result = statement.executeQuery ();
      result.first ();
      MedicalRecord md = new MedicalRecord (result.getString (2), result.getInt (3));
      md.setId (result.getLong (1));
      statement = connection.prepareStatement ("SELECT * FROM cis625.Anamnesis WHERE mdId = ?");
      statement.setLong (1, id);
      result = statement.executeQuery ();
      while (result.next ()) {
        md.addRecord (result.getString (3));
      }
      return md;
    }
    catch (SQLException ex) {
      ex.printStackTrace ();
    }
    return null;
  }

  @Override
  public List<MedicalRecord> getEmrs () {
    List<MedicalRecord> list = new ArrayList<MedicalRecord> ();
    try {
      PreparedStatement statement = connection.prepareStatement ("SELECT * FROM cis625.MedicalRecord");
      ResultSet result = statement.executeQuery ();
      while (result.next ()) {
        MedicalRecord md = new MedicalRecord (result.getString (2), result.getInt (3));
        md.setId (result.getLong (1));
        PreparedStatement statement1 = connection.prepareStatement ("SELECT * FROM cis625.Anamnesis WHERE mdId = ?");
        statement1.setLong (1, result.getLong (1));
        ResultSet result1 = statement1.executeQuery ();
        while (result1.next ()) {
          md.addRecord (result1.getString (3));
        }
        list.add (md);
      }
    }
    catch (SQLException ex) {
      ex.printStackTrace ();
    }
    return list;
  }
}
