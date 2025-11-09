/*
 * Created on 11.01.2005
 */
package gui;

/**
 * 
 * Eine Art "Dummy"-Klasse, um die Informationen der Tankstellen-Agenten zu speichern, 
 * um diese für das zeichnen in der GUI benutzen zu können
 * 
 * @author Carmen Heger
 */
public class PetrolStation {
    
    int type;
    int x;
    int y;
    
 
    /**
     * erzeugt neue "Dummy"-Tankstelle
     * @param type - Typ
     * @param x - Position x
     * @param y - Position y
     */
    public PetrolStation(int type, int x, int y){
        this.type=type;
        this.x=x;
        this.y=y;
    }
    
    /**
     * gibt den Typ der Tankstelle zurück
     * @return Tankstellentyp
     */
    public int getType(){
        return type;
    }
    
    /**
     * gibt die x-Position der Tankstelle zurück
     * @return x-Position
     */
    public int getX(){
        return x;
    }
    
    /**
     * gibt die y-Position der Tankstelle zurück
     * @return y-Position
     */
    public int getY(){
        return y;
    }

}
