/*
 * Created on 27.12.2004
 */
package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.util.leap.LinkedList;

import java.util.HashMap;
import java.util.Random;

import behaviours.BrokenBehaviour;
import behaviours.DrivingBehaviour;
import behaviours.DrivingToPetrolStationBehaviour;
import behaviours.FuelingBehaviour;
import behaviours.SearchingPetrolStationBehaviour;

import gui.*;

/**
 * Diese Klasse kapselt den Agenten und das Verhalten eines Autos.
 * Das Verhalten ist durch einen endlichen Zustandsautomaten 
 * repr&auml;sentiert. Das Auto bewegt sich selbst&auml;ndig auf dem Gitternetz 
 * der Ausmaße 10x10 Knoten und versucht bei Erreichen eines Schwellwertes 
 * eine Tankstelle aufzusuchen, um aufzutanken, anderenfalls bleibt es 
 * liegen und der Agent wird beendet.
 * 
 * @author Denis Stein, Philipp B&ouml;nisch
 */
public class CarAgent extends Agent {
	
	/**
	 * Tankstelle, an die das Auto f&auml;hrt, um an dieser zu tanken.
	 */
	private AID petrolAID;
	
	/**
	 * Speichert den Preis der Tankstelle, wohin das Auto zum Tanken f&auml;hrt.
	 */
	private double price;
	
	/**
	 * Speichert alle erreichbaren Tankstellen ab. Als Schl&uuml;ssel dient die AID der Tankstelle,
	 * der zweite Eintrag sind die Koordinaten der Tankstelle.
	 */
	private HashMap reachablePetrolStationsAID; 
	
	/**
	 * aktueller Tankinhalt
	 */
	private int fillLevel = 30;
	
	/**
	 * maximaler Tankinhalt
	 */
	private final int maxFillLevel = 35;
	
	/**
	 * Schwellwert für Tankstellensuche
	 */
	private final int threshold = 9;
	
	/**
	 * aktuelle Position in X-Richtung (zwischen 0 und 9)
	 */
	private int actualXPosition;
	
	/**
	 * aktuelle Position in Y-Richtung (zwischen 0 und 9)
	 */
	private int actualYPosition;

	/**	
	 * merkt sich letzte Richtungsänderung
	 */
	private int lastCourse = -1;
	
	/**
	 * Liste der erreichbaren Tankstellen
	 */
	private LinkedList reachablePetrolStations;
	
	/**
	 * Startzustand: zufällig umherfahrend
	 */
	private final String STATE_A = "driving";
	
	/**
	 * Zustand: auf Tankstellensuche
	 */
	private final String STATE_B = "searchingPetrolStation";
	
	/**
	 * Zustand: Erfragen der Preise, Ermittlung des günstigsten 
	 * Angebots und Fahrt zur Tankstelle
	 */
	private final String STATE_C = "drivingToPetrolStation";
	
	/**
	 * Zustand: Tanken und Bezahlen an Tankstelle
	 */
	private final String STATE_D = "fueling";
	
	/**
	 * Endzustand: fahruntauglich auf Grund von Spritmangel
	 */
	private final String STATE_Z = "broken";
	
	/**
	 * In dieser Funktion werden die Argumente dem Agenten zugewiesen und 
	 * der endliche Zustandsautomat registriert.
	 */
	public void setup() {
		/*
		 * Einlesen der Argumente und Zuweisung an den AutoAgenten
		 */
		Object[] args = this.getArguments();
		actualXPosition = Integer.parseInt(String.valueOf(args[0]));
		actualYPosition = Integer.parseInt(String.valueOf(args[1]));		
		
		/*
		 * Anlegen des endlichen Zustandsautomaten
		 */
		FSMBehaviour fsm = new FSMBehaviour(this);
		
		// Registrieren des Zustands A als Startzustand
		fsm.registerFirstState(new DrivingBehaviour(this), STATE_A);
		
		// Registrieren des Zustands B
		fsm.registerState(new SearchingPetrolStationBehaviour(this), STATE_B);
		
		// Registrieren des Zustands C
		fsm.registerState(new DrivingToPetrolStationBehaviour(this), STATE_C);
		
		// Registrieren des Zustands D
		fsm.registerState(new FuelingBehaviour(this), STATE_D);
		
		// Registrieren des Zustands Z als Endzustand
		fsm.registerLastState(new BrokenBehaviour(this), STATE_Z);
		
		/*
		 * Registrieren der Zustandsübergänge
		 */ 
		fsm.registerDefaultTransition(STATE_A, STATE_B);
		// bei nächsten beiden Zustandsübergang situationsabhängig 
		fsm.registerTransition(STATE_B, STATE_C, 0);
		fsm.registerTransition(STATE_B, STATE_Z, -1);
		fsm.registerDefaultTransition(STATE_C, STATE_D);
		fsm.registerDefaultTransition(STATE_D, STATE_A);
		
		// Verhalten dem Agenten zuweisen
		this.addBehaviour(fsm);
	}
	
	/**
	 * Ermöglicht das Fahren des Autos, wobei unsinniges Fahren unterbunden 
	 * wird.
	 * 
	 * @param course Richtungsanweisung (0 vorwärts, 1 rechts, 2 rückwärts,
	 * 			3 links, -1 zufällig
	 * @return Schwellwert noch nicht erreicht?
	 */
	public boolean drive(int course) {		
		/*
		 * Wenn -1 übergeben, dann zufällige Routenwahl, sonst Richtungsangabe 
		 * ausführen. Ist die aktuelle Richtung nicht möglich (Randknoten oder 
		 * entgegen letzter Richtung), verharrt das Auto an seinem Punkt, 
		 * sonst wird zum entsprechenden Nachbarknoten gefahren und der 
		 * Füllstand des Wagens dekrementiert.
		 * Liefert zurück, ob Schwellwert noch nicht erreicht.
		 */
		
		// zufällige Richtung ermitteln
		if (course == -1) {
			course = ((Random) new Random()).nextInt() % 4;
			if (course < 0) course = -course;
		}

		/*
		 * Wenn entgegen der letzten Richtung, dann in diesem Durchlauf 
		 * keine Bewegung und Rückgabe von true, da kein Sprit verbraucht.
		 */
		if ((lastCourse == 0 && course == 2) ||
				(lastCourse == 1 && course == 3) ||
				(lastCourse == 2 && course == 0) ||
				(lastCourse == 3 && course == 1))
			return true;
		
		/*
		 * Simulation des Fahrens und Verbrauch von Sprit.
		 */
		switch(course) {
			case 0: // vorwärts
				if (this.actualYPosition < 9) {
					this.actualYPosition++;
					this.fillLevel--;
					break;
				}
				else return true;
			case 1: // rechts
				if (this.actualXPosition < 9) {
					this.actualXPosition++;
					this.fillLevel--;
					break;
				}
				else return true;
			case 2: // zurück
				if (this.actualYPosition > 0) {
					this.actualYPosition--;
					this.fillLevel--;
					break;
				}
				else return true;
			case 3: // links
				if (this.actualXPosition > 0) {
					this.actualXPosition--;
					this.fillLevel--;
					break;
				}
				else return true;
		}
		// Merken der letzten Richtungsänderung
		lastCourse = course;
		// Pause von 0.5 Sekunden
		this.doWait((long) 500);
		
		MapPane.getInstance().drawCar(course,actualXPosition,actualYPosition);
		InfoPane.getInstance().setTank(this.getLocalName(),fillLevel);
		
		/*
		 * Wenn Schwellwert erreicht oder unterschritten, Rückgabe von 
		 * false, um in anderen Zustand (SearchingPetrolStation) zu 
		 * verzweigen.
		 */
		if (this.fillLevel <= this.threshold) return false;
		else return true;
	}
	
	/**
	 * Ermöglicht das gezielte Anfahren eines Punktes (x,y).
	 * 
	 * @param x x-Koordinate
	 * @param y y-Koordinate
	 */
	public void driveTo(int x, int y) {
		/*
		 * Berechnung der Differenz in x- und y-Richtung
		 */
		// Differenz in x-Richtung
		int diffX = this.actualXPosition - x;
		// Differenz in y-Richtung
		int diffY = this.actualYPosition - y;
		// aktuell einzuschlagende Richtungsänderung (0..3)
		int course;
		
		/*
		 * Zuerst Berechnung Ausrichtung in x-Richtung, dann y-Richtung.
		 */
		if (diffX >= 0) course = 3;
		else {
			course = 1;
			diffX = -diffX;
		}
		for (int i = 0; i < diffX; i++)
			drive(course);
		
		if (diffY >= 0) course = 2;
		else {
			course = 0;
			diffY = -diffY;
		}
		for (int i = 0; i < diffY; i++)
			drive(course);
	}		
	
	/**
	 * Simuliert das Auftanken des Wagens um übergebene Anzahl Einheiten.
	 * @param units aufgetankte Einheiten
	 */
	public void fuel(int units) {
		this.fillLevel += units;
		InfoPane.getInstance().setTank(this.getLocalName(),fillLevel);
	}
	
	/**
	 * Liefert aktuelle x-Koordinate
	 * @return aktuelle x-Koordinate
	 */
	public int getActualXPosition() {
		return actualXPosition;
	}
	
	/**
	 * Liefert aktuelle y-Koordinate
	 * @return aktuelle y-Koordinate
	 */
	public int getActualYPosition() {
		return actualYPosition;
	}
	
	/**
	 * Liefert Füllstand des Wagens.
	 * @return Füllstand des Wagens
	 */
	public int getFillLevel() {
		return fillLevel;
	}

	/**
	 * &Uuml;bermittelt alle erreicbaren Tankstellen. Als Schl&uuml;ssel soll die AID der
	 * Tankstelle verwendet werden, als Wert-Eintrag die Koordinaten der Tankstelle.
	 * 
	 * @param t Liste mit ensprechenden Eintr&auml;gen
	 */
	public void setReachablePetrolStationsAID(HashMap t)
	{
		reachablePetrolStationsAID = t;
	}
	
	/**
	 * Enth&auml;lt die erreichbaren Tankstellen. Als Schl&uuml;ssel sind die AID der Tankstellen
	 * verwendet worden, der Wert enth&auml;lt die Koordinaten der Tankstellen. 
	 * 
	 * @return alle erreichbaren Tankstellen
	 */
	public HashMap getReachablePetrolStationsAID()
	{
		return reachablePetrolStationsAID;
	}
	
	/**
	 * Ermittelt den &quot;Leerstand&quot; im Tank des Autos
	 * 
	 * @return Fehlende Menge an Benzin im Tank
	 */
	public int getDiff()
	{
		return maxFillLevel - fillLevel;
	}
	
	/**
	 * Zum Setzen der Tankstelle, die gerade angefahren werden soll zum Tanken.
	 * 
	 * @param paid AID der Tankstelle
	 */
	public void setPetrolAID(AID paid)
	{
		petrolAID = paid;
	}
	
	/**
	 * Gibt die Tankstelle zur&uuml;ck, die von dem Auto gerade angefahren wird, um dort zu tanken.
	 * 
	 * @return AID der anzufahrenden Tankstelle
	 */
	public AID getPetrolAID()
	{
		return petrolAID;
	}
	
	/**
	 * Liefert den Preis zur&uuml;ck, f&uuml;r den das Auto getankt hat.
	 * 
	 * @return bezahlter Preis
	 */
	public double getPrice()
	{
		return price;
	}
	
	/**
	 * Setzt den Preis der g&uuml;nstigsten Tankstelle
	 * 
	 * @param price g&uuml;nstigste Preis
	 */
	public void setPrice(double price)
	{
		this.price = price;
	}
}