/*
 * Created on 28.12.2004
 */
package behaviours;

import main.Global;
import agents.CarAgent;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import gui.*;

/**
 * Das Auto ist an die Tankstelle gefahren und rechnet mit dieser jetzt ab. 
 * 
 * @author Philipp B&ouml;nisch
 */
public class FuelingBehaviour extends OneShotBehaviour
{
	/**
	 * Initialisiert die Klasse
	 * 
	 * @param a dazugeh&ouml;riger Agent
	 */
	public FuelingBehaviour(Agent a)
	{
		super(a);
	}
	
	/**
	 * Das Auto meldet sich bei der Tankstelle, das es dort eben getankt hat.
	 */
	public void action()
	{
		// Auto hält auf der Tankstelle für eine Sekunde an
		try
		{
			Thread.sleep(1500);
		}
		catch (InterruptedException e)
		{
			/// tue nichts, beeinflusst nur das Aussehen auf der Ausgabe
		}
		
		// ermittelt die benötigte Menge
		int needToFuel = ((CarAgent)myAgent).getDiff();
		
		// Sendet eine Nachricht an die Tankstelle. Der Content der Nachricht enthält
		// die getankte Menge
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(Global.ID_FUEL_REQ);
		message.setContent(String.valueOf(needToFuel));
		message.addReceiver(((CarAgent)myAgent).getPetrolAID());
		myAgent.send(message);
		
		MessagesPane.println(myAgent.getLocalName() + " hat soeben " + needToFuel + 
				" Liter bei " + ((CarAgent)myAgent).getPetrolAID().getLocalName() + " für " + 
				Global.doubleToString(((CarAgent)myAgent).getPrice()) + " pro Liter getankt.");
		
		// Auffüllen des Tankes um Einheiten
		((CarAgent) myAgent).fuel(needToFuel);
	}
}