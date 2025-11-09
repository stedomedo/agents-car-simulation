/*
 * Created on 27.12.2004
 */
package behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.HashMap;
import java.util.Iterator;

import main.Global;
import agents.CarAgent;

import gui.*;


/**
 * Diese Verhaltensklasse repräsentiert das Suchen einer Tankstelle.
 * Es werden alle Tankstellen ermittelt, die mit dem aktuellen Tankinhalt des 
 * Autos noch erreichbar sind und für die weitere Verarbeitung im AutoAgenten 
 * deren Koordinaten gespeichert.
 * 
 * @author Denis Stein
 */
public class SearchingPetrolStationBehaviour extends OneShotBehaviour {
	/**
	 * gibt an, in welchen Zustand verzweigt werden muss (0 nach C, 
	 * -1 nach Z)
	 */
	private int exitValue;
	
	public SearchingPetrolStationBehaviour(Agent a) {
		super(a);
	}
	
	/**
	 * In dieser Methode werden alle Tankstellen vom DF abgefragt und 
	 * danach getestet, ob sie mit dem aktuellen Füllstand des Tanks des 
	 * Autos noch erreichbar sind. Ist dies der Fall, werden sie in einer 
	 * Liste gespeichert. Diese Liste wird dem AutoAgenten für die weitere
	 * Verarbeitung übergeben. Ist die Liste leer, es kann also keine 
	 * Tankstelle mehr erreicht werden, wird das Auto liegen bleiben. 
	 */
	public void action() {
		MessagesPane.println(myAgent.getLocalName() + " meint: \"Ich müsste " +
				"mal tanken..\"");
		/*
		 * Abfrage aller Tankstellen von DF.
		 * Im Fehlerfall wird der AutoAgent beendet.
		 */
		DFAgentDescription dfad = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		// Suche nur nach Tankstellen
		sd.setType(Global.SERVICE);
		dfad.addServices(sd);
		// HashMap aller erreichbarer Tankstellen, als Schlüssel dient die AID der Tankstelle,
		// als Wert werden die Koordinaten der Tankstelle gespeichert
		HashMap t = new HashMap();
		String coord;
		
		/*
		 * Finden aller Koordinaten der Tankstellen und Überprüfung, ob mit 
		 * aktuellem Tankinhalt die kürzeste Verbindung zu Tankstelle noch 
		 * möglich ist. Falls dies der Fall ist, werden die Koordinaten in 
		 * einer Liste gespeichert, die am Ende dem AutoAgenten übergeben wird.
		 * Im Fehlerfall (d.h. nicht, dass keine Tankstellen erreichbar sind!),
		 * wird der TankstellenAgent beendet.
		 */
		try {
			DFAgentDescription[] result = DFService.search(myAgent, dfad);
			for (int i = 0; i < result.length; i++)
			{
				Iterator it = result[i].getAllServices();
				while (it.hasNext())
				{
					coord = ((ServiceDescription) it.next()).getName();
					if (Global.distance(coord, (CarAgent) myAgent) <= ((CarAgent) myAgent).getFillLevel())
					{
						t.put(result[i].getName(), coord);
					}
				}
			}
		}
		catch (Exception e) {
			MessagesPane.println(myAgent.getLocalName() + " konnte keine Tankstellen finden");
			myAgent.doDelete();
		}
		
		// erreichbare Tankstellen dem Auto für weitere Bearbeitung mitteilen
		((CarAgent)myAgent).setReachablePetrolStationsAID(t);
		
		/*
		 * Nachfolgezustand abhängig, ob genügend Sprit zum Anfahren einer 
		 * Tankstelle vorhanden, d.h. wenn Liste leer, also keine Tankstellen 
		 * erreicht werden können, muss dass Auto in BrokenBehaviour übergehen, 
		 * sonst fährt es Tankstelle an.
		 */
		if (t.isEmpty()) {
			exitValue = -1;
			MessagesPane.println(myAgent.getLocalName() + " hat keine " +
					" Tankstelle in Nähe gefunden.");
		}
		else exitValue = 0;
	}
	
	public int onEnd() {
		return exitValue;
	}
}