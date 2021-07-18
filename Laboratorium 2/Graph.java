import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*
 *  Program: Prosty edytor grafu
 *     Plik: Graph.java
 *

 *
 *    Autor: Lukasz Mieczński
 *     Data: grudzien 2020 r.
 */
public class Graph extends Component implements Serializable {

    private static final long serialVersionUID = 1L;

    // Lista węzłów grafu
    private List<Node> nodes;
    private List<Edge> edges;
    private final String ERROR_MESSAGE = "Nieprawidłowe dane!\nSpróbuj jeszcze raz.";
    public Graph() {
        this.nodes = new ArrayList<Node>();
        this.edges = new ArrayList<Edge>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }


    public void addEgde(Edge edge) {
        edges.add(edge);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public Node[] getNodes() {
        Node[] array = new Node[0];
        return nodes.toArray(array);
    }

    public Edge[] getEdges() {
        Edge[] array = new Edge[0];
        return edges.toArray(array);
    }

    public void removeNode(Node node)
    {
        Iterator iter = this.edges.iterator();

        while(true) {
            Edge edge;
            do {
                if (!iter.hasNext()) {
                    this.nodes.remove(node);
                    return;
                }

                edge = (Edge)iter.next();
            } while(edge.getNode1() != node && edge.getNode2() != node);

            iter.remove();
        }
    }


    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke s = new BasicStroke(3);
        Stroke s1 = new BasicStroke(2);
        g2d.setStroke(s);
        for (Edge edge : edges){
            edge.draw(g2d);
        }
        g2d.setStroke(s1);
        for (Node node : nodes) {
            node.draw(g2d);
        }
    }
    public static void writeToFile(String fileName, Graph graph) throws Throwable {
        try {
            Throwable var2 = null;
            Object var3 = null;

            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));

                try {
                    outputStream.writeObject(graph);
                } finally {
                    if (outputStream != null) {
                        outputStream.close();
                    }

                }

            } catch (Throwable var14) {
                if (var2 == null) {
                    var2 = var14;
                } else if (var2 != var14) {
                    var2.addSuppressed(var14);
                }

                throw var2;
            }
        } catch (FileNotFoundException var15) {
            throw new Exception("Nie znaleziono pliku <" + fileName + ">");
        } catch (IOException var16) {
            throw new Exception("Wystąpił błąd przy zapisie pliku <" + fileName + ">");
        }
    }




    public static Graph readFromFile(String file_name) throws Exception {
        try {
            FileInputStream file = new FileInputStream(file_name);
            ObjectInputStream reader = new ObjectInputStream(file);
            while (true) {
                try {
                    Graph obj = (Graph)reader.readObject();
                    System.out.println(obj);
                    return obj;
                } catch (Exception ex) {
                    System.err.println("end of reader file ");
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("failed to read " + file_name + ", "+ ex);
        }

        return null;
    }
}