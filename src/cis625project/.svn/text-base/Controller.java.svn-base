package cis625project;

import cis625project.emr.EmrAccessManager;
import cis625project.emr.EmrDatabase;
import cis625project.emr.RealDatabase;
import cis625project.gui.Gui;
import cis625project.gui.GuiManager;
import cis625project.gui.GuiType;
import cis625project.md.MdManager;
import cis625project.messaging.JmsMessaging;
import cis625project.security.AccessMatrix;

/**
 * @author platinix
 */
public class Controller {

  public Controller () {
  }

  public static void main (String[] args) {
    new Controller ().start ();
  }

  public void start () {
    try {
      JmsMessaging m = JmsMessaging.getInstance ();
      EmrDatabase emrDb = new RealDatabase ();
      emrDb.addTestingRecords1 ();
      EmrAccessManager emrAccessManager = new EmrAccessManager (emrDb);
      new Thread (emrAccessManager).start ();
      MdManager mdManager = new MdManager ();
      mdManager.addTestingDevices ();
      new Thread (mdManager).start ();
      GuiManager guiManager = new GuiManager ();
      Gui nurseGui = guiManager.getGui (GuiType.NURSE);
      Gui clinicianGui = guiManager.getGui (GuiType.CLINICIAN);
      Thread t = new Thread (nurseGui);
      Thread t1 = new Thread (clinicianGui);
      t.start ();
//      t.join ();
      t1.start ();
//      t1.join ();
      nurseGui = guiManager.getGui (GuiType.NURSE);
      clinicianGui = guiManager.getGui (GuiType.CLINICIAN);
      AccessMatrix.getInstance ().addTestingGrantRights (nurseGui.getId ());
      AccessMatrix.getInstance ().addTestingGrantRights (clinicianGui.getId ());
      emrDb.addTestingRights1 (nurseGui.getId ());
      emrDb.addTestingRights1 (clinicianGui.getId ());
      mdManager.addTestingRights1 (nurseGui.getId ());
      t = new Thread (nurseGui);
      t1 = new Thread (clinicianGui);
      t.start ();
//      t.join ();
      t1.start ();
//      t1.join ();
      Gui technicianGui = guiManager.getGui (GuiType.TECHNICIAN);
      t = new Thread (technicianGui);
      t.start ();
//      t.join ();
      technicianGui = guiManager.getGui (GuiType.TECHNICIAN);
      AccessMatrix.getInstance ().addTestingGrantRights (technicianGui.getId ());
      mdManager.addTestingRights2 (technicianGui.getId ());
      t = new Thread (technicianGui);
      t.start ();
//      t.join ();
      Thread.sleep (20000); // just to be able to see the output
      System.exit (0);
    }
    catch (InterruptedException ex) {
      ex.printStackTrace ();
    }
  }
}
