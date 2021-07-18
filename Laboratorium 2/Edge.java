/*
 *  Program: Prosty edytor grafu
 *     Plik: Edge.java
 *
 *  Klasa GraphEditor implementuje okno główne
 *  dla prostego graficznego edytora grafu.
 *
 *    Autor: Lukasz Mieczński
 *     Data: grudzien 2020 r.
 */
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.Objects;
import javax.swing.*;

public class Edge implements Serializable {
    private Node node1, node2;
    private Color color;
    private Stroke stroke;



    public Edge(Node node1, Node node2, Stroke stroke) {
        this.node1 = node1;
        this.node2 = node2;
        this.stroke = stroke;
    }
    public Edge(Node node1, Node node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public Edge(Node node1, Node node2,Color color) {
        this.node1 = node1;
        this.node2 = node2;
        this.color=color;
    }


    public Node getNode1() {
        return node1;
    }

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setNode2(Node node2) {
        this.node2 = node2;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isMouseOver(int mx, int my) {


        double distance = Line2D.ptSegDist(node1.getX(), node1.getY(),
                node2.getX(), node2.getY(),
                mx, my);

        if (distance < 2) {
            return true;
        }
        return  false;
    }

    void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.drawLine(node1.getX(), node1.getY(), node2.getX(), node2.getY());
        g.setColor(Color.BLACK);


    }


        public String toString() {
           return  String.format(String.valueOf(this.color));
        }
    }
