/*
 * Created on 28.12.2004
 */
package behaviours;

import gui.MessagesPane;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;

import main.Global;
import agents.CarAgent;

/**
 * Diese Verhaltensklasse repr&auml;sentiert das gezielte Anfahren einer Tankstelle.
 * 
 * @author Denis Stein, Philipp B&ouml;nisch
 */
public class DrivingToPetrolStationBehaviour extends OneShotBehaviour
{
	/**
	 * Bestimmt die maximale Wartezeit auf Antworten auf eine Preisanfrage an alle erreichbaren
	 * Tankstellen im System.
	 */
	private static final int TIMEOUT = 30;

	/**
	 * Initialisiert die Klasse.
	 * 
	 * @param a dazugeh&ouml;riger Agent
	 */
	public DrivingToPetrolStationBehaviour(Agent a)
	{
		super(a);
	}

	/**
	 * F&uuml;hrt die Aufgaben dieser Klasse aus.
	 */
	public void action()
	{
		/*
		 * Daten initialisieren
		 */
		HashMap petrols = ((CarAgent) myAgent).getReachablePetrolStationsAID();
		HashMap prices = new HashMap();
		AID lowestAID = new AID();
		double lowestPrice = 1000;

		/*
		 * Preisanfrage absenden
		 */
		// Vorbereiten einer Preisanfrage an alle erreichbaren Tankstellen
		ACLMessage message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		message.setConversationId(Global.ID_PRICE_REQ);
		java.util.Iterator iter = petrols.keySet().iterator();
		while (iter.hasNext())
		{
			// Alle erreichbaren Tankstellen als Empfänger eintragen
			message.addReceiver((AID) iter.next());
		}
		// Anfrage absenden
		myAgent.send(message);

		/*
		 * Antworten empfangen
		 */
		// Daten für den Timeout initialisieren
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal2.add(Calendar.SECOND, TIMEOUT);
		// Könnte auch mit im Switch formuliert werden, gibt dann aber Probleme mit dem Timeout
		while (true)
		{
			MessageTemplate mt = MessageTemplate.MatchConversationId(Global.ID_PRICE_ANSW);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null)
			{
				// Einzelnen Preisangebote in die Liste einfügen
				prices.put(msg.getSender(), msg.getContent());
			}

			if (petrols.size() == prices.size())
				// Es wurde von jeder gefragten Tanktstelle ein Angebot empfangen
				break;

			if (cal1.after(cal2))
				// Timeout ist erreicht -> es wird auf keine Antworten mehr gewartet
				break;
		}

		/*
		 * Antworten auswerten
		 */
		// Den günstigsten Anbieter ermitteln
		Set keys = prices.keySet();
		java.util.Iterator iter1 = keys.iterator();
		AID tmp;
		while (iter1.hasNext())
		{
			tmp = (AID) iter1.next();
			if (Double.parseDouble((String) prices.get(tmp)) < lowestPrice)
			{
				lowestPrice = Global.doubleToDouble(Double.parseDouble((String) prices.get(tmp)));
				lowestAID = tmp;
			}
		}

		// Koordinaten der günstigsten Tankstelle
		String coord = (String) petrols.get(lowestAID);

		// Das Auto fährt zu diesen Koordinaten
		((CarAgent) myAgent).driveTo(Global.xCoordinateFromString(coord), Global.yCoordinateFromString(coord));

		// AID der Tankstelle speichern, wohin das Auto gerade fährt
		((CarAgent) myAgent).setPetrolAID(lowestAID);
		// Preis dieser Tankstelle speichern
		((CarAgent) myAgent).setPrice(lowestPrice);
		
		MessagesPane.println(myAgent.getLocalName() + " fährt zu "
				+ lowestAID.getLocalName() + ".");
	}
}