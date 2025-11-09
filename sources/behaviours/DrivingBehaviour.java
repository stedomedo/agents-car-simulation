/*
 * Created on 27.12.2004
 */
package behaviours;

import agents.CarAgent;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;


/**
 * Diese Verhaltensklasse repräsentiert das zufällige Umherfahren.
 * 
 * @author Denis Stein
 */
public class DrivingBehaviour extends OneShotBehaviour {
	public DrivingBehaviour(Agent a) {
		super(a);
	}
	
	/**
	 * Zufälliges Umherfahren mittels drive-Methode des AutoAgenten, 
	 * Abbruch, wenn nicht mehr genügend Sprit vorhanden (d.h. hier 
	 * Schwellwert des AutoAgenten unterschritten).
	 */
	public void action() {
		while (((CarAgent) myAgent).drive(-1)) ;
	}
}