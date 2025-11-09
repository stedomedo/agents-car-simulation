package main;

import gui.MainFrame;
import gui.MessagesPane;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.BasicProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.StringTokenizer;

/*
 * Created on 28.12.2004
 */

/**
 * Diese Klassen dient zum Starten der gesamten Applikation. Damit dies erfolgreich geschehen kann, 
 * <b>muss</b> beim Start des Programms der Classpath zur jade.jar gesetzt sein.
 * 
 * @author Philipp B&ouml;nisch
 */
public class Main
{
	/**
	 * Dateiname des Auto-Agenten
	 */
	private static final String CAR_AGENT = "agents.CarAgent";
	
	/**
	 * Dateiname des Tankstellen-Agenten
	 */
	private static final String PS_AGENT = "agents.PetrolStationAgent";
	
	/**
	 * Startmethode zum Starten des Programms
	 * 
	 * @param args Argumente, die über die Kommandozeile eingegeben werden 
	 */
	
	public static void main(String[] args)
	{
		// Startet die JADE-Plattform
	    MainFrame.getInstance().setVisible(true);
		try
		{
			// Plattform starten, sollte soweit plattformunabhängig sein!
			// Kommando wird in einem extra Prozess ausgeführt, deshalb kann dieser Thread dann
			// einfach schlafen gelegt werden und das Kommando muss nicht in einem extra Thread
			// ausgeführt werden
		
			/*
			// Start der Plattform, wenn die jade.jar nicht im gleichen Verzeichnis liegt
			// oder von Außen übergeben wird, wie bspw. in einer jar
			String path = null;
			StringTokenizer teile = new StringTokenizer(System.getProperty("java.class.path"), ";");
			// Ermittlung des Pfads zu der jade.jar
			while(teile.hasMoreTokens())
			{
				path = teile.nextToken();
				if (path.toLowerCase().indexOf("jade.jar") != -1)
					break;
			}
			java.lang.Runtime.getRuntime().exec("java -classpath \"" + path + "\" jade.Boot -nomtp");
			
			// Start der Pllatform, wenn die Sources mit ins Projekt eingefügt sind
			/*
			 * String arguments[] = {"-notmp"};
			 * Boot.main(arguments);
			 */
			
			// Start der Plattform, wenn JADE.jar im ins Projekt eingefügt ist
			java.lang.Runtime.getRuntime().exec("java -classpath jade.jar jade.Boot -nomtp");
			
			MessagesPane.println("JADE wird gestartet...");
			
			/*
			 * TODO: Schlafenszeit eventuell noch dynamisch anpassen. Dazu gibt es generell zwei
			 * Möglichkeiten:
			 * 1. Die Erzeugung eines Containers erfolgt in einer Schleife, die solange ausgeführt wird,
			 * so lange es noch zu einer Fehlermeldung kommt (vorher System.err umbiegen).
			 * Funktioniert bis jetzt nicht, da die Plattform ihren Ladevorgang abbricht, wenn dieser
			 * Befehl kommt und die Plattform noch nicht geladen ist.
			 * 
			 * 2. Wenn die Plattform geladen ist, dann erzeugt diese eine Ausgabe auf die Konsole.
			 * Idee dabei, dass man in einer Schleife so lange wartet, bis diese Ausgabe gekommen ist.
			 * Allerdings ist es mir bis jetzt nicht gelungen, an die Ausgabe des Prozesses zu kommen,
			 * der die Plattform startet.
			 */ 
			
			// eigentlichen Programm-Thread schlafen legen, bis die JADE-Plattform geladen ist
			for (int i = 0; i < 20; i++)
			{
				// zeichnet den Lade-Balken
				MessagesPane.print("\u2580");
				Thread.sleep(500);
			}
			
			MessagesPane.println("\n");
		}
		catch (IOException ioe)
		{
			// Ohne Jade-Plattform macht das Programm keinen Sinn
			System.out.println("Die JADE-Plattform konnte nicht gestartet werden!");
			ioe.printStackTrace();
			System.exit(-1);
		}
		catch (InterruptedException ie)
		{
			// Programm-Thread konnte nicht warten, bis die Platttform beendet ist,
			// ohne Plattform macht das Programm keinen Sinn
			System.out.println("Die JADE-Plattform konnte nicht gestartet werden!");
			ie.printStackTrace();
			System.exit(-1);
		}
		
		// enthält die Bootoptionen für den Container, in dem die Agenten gestartet werden
		String propArgs[] = {"-nomtp"};
		
		// legt ein neues Profil an
		ProfileImpl p = new ProfileImpl(new BasicProperties(propArgs));
		
		// kurzzeitig den Standardout umbiegen, damit keine Meldung von JADE zu sehen ist
		PrintStream out = System.out;
		System.setOut(new PrintStream(new ByteArrayOutputStream()));
		
		// legt einen AgentContainer an, um in ihm Agenten starten zu können
		AgentContainer ac = Runtime.instance().createAgentContainer(p);
		
		// Standardout wiederherstellen
		System.setOut(out);
		
		// versucht dem Container einen Autoagenten hinzuzufügen
		try
		{
			// Argumente für das Auto
			Object carAgentArgs[] = {"3", "3"};
			// Auto dem System hinzufügen
			ac.createNewAgent("Pirsche", CAR_AGENT, carAgentArgs).start();
			//ac.createNewAgent("Audi", CAR_AGENT, carAgentArgs).start();
			//ac.createNewAgent("VW", CAR_AGENT, carAgentArgs).start();
		}
		catch (StaleProxyException spe)
		{
			System.out.println("Es konnte kein Auto hinzugefügt werden!\n\n");
			spe.printStackTrace();
		}
		
		// Koordinaten der Tankstellen
		Object psCoords[][] = {{"9", "9"}, {"5", "5"}, {"1", "3"}};
		// Namen der Tankstellen
		Object psNames[] = {"Bea", "Sheff", "Essa"};
		// Strategien, nach der die Tankstellen ihre Preise ermitteln 
		Object psStrategy[] = {"1", "2", "3"};
		
		for (int i = 0; i < psCoords.length; i++)
		{
			try
			{
				// Zusammenfassen der Daten für eine Tankstelle
				Object psAgentArgs[] = {psCoords[i][0], psCoords[i][1], psNames[i], psStrategy[i]}; 
				// Tankstelle dem System hinzufügen
				ac.createNewAgent("Tanke" + i, PS_AGENT, psAgentArgs).start();
			}
			catch (StaleProxyException spe)
			{
				System.out.println("Es konnte keine Tankstelle hinzugefügt werden!\n\n");
				spe.printStackTrace();
			}
		}
	} // Ende main()
	
}