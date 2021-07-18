import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

/*
 *  Program: Prosty edytor grafu
 *     Plik: Node.java
 *

 *    Autor: Lukasz Mieczyński
 *     Data:  grudzień 2020 r.
 */






public class Node implements Serializable {
    private static final long serialVersionUID = 1L;

    // położenie koła
    protected int x;
    protected int y;
protected String name="?";

    // promień koła
    protected int r;

    // kolor wypełnienia
    private Color color;


    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.r = 30;
        this.color = Color.WHITE;
    }
    public Node(int x, int y, String name, Color color) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.color = color;
    }
    public Node(String text) {
        this.name=text;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public void setName(String name)  {
        this.name = name;
    }
    public String getName()
    {  return  name;}


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isMouseOver(int mx, int my){
        return (x-mx)*(x-mx)+(y-my)*(y-my)<=r*r;
    }

    void draw(Graphics g) {
        // Rysowanie wypełnionego koła o środku w punkcie  (x,y)
        // i promieniu r
        g.setColor(color);
        g.fillOval(x-r, y-r, 2*r, 2*r);
        g.setColor(Color.BLACK);
        g.drawOval(x-r, y-r, 2*r, 2*r);
        g.drawString(" ",x-3,y);
        g.drawString(name+" ",x-3,y);



    }

    public String toString() {
        return String.format("%s(%d, %d, %8X)", this.name, this.x, this.y, this.color.getRGB());
    }


}
