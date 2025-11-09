package gui;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Carmen Heger
 *
 */

/** Beschreibt das Verhalten, wenn ein Fenster geschlossen wird
 */
public class WindowClosingAdapter extends WindowAdapter
{
  private boolean exitSystem;

  /**
   * Erzeugt einen WindowClosingAdapter zum Schliessen
   * des Fensters. Ist exitSystem true, wird das komplette
   * Programm beendet.
   */
  public WindowClosingAdapter(boolean exitSystem)
  {
    this.exitSystem = exitSystem;
  }

  /**
   * Erzeugt einen WindowClosingAdapter zum Schliessen
   * des Fensters. Das Programm wird nicht beendet.
   */
  public WindowClosingAdapter()
  {
    this(false);
  }

  public void windowClosing(WindowEvent event){
	if (exitSystem){
		System.exit(0);		
	}
  }
}