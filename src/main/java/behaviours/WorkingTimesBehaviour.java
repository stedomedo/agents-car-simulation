/*
 * Created on 30.12.2004
 */
package behaviours;

import java.util.HashMap;

import agents.PetrolStationAgent;
import jade.core.Agent;

/**
 * Diese Klasse ermittelt f&uuml;r eine konkrete Anfrage den Preis, den der Kunde an dieser Tankstelle zu
 * bezahlen hat und &uuml;bermittelt diesen an den Kunden.
 * Der Preis ergibt sich dabei aus der Anzahl, wie oft der Kunde bereits an dieser Tankstelle getankt
 * hat. Umso mehr dieser in der Vergangenheit bereits bei einer Tankstelle dieser Firma sein Benzin
 * gekauft hat, desto g&uuml;nstiger (bis zu einer bestimmten Minimalgrenze) wird es für den Kunden.
 * Die eigentliche Arbeit wird allerdings in der Klasse WorkingBehavior geleistet, hier ist lediglich
 * die Ermittlung des Preises und die Aktualisierung der Kundendaten implementiert. 
 * 
 * @see behaviours.WorkingBehaviour
 * @author Philipp B&ouml;nisch
 */
public class WorkingTimesBehaviour extends WorkingBehaviour
{

	/**
	 * Initialisierung der Klasse.
	 * 
	 * @param a dazugeh&ouml;riger Agent
	 */
	public WorkingTimesBehaviour(Agent a)
	{
		super(a);
	}

	/**
	 * Ermittelt der Benzinpreis auf Anfrage. Dieser richtet sich nach der der Anzahl der
	 * Besuche des Kunden. Die Preise fangen bei 1.75 pro Liter an und nehmen mit jedem
	 * Besuch um 0,02 ab. Der Minimalpreis betr&auml;gt 1,40.
	 * 
	 * @see behaviours.WorkingBehaviour#calculatePrice()
	 */
	public double calculatePrice()
	{
		// Kundendaten sämtlicher Kunden an allen Tankstellen
		HashMap data = ((PetrolStationAgent)myAgent).getSales();
		// Kundendaten von Kunden dieser Tankstellenfirma
		HashMap buyers = (HashMap)(data.get(((PetrolStationAgent)myAgent).getCompany()));
		
		if (buyers == null)
		{
			// Es existiert noch kein Eintrag für Tankstellen dieser Firma
			buyers = new HashMap();
			data.put(((PetrolStationAgent)myAgent).getCompany(), buyers);
		}
		
		// Anzahl der bisherigen Besuche an Tankstellen dieser Firma
		Integer count = (Integer)buyers.get(super.getBuyer());
		
		if (count != null)
		{
			// Der Kunde hat bereits an einer Tankstelle dieses Unternehmens getankt
			double ret = 1.75 - (0.02 * count.intValue());
			if (ret < 1.40)
				// Minimum ist erreicht
				return 1.40;
			else
				// gesenkter Preis
				return ret;
		}
		else
			// Kunde hat bis jetzt noch nicht an dieser Tankstelle getankt
			return 1.75;
	}

	/**
	 * Aktualisiert die Verwaltungsdaten dieser Tankstelle. Die Anzahl der Besuche dieses Kunden an
	 * dieser Tankstelle hat sich um eins erh&ouml;ht. 
	 * 
	 * @see behaviours.WorkingBehaviour#fueling(java.lang.String)
	 */
	public void fueling(String content)
	{
		// Daten aller Kunden an dieser Tankstelle
		HashMap buyers = (HashMap)((PetrolStationAgent)myAgent).getSales().get(((PetrolStationAgent)myAgent).getCompany());
		
		// Anzahl der bisherigen Besuche des Kunden an Tankstellen dieser Firma
		Integer counter = (Integer)buyers.get(super.getBuyer());
		
		if (counter != null)
			// Die Anzahl der Besuche erhöht sich um Eins
			counter = new Integer(counter.intValue()+1);
		else
			// Der Kunde hat das erste Mal an dieser Tankstelle getankt
			counter = new Integer(1);
		
		buyers.put(super.getBuyer(), counter);
	}
}