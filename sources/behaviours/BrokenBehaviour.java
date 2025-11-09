/*
 * Created on 27.12.2004
 */
package behaviours;

import gui.MessagesPane;
import agents.CarAgent;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;


/**
 * Diese Verhaltensklasse wird dann erreicht, wenn keine Tankstelle mehr
 * erreichbar ist. Es wird der restliche Sprit verfahren und und anschließend 
 * bleibt das Auto auf Grund von Spritmangel liegen. Der AutoAgent wird 
 * gelöscht.
 * 
 * @author Denis Stein
 */
public class BrokenBehaviour extends OneShotBehaviour {
	public BrokenBehaviour(Agent a) {
		super(a);
	}
	
	/**
	 * Es wird der restliche Sprit verfahren und anschließend bleibt das 
	 * Auto liegen, der AutoAgent wird beendet.
	 */
	public void action() {
		// restlichen Sprit verbrauchen.
		while (((CarAgent) myAgent).getFillLevel() > 0)
			((CarAgent) myAgent).drive(-1);
		MessagesPane.println(myAgent.getLocalName() + " ist auf Grund von " +
				"Spritmangel liegen geblieben.");
		myAgent.doDelete();
	}
}