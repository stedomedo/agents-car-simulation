/*
 * Created on 10.01.2005
 */
package gui;

import java.awt.Color;

import javax.swing.*;

/**
 * 
 * Klasse, die JPanel erzeugt, welches Informationen über Auto und Tankstellen anzeigt.
 * 
 * @author Carmen Heger
 */
public class InfoPane extends JPanel {

    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);

    JLabel label = new JLabel("----------------------------------");
    JLabel label2 = new JLabel("----------------------------------");

    JLabel tank = new JLabel("Tank:   ");
    JLabel stations = new JLabel("Tankstellen:");

    ImageIcon gelb = new ImageIcon(ClassLoader.getSystemResource("gui/pics/tank_gelb.gif"));
    JLabel gelbL = new JLabel(gelb);

    ImageIcon gruen = new ImageIcon(ClassLoader.getSystemResource("gui/pics/tank_gruen.gif"));
    JLabel gruenL = new JLabel(gruen);
    
    ImageIcon rot = new ImageIcon(ClassLoader.getSystemResource("gui/pics/tank_rot.gif"));
    JLabel rotL = new JLabel(rot);
    

    public static InfoPane instance = null;

    public static InfoPane getInstance() {
        if (instance == null) {
            instance = new InfoPane();
        }
        return instance;
    }

    /**
     * aktualisiert die Anzeige der Tankfüllung
     * @param name - Name des Autos
     * @param s - Tankstand
     */
    public void setTank(String name, int s) {
        tank.setText("Tank (" + name + ") : " + s);
    }

    /**
     * erzeugt neues InfoPane
     */
    public InfoPane() {
        
        setBackground(Color.WHITE);

        setLayout(box);
        add(new JLabel("Informationen:"));
        add(label);
        add(tank);
        add(label2);
        add(stations);
        gelbL.setText("Bea");
        gruenL.setText("Essa");
        rotL.setText("Sheff");
                
        add(gelbL);
        add(gruenL);
        add(rotL);
        
        //setSize(320, 350);

    }

}