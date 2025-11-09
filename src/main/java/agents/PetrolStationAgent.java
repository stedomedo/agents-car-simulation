/*
 * Created on 27.12.2004
 */
package agents;

import gui.MapPane;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.HashMap;

import main.Global;
import behaviours.WorkingQuantityBehaviour;
import behaviours.WorkingRandomBehaviour;
import behaviours.WorkingTimesBehaviour;

/**
 * Diese Klasse kapselt den Agenten einer Tankstelle.
 * <br>Die Tankstelle ist repr&auml;sentiert durch ihre Position auf dem Gitternetz. 
 * Au&szlig;erdem beinhaltet sie interne Verwaltungsangaben zur Preisermittlung, welcher auf
 * verschiedene Art und Weisen ermittelt wird. Dazu gibt es drei verschiedene Varianten.
 * <br>Die erste Variante h&auml;ngt davon ab, wieviel der Kunde bereits gekauft hat. Um so mehr er bereits
 * gekauft hat, um so g&uuml;nstiger wird das Angebot.
 * <br>Bei der zweiten Variante h&auml;ngt der Preis von der bereits gekauften Menge ab. Je mehr der Kunde
 * bereits an einer Tankstelle dieser Firma gekauft hat, desto g&uuml;nstiger wird der Preis.
 * <br>Bei der dritten Preisstrategie wird der Preis per Zufall innerhalb eines bestimmten Bereichs
 * ermittelt.
 * 
 * <br><br>Bei der Erzeugung erwartet der Tankstellen-Agent insgesamt 4 Parameter:
 * <ol>
 * 	<li>X-Koordinate im Stra&szlig;engitter</li>
 * 	<li>Y-Koordinate im Stra&szlig;engitter</li>
 * 	<li>Firmenname der Gesellschaft</li>
 * 	<li>Preisstrategie zur Ermittlung des Preises:
 * 		<ul style="list-style-type:none">
 * 			<li>- nach bereits getankter Menge: 1</li>
 * 			<li>- nach Anzahl der Tankungen: 2</li>
 * 			<li>- per Zufall: >2</li>
 * 		</ul>
 * 	</li>
 * </ol>
 * 
 * @see WorkingQuantityBehaviour#calculatePrice()
 * @see WorkingTimesBehaviour#calculatePrice()
 * @see WorkingRandomBehaviour#calculatePrice()
 * 
 * @author Denis Stein, Philipp B&ouml;nisch
 */
public class PetrolStationAgent extends Agent {
	
	/**
	 * x-Position
	 */
	private int xPosition;
	
	/**
	 * y-Position
	 */
	private int yPosition;
	
	/**
	 * Marke der Tankstelle
	 */
	private String company;
	
	/**
	 * Speichert je nach verwendeter Preisstrategie verschiedene Kundendaten ab
	 */
	private static HashMap sales = new HashMap();

	/**
	 * In dieser Funktion werden die Argumente dem Agenten zugewiesen und 
	 * die angebotenen Services beim DF angemeldet. 
	 */
	public void setup() {
		/*
		 * Einlesen der Argumente und Zuweisung an TankstellenAgent.
		 */
		Object[] args = this.getArguments();
		xPosition = Integer.parseInt(String.valueOf(args[0]));
		yPosition = Integer.parseInt(String.valueOf(args[1]));	
		company = String.valueOf(args[2]);
		int type = Integer.parseInt(String.valueOf(args[3]));
		
		// Registreiren des Behaviours, um auf Kundenanfragen regaieren zu können
		switch (type)
		{
			// Tankstelle ermittelt den Preis nach bereits verkaufter Menge
			case 1:	this.addBehaviour(new WorkingQuantityBehaviour(this));
					break;
				
			// Tankstelle ermittelt den Preis nach der Anzahl der Besuche eines Kunden
			case 2:	this.addBehaviour(new WorkingTimesBehaviour(this));
					break;
				
			// Tankstelle ermittelt den Preis per Zufall
			default: this.addBehaviour(new WorkingRandomBehaviour(this));
		}
		
		MapPane.getInstance().addPS(type,xPosition,yPosition);
		
		/*
		 * Registrieren des angebotenen Services.
		 */
		registerService();
	}
	
	/**
	 * Erledigt Registrierung des angebotenen Services (Tankstelle) bei DF.
	 */
	private void registerService() {
		/*
		 * Es wird eine Beschreibung des Services an DF geschickt, im 
		 * Fehlerfall beendet sich der TanstellenAgent.
		 * Bedeutung der 3 Parameter der ServiceDescription:
		 * Name: Koordinaten
		 * Type: Service
		 * Ownership: Marke
		 */
		DFAgentDescription dfad = new DFAgentDescription();
		dfad.setName(this.getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Global.SERVICE);
		sd.setName(Global.coordinatesToString(this.xPosition, this.yPosition));
		sd.setOwnership(company);
		dfad.addServices(sd);
		try
		{
			DFService.register(this, dfad);
		}
		catch (FIPAException e)
		{
			// Fehlermeldung ist eine andere, als dass der Agent schon registriert ist
			if (e.getMessage().indexOf("already-registered") == -1)
			{
				// Stack ausgeben
				e.printStackTrace();
				// Agenten beenden
				this.doDelete();
			}
		}
	}
	
	/**
	 * Liefert x-Koordinate.
	 * @return x-Koordinate.
	 */
	public int getXPosition() {
		return xPosition;
	}
	
	/**
	 * Liefert y-Koordinate.
	 * @return y-Koordinate.
	 */
	public int getYPosition() {
		return yPosition;
	}
	
	/**
	 * Enth&auml;lt verschiedene Kundendaten, je nachdem, welche Preisstrategie die Tankstelle
	 * gerade implementiert.
	 * 
	 * @return Verschiedene Kundendaten
	 */
	public HashMap getSales()
	{
		return sales;
	}
	
	/**
	 * Gibt den Firmennamen zur&uuml;ck.
	 * 
	 * @return Firmenname
	 */
	public String getCompany()
	{
		return company;
	}
	
	/**
	 * &Uuml;berschriebene Hook-Methode, die beim Beenden des Agenten aufgerufen wird.
	 * Sie sorgt daf&uuml;r, dass der Agent bei der Registry deregistriert wird.
	 */
	protected void takeDown()
	{
		try
		{
			DFService.deregister(this);
		}
		catch (FIPAException e)
		{
			System.out.println("Der Agent konnte nicht aus der Registry ausgetragen werden!\n\n");
			e.printStackTrace();
		}
	}
}