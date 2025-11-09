/*
 * Created on 01.01.2005
 */
package gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * 
 * Klasse stellt das Panel mit dem Stadtplan dar, samt Autos und Tankstellen
 * 
 * @author Carmen Heger
 */
public class MapPane extends JPanel {

    /**
     * Liste mit Tankstellen
     */
    static LinkedList ps = new LinkedList();

    private Image dbImage;

    private Image gelb = getToolkit().getImage(
            ClassLoader.getSystemResource("gui/tank_gelb.gif"));

    private Image gruen = getToolkit().getImage(
            ClassLoader.getSystemResource("gui/tank_gruen.gif"));

    private Image rot = getToolkit().getImage(
            ClassLoader.getSystemResource("gui/tank_rot.gif"));

    private Image baum = getToolkit().getImage(
            ClassLoader.getSystemResource("gui/baum.gif"));

    private Image oben = getToolkit().getImage(
            ClassLoader.getSystemResource("gui/auto_oben.gif"));

    private Image unten = getToolkit().getImage(
            ClassLoader.getSystemResource("gui/auto_unten.gif"));

    private Image links = getToolkit().getImage(
            ClassLoader.getSystemResource("gui/auto_links.gif"));

    private Image rechts = getToolkit().getImage(
            ClassLoader.getSystemResource("gui/auto_rechts.gif"));
    
    private Image map = getToolkit().getImage(
            ClassLoader.getSystemResource("gui/map.gif"));

    private Graphics dbGraphics;

    int xOffset;

    int yOffset;

    int xCar;

    int yCar;

    int courseCar;

    public static MapPane instance = null;

    JPanel panel;

    /**
     * erzeugt ein neues MapPane
     *
     */
    public MapPane() {
        add(new JLabel("Stadtplan:"));
        xOffset = getInsets().left + 60;
        yOffset = getInsets().top + 40;
        setSize(10 * 30 + xOffset + getInsets().right, 10 * 30 + yOffset
                + getInsets().bottom);
        setBackground(Color.WHITE);
    }

    public static MapPane getInstance() {
        if (instance == null) {
            instance = new MapPane();
        }
        return instance;
    }

    /**
     * zeichnet den Stadtplan neu
     */
    public void paint(Graphics g) {
        int x;
        int y;
        Image img = rot;
        super.paint(g);
        g.drawLine(xOffset, yOffset, 300 + xOffset, yOffset);
        g.drawLine(300 + xOffset, yOffset, 300 + xOffset, 300 + yOffset);
        g.drawLine(xOffset, 300+ yOffset, 300 + xOffset, 300 + yOffset);
        g.drawLine(xOffset, yOffset, xOffset, 300 + yOffset);
        
        getGraphics().drawImage(map, xOffset, yOffset,300,300,this);
        
        Iterator it = ps.iterator();
        while (it.hasNext()) {
            PetrolStation station = (PetrolStation) it.next();
            switch (station.getType()) {
            case 1:
                img = gelb;
                break;
            case 2:
                img = gruen;
                break;
            case 3:
                img = rot;
                break;
            default:
                ;
            }
            getGraphics().drawImage(img, station.getX() * 30 + xOffset,
                    station.getY() * 30 + yOffset, 30, 30, this);

        }

    }

    /**
     * übermalt den Stadtplan und stellt eine leere Graphics-Umgebung dar
     */
    public void update(Graphics g) {
        //Double-Buffer initialisieren
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbGraphics = dbImage.getGraphics();
        }
        //Hintergrund löschen
        dbGraphics.setColor(getBackground());
        dbGraphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
        //Vordergrund zeichnen
        dbGraphics.setColor(getForeground());
        paint(dbGraphics);
        //Offscreen anzeigen
        g.drawImage(dbImage, 0, 0, this);
    }

    /**
     * zeichnet die Tankstellen neu
     *
     */
    public void drawPS() {
        paint(getGraphics());
    }

    /**
     * fügt Tankstelle der ps (Liste von Tankstellen) hinzu
     * 
     * @param type - Typ
     * @param x - Position x
     * @param y - Position y
     */
    public void addPS(int type, int x, int y) {
        PetrolStation station = new PetrolStation(type, x, y);
        ps.add(station);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
        drawPS();
    }

    /**
     * zeichnet Auto an aktuelle Position
     * 
     * @param course - Richtung
     * @param x - Position x
     * @param y - Position y
     */
    synchronized public void drawCar(int course, int x, int y) {
        
        Image img2 = oben;

        switch (course) {
        case 0: // unten
            img2 = unten;
            break;

        case 1: // rechts
            img2 = rechts;
            break;

        case 2: // oben
            img2 = oben;
            break;

        case 3: // links
            img2 = links;
            break;
        }
        paint(getGraphics());
        getGraphics().drawImage(img2, x * 30 + xOffset,
                y * 30 + yOffset, 30, 30, this);
    }

}