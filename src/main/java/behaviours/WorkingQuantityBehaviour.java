/*
 * Created on 29.12.2004
 */
package behaviours;

import java.util.HashMap;

import agents.PetrolStationAgent;
import jade.core.Agent;

/**
 * Diese Klasse ermittelt f&uuml;r eine konkrete Anfrage den Preis, den der Kunde an dieser Tankstelle zu
 * bezahlen hat und &uulm;bermittelt diesen an den Kunden.
 * Der Preis ergibt sich dabei aus der bereits gekauften Menge des Kunde. Umso mehr dieser in der 
 * Vergangenheit bereits bei einer Tankstelle dieser Firma sein Benzin gekauft hat, desto g&uuml;nstiger
 * (bis zu einer bestimmten Minimalgrenze) wird es f&uuml;r den Kunden.
 * Die eigentliche Arbeit wird allerdings in der Klasse WorkingBehavior geleistet, hier ist lediglich
 * die Ermittlung des Preises und die Aktualisierung der Verwaltungsdaten implementiert. 
 * 
 * @see behaviours.WorkingBehaviour
 * @author Philipp B&ouml;nisch
 */
public class WorkingQuantityBehaviour extends WorkingBehaviour
{
	/**
	 * Initialisiert die Klasse.
	 * 
	 * @param a dazugeh&ouml;riger Agent
	 */
	public WorkingQuantityBehaviour(Agent a)
	{
		super(a);
	}

	/**
	 * Ermittelt, wieviel der Kunde bereits gekauft hat und berechnet anhand dessen den Preis
	 * f&uuml;r den Kunden. Desto mehr dieser bereits gekauft hat, desto g&uuml;nstiger wird es für den
	 * Kunden.
	 * Wenn der Kunde noch nichts gekauft hat, dann betr&auml;gt der Preis pro Liter 1,60. Alle 10 Liter
	 * sinkt der Preis um 0,01 bis zu einem minimalen Wert von 1,20.
	 * 
	 * @see behaviours.WorkingBehaviour#calculatePrice()
	 */
	public double calculatePrice()
	{
		// sämtliche Kundendaten aller Tankstellen
		HashMap data = ((PetrolStationAgent)myAgent).getSales();
		// nur die Kundendaten der Tankstellen dieser Firma
		HashMap buyers = (HashMap)(data.get(((PetrolStationAgent)myAgent).getCompany()));
		
		if (buyers == null)
		{
			// Es existiert noch kein Eintrag für Tankstellen dieser Firma
			buyers = new HashMap();
			data.put(((PetrolStationAgent)myAgent).getCompany(), buyers);
		}
		
		// bisherige Anzhal der getankten Liter des Kunden
		Integer liter = (Integer)buyers.get(super.getBuyer());
		
		if (liter != null)
		{
			// Der Kunde hat bereits an einer Tankstelle dieses Unternehmens getankt
			double ret = 1.65 - (0.01 * (liter.intValue() / 10));
			if (ret < 1.2)
				// Preislimit ist unterschritten
				return 1.2;
			else
				return ret;
		}
		else
			// Der Kunde hat bis jetzt noch nicht an dieser Tankstelle getankt
			return 1.65;
	}

	/**
	 * Verwaltungsdaten werden aktualisiert. D.h. es wird eingetragen, wieviel Liter
	 * der Kunde an dieser Tankstelle getankt hat.
	 * 
	 * @see behaviours.WorkingBehaviour#fueling(java.lang.String)
	 * @param content Anzahl der getankten Liter
	 */
	public void fueling(String content)
	{
		int value;
		
		// Kundendaten dieser Tnkstelle
		HashMap buyers = (HashMap)((PetrolStationAgent)myAgent).getSales().get(((PetrolStationAgent)myAgent).getCompany());
		
		// bisherig getankte Liter des Kunden
		Integer liter = (Integer)buyers.get(super.getBuyer());
		
		if (liter != null)
			// Kunde hat bereits an dieser Tankstelle getankt
			value = liter.intValue();
		else
			// Kunde hat noch nicht getankt
			value = 0;
		
		 value += Integer.parseInt(content);
		 
		buyers.put(super.getBuyer(), new Integer(value));
	}
}