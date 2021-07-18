import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/*
 *  Program: Prosty edytor grafu
 *     Plik: GraphEditor.java
 *
 *  Klasa GraphEditor implementuje okno główne
 *  dla prostego graficznego edytora grafu.
 *
 *    Autor: Lukasz Mieczński
 *     Data: grudzien 2020 r.
 */

public class GraphEditor extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String APP_AUTHOR = "Autor: Lukasz Mieczynski\n  Data: Grudzien 2020";
    private static final String APP_TITLE = "Edytor grafów";

    private static final String APP_INSTRUCTION =
            "                  O P I S   P R O G R A M U \n\n" +
                    "Aktywna klawisze:\n" +
                    "   strzałki ==> przesuwanie wszystkich kół\n" +
                    "   SHIFT + strzałki ==> szybkie przesuwanie wszystkich kół\n\n" +
                    " gdy kursor wskazuje koło:\n" +
                    "   DEL   ==> usuwanie koła\n" +
                    "   +, -   ==> powiększanie, pomniejszanie koła\n" +
                    "   r,g,b ==> zmiana koloru koła\n\n" +
                    "Operacje myszka:\n" +
                    "   przeciąganie ==> przesuwanie wszystkich kół\n" +
                    "   PPM ==> tworzenie nowego koła w niejscu kursora\n" +
                    "ponadto gdy kursor wskazuje koło:\n" +
                    "   przeciąganie ==> przesuwanie koła\n" +
                    "   PPM ==> zmiana koloru koła\n" +
                    "               usuwanie koła\n" +
                    "           dodawanie krawędzi\n";


    public static void main(String[] args) {
        new GraphEditor();
    }


    // private GraphBase graph;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuGraph = new JMenu("Graph");
    private JMenu menuHelp = new JMenu("Help");
    private JMenuItem menuNew = new JMenuItem("New", KeyEvent.VK_N);
    private JMenuItem menuShowExample = new JMenuItem("Example", KeyEvent.VK_X);
    private JMenuItem menuExit = new JMenuItem("Exit", KeyEvent.VK_E);
    private JMenuItem menuListOfNodes = new JMenuItem("List of Nodes", KeyEvent.VK_N);
    private JMenuItem menuListOfEdges = new JMenuItem("List of Edges", KeyEvent.VK_N);
    private JMenuItem menuAuthor = new JMenuItem("Author", KeyEvent.VK_A);
    private JMenuItem menuOpen = new JMenuItem("Open from File", 83);
    private JMenuItem menuSave = new JMenuItem("Save to File");
    private JMenuItem menuInstruction = new JMenuItem("Instruction", KeyEvent.VK_I);

    private GraphPanel panel = new GraphPanel();


    public GraphEditor() {
        super(APP_TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400,400);
        setLocationRelativeTo(null);
        setContentPane(panel);
        createMenu();
        showBuildinExample();
        setVisible(true);
    }

    private void showListOfNodes(Graph graph) {
        Node array[] = graph.getNodes();
      Node [] nodeArray = array;
        int lentgh = array.length;
        int i = 0;

        StringBuilder message = new StringBuilder("Number of nodes: " + array.length + "\n");
        for(int j = 0; j < lentgh; ++j) {
            Node node = nodeArray[j];
            message.append(node + "    ");
            ++i;
            if (i % 5 == 0) {
                message.append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, message, " List of edges", -1);
    }
    private void showListOfEdges(Graph graph) {
        Edge array[] = graph.getEdges();
        Edge[] edgeArray = array;
        int lentgh = array.length;
        int i = 0;

        StringBuilder message = new StringBuilder("Number of edges: " + array.length + "\n");
        for(int j = 0; j < lentgh; ++j) {
            Edge edge = edgeArray[j];
            message.append(edge + "    ");
            ++i;
            if (i % 5 == 0) {
                message.append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, message, " List of edges", -1);
    }

    private void createMenu() {
        menuNew.addActionListener(this);
        menuShowExample.addActionListener(this);
        menuExit.addActionListener(this);
        menuListOfNodes.addActionListener(this);
        menuListOfEdges.addActionListener(this);
        menuAuthor.addActionListener(this);
        menuInstruction.addActionListener(this);
        menuSave.addActionListener(this);
        menuOpen.addActionListener(this);
        menuGraph.setMnemonic(KeyEvent.VK_G);
        menuGraph.add(menuNew);
        menuGraph.add(menuShowExample);
        menuGraph.addSeparator();
        menuGraph.add(menuListOfNodes);
        menuGraph.add(menuListOfEdges);
        menuGraph.addSeparator();
        menuGraph.add(menuOpen);
        menuGraph.add(menuSave);
        menuGraph.add(menuExit);


        menuHelp.setMnemonic(KeyEvent.VK_H);
        menuHelp.add(menuInstruction);
        menuHelp.add(menuAuthor);

        menuBar.add(menuGraph);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == menuNew) {
            Graph graph= new Graph();
            panel.setGraph(graph);
        }
        String fileName;
        if (source == this.menuOpen) {
            fileName = JOptionPane.showInputDialog("Gives the name of the file");
            if (fileName == null || fileName.equals("")) {
                return;
            }

            try {
                this.panel.setGraph(Graph.readFromFile(fileName));
            } catch (Exception var6) {
                JOptionPane.showMessageDialog(this, var6.getMessage(), "Error", 0);
            }
        }

        if (source == this.menuSave) {
            fileName = JOptionPane.showInputDialog("Gives the name of the file");
            if (fileName == null || fileName.equals("")) {
                return;
            }

            try {
                Graph.writeToFile(fileName, this.panel.getGraph());
            } catch (Throwable var5) {
                JOptionPane.showMessageDialog(this, var5.getMessage(), "Error", 0);
            }
        }
        if (source == menuShowExample) {
           showBuildinExample();
        }
        if (source == menuListOfNodes) {
            showListOfNodes(panel.getGraph());
        }
        if (source == menuListOfEdges) {
            showListOfEdges(panel.getGraph());
        }
        if (source == menuAuthor) {
            JOptionPane.showMessageDialog(this, APP_AUTHOR, APP_TITLE, JOptionPane.INFORMATION_MESSAGE);
        }
        if (source == menuInstruction) {
            JOptionPane.showMessageDialog(this, APP_INSTRUCTION, APP_TITLE, JOptionPane.PLAIN_MESSAGE);
        }
        if (source == menuExit) {
            System.exit(0);
        }
    }

    private void showBuildinExample() {
        Graph graph = new Graph();
        Node n1 = new Node(100, 100);
        n1.setName("a");
        Node n2 = new Node(100, 200);
        n2.setColor(Color.CYAN);
        n2.setName("b");
        Stroke stroke = new BasicStroke(5);
        Edge e1= new Edge(n1,n2,stroke);

e1.setColor(Color.black);
        Node n3 = new Node(200, 100);
        n3.setR(20);
        n3.setName("c");
        Node n4 = new Node(200, 250);
        n4.setColor(Color.GREEN);
        n4.setR(30);
        n4.setName("d");
        Edge e2= new Edge(n1,n3,stroke);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);
graph.addEgde(e1);
graph.addEgde(e2);
        panel.setGraph(graph);
    }
}
