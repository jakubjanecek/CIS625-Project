package cis625project.gui;

/**
 * @author platinix
 */
public class GuiManager {

  public Gui getGui (GuiType type) {
    Gui g = null;
    switch (type) {
      case NURSE:
        g = new SimNurseGui ();
        break;
      case CLINICIAN:
        g = new SimClinicianGui ();
        break;
      case TECHNICIAN:
        g = new SimTechnicianGui ();
        break;
      default:
    }
    return g;
  }
}
