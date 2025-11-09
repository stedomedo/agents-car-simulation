/*
 * Created on 29.12.2004
 */
package behaviours;

import java.util.Random;

import jade.core.Agent;

/**
 * Diese Klasse ermittelt f&uuml;r eine konkrete Anfrage den Preis, den der Kunde an dieser Tankstelle zu
 * bezahlen hat und &uuml;bermittelt diesen an den Kunden.
 * Die Preise ergeben sich dabei zuf&auml;llig und schwanken um den Wert von 1,50 mit plus/minus 0,00...0,30.
 * Die eigentliche Arbeit wird allerdings in der Klasse WorkingBehavior geleistet, hier ist lediglich
 * die Ermittlung des Preises und die Aktualisierung der Kundendaten implementiert. 
 * 
 * @see behaviours.WorkingBehaviour
 * @author Philipp B&ouml;nisch
 */
public class WorkingRandomBehaviour extends WorkingBehaviour
{
	/**
	 * Liefert Zufallszahlen für die Kalkulation des Preises.
	 */
	private static Random rand;

	/**
	 * Initilisiert die Klasse.
	 * 
	 * @param a dazugeh&ouml;riger Agent
	 */
	public WorkingRandomBehaviour (Agent a)
	{
		super(a);
		
		rand = new Random();
	}
	
	/**
	 * Der Preis pro Liter wird zuf&auml;llig ermittelt. Dieser kann zwischen 1,20 und 1,80 schwanken.
	 * 
	 * @see behaviours.WorkingBehaviour#calculatePrice()
	 */
	public double calculatePrice()
	{
		double offset = ((double)rand.nextInt(31)) / 100;
		
		if (rand.nextBoolean())
			return 1.5 + offset;
		else
			return 1.5 - offset;
	}

	/**
	 * Aktualisierung der Verwaltungsdaten. In diesem Fall nicht n&ouml;tig.
	 * 
	 * @see behaviours.WorkingBehaviour#fueling(java.lang.String)
	 */
	public void fueling(String content)
	{
		// es gibt nichts zu tun
	}
}