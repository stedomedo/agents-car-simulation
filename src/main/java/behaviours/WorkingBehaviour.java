/*
 * Created on 28.12.2004
 */
package behaviours;

import agents.PetrolStationAgent;
import gui.MessagesPane;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import main.Global;

/**
 * Abstrakte Klasse, deren Aufgabe darin besteht, den Benzinpreis f&uuml;r eine
 * bestimmte Anfrage zu ermitteln und dieses an den Kunden zur&uuml;ckzuschicken.
 * In den abgeleiteten Klassen muss dazu lediglich die Methode calculatePrice()
 * und fueling() implementiert werden. S&auml;mtliche andere Funktionalitäten
 * erledigt diese Klasse selbst.
 * 
 * @author Philipp B&ouml;nisch
 */
public abstract class WorkingBehaviour extends CyclicBehaviour
{
	/**
	 * Name des Kunden, der gerade eine Preisanfrage stellt. 
	 */
	private String buyer;
	
	/**
	 * Initialisiert die Klasse
	 * 
	 * @param a zugehöriger Agent
	 */
	public WorkingBehaviour (Agent a)
	{
		super(a);
	}
	
	/**
	 * Bearbeitet die Anfragen des Kunden
	 */
	public void action()
	{
		// Anfrage des Kunden nach den Preis empfangen
		MessageTemplate mt = MessageTemplate.MatchConversationId(Global.ID_PRICE_REQ);
		ACLMessage msg = myAgent.receive(mt);
		
		if(msg != null)
		{
			// Es wurde eine Anfrage nach einem Preis gestellt, diesen ermitteln und
			// zurück senden
			buyer = msg.getSender().getLocalName();
			ACLMessage reply = msg.createReply();
			
			// Content der Nachricht enthält den Preis pro Liter
			double price = Global.doubleToDouble(calculatePrice());
			reply.setContent(String.valueOf(price));
			reply.setConversationId(Global.ID_PRICE_ANSW);
			
			myAgent.send(reply);
			
			MessagesPane.println(((PetrolStationAgent) myAgent).getCompany() + 
					"-Tankstelle " + myAgent.getLocalName() + " macht folgendes " +
					"Angebot an den Kunden " + buyer + ": " 
					+ Global.doubleToString(price) + " pro Liter.");
		}
		
		// Nachricht, das der Kunde jetzt tanken möchte
		mt = MessageTemplate.MatchConversationId(Global.ID_FUEL_REQ);
		msg = myAgent.receive(mt);
		
		if (msg != null)
		{
			// Es wurde getankt und dieser Vorgang muss verbucht werden.
			buyer = msg.getSender().getLocalName();
			// Content der empfangenen Nachricht enthält die getankte Menge
			fueling(msg.getContent());
			
			ACLMessage reply = msg.createReply();
			reply.setConversationId(Global.ID_FUEL_ANSW);
			
			myAgent.send(reply);
			
			MessagesPane.println(((PetrolStationAgent) myAgent).getCompany() + 
					"-Tankstelle " + myAgent.getLocalName() + 
					" bedankt sich für die Zahlung des Kunden " + buyer + ".");
		}
		
		else
			block();
		
		// aus Testgründen
		myAgent.doWait((long) 5000);
	}
	
	/**
	 * Hook-Methode zur Ermittlung des Preis f&uuml;r einen bestimmten Kunden unter
	 * Ber&uuml;cksichtigung seines bisherigen Kaufverhaltens und die Mitgliedschaft in eventuellen Bonus-
	 * Programmen
	 */
	public abstract double calculatePrice();
	
	/**
	 * Hook-Methode die aufgerufen wird, sobald der Kunde an der Tankstelle getankt hat. Kann dazu
	 * genutzt werden, um die Daten in den Tankstellen für eventuelle Bonusprogramme zu aktualisieren.
	 * 
	 * @param content Content der Nachricht, die der Kunde beim Tanken an die Tankstelle gesendet hat
	 */
	public abstract void fueling(String content);
	
	/**
	 * Gibt den Namen des Kunden zur&uuml;ck, der gerade eine Anfrage stellt.
	 * 
	 * @return Name des Kunden
	 */
	public String getBuyer()
	{
		return buyer;
	}
}