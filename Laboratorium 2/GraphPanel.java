import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.*;

/*
 *  Program: Prosty edytor grafu
 *     Plik: GraphPanel.java
 *
 *  Klasa GraphPanel implementuje podstawowe funkcjonalności
 *  graficznego edytora grafu nieskierowanego.
 *  Klasa umożliwia:
 *     - rysowanie grafu w oknie,
 *     - obsługę zdarzeń generowanych przez myszkę,
 *     - tworzenie i obsługę menu kontekstowych
 *       umożliwiających wykonywanie operacji edycyjnych.
 *
 *    Autor: Lukasz Mieczynski
 *     Data:  grudzien 2020 r.
 */

public class GraphPanel extends JPanel
        implements MouseListener, MouseMotionListener, KeyListener {
    private Color currentColor;


    private static final long serialVersionUID = 1L;

    protected Graph graph;

    private int mouseX = 0;
    private int mouseY = 0;
    private boolean mouseButtonLeft = false;
    @SuppressWarnings("unused")
    private boolean mouseButtonRigth = false;
    protected int mouseCursor = Cursor.DEFAULT_CURSOR;

    protected Node nodeUnderCursor = null;
protected  Edge edgeUnderCursor =null;
protected  Node nodeFrom=null;


    public GraphPanel() {
        this.currentColor = Color.WHITE;
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        this.nodeUnderCursor = null;
        this.edgeUnderCursor = null;
        this.nodeFrom = null;
        this.setCursor(Cursor.getDefaultCursor());
        this.repaint();
    }


    private Node findNode(int mx, int my){
        for(Node node: graph.getNodes()){
            if (node.isMouseOver(mx, my)){
                return node;
            }
        }
        return null;
    }

    private Edge findEdge(int mx, int my){
        for(Edge edge: graph.getEdges()){
            if (edge.isMouseOver(mx, my)){
                return edge;
            }
        }
        return null;
    }
    private Node findNode(MouseEvent event){
        return findNode(event.getX(), event.getY());
    }
    private Edge findEdge(MouseEvent event){
        return findEdge(event.getX(), event.getY());
    }


    protected void setMouseCursor(MouseEvent event) {
        nodeUnderCursor = findNode(event);
        edgeUnderCursor = findEdge(event);

        if (nodeUnderCursor != null) {
            mouseCursor = Cursor.HAND_CURSOR;
        } else if (mouseButtonLeft) {
            mouseCursor = Cursor.MOVE_CURSOR;
        } else if (this.nodeFrom != null) {
            this.mouseCursor = 3;
        } else {
            this.mouseCursor = 0;}
            if (edgeUnderCursor != null)
                mouseCursor = Cursor.HAND_CURSOR;

            setCursor(Cursor.getPredefinedCursor(mouseCursor));
            mouseX = event.getX();
            mouseY = event.getY();

    }
    protected void setMouseCursor() {

        if (nodeUnderCursor != null) {
            mouseCursor = Cursor.HAND_CURSOR;
        } else if (mouseButtonLeft) {
            mouseCursor = Cursor.MOVE_CURSOR;
        } else if (this.nodeFrom != null) {
            this.mouseCursor = 3;
        } else {
            this.mouseCursor = 0;}
            if (edgeUnderCursor != null)
                mouseCursor = Cursor.HAND_CURSOR;

        this.setCursor(Cursor.getPredefinedCursor(this.mouseCursor));

    }
    private void moveNode(int dx, int dy, Node node){
        node.setX(node.getX()+dx);
        node.setY(node.getY()+dy);
    }

    private void moveAllNodes(int dx, int dy) {
        for (Node node : graph.getNodes()) {
            moveNode(dx, dy, node);
        }
    }

    /*
     * UWAGA: ta metoda będzie wywoływana automatycznie przy każdej potrzebie
     * odrysowania na ekranie zawartości panelu
     *
     * W tej metodzie NIE WOLNO !!! wywoływać metody repaint()
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (graph==null) return;
        {
            graph.draw(g);
        }
    }

    /*
     * Implementacja interfejsu MouseListener - obsługa zdarzeń generowanych przez myszkę
     * gdy kursor myszki jest na tym panelu
     */
    @Override
    public void mouseClicked(MouseEvent event) {

        this.setMouseCursor(event);
        if (event.getButton() == 1) {
            if (this.nodeFrom != null && this.nodeUnderCursor != null && this.nodeUnderCursor != this.nodeFrom) {
                this.graph.addEgde(new Edge(this.nodeFrom, this.nodeUnderCursor));
            }
        }
                this.repaint();
            this.nodeFrom = null;
        this.setMouseCursor(event);
    }


    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getButton()==1) mouseButtonLeft = true;
        if (event.getButton()==3) mouseButtonRigth = true;
        setMouseCursor(event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (event.getButton() == 1)
            mouseButtonLeft = false;
        if (event.getButton() == 3)
            mouseButtonRigth = false;
        setMouseCursor(event);
        if (event.getButton() == 3) {
            if (nodeUnderCursor != null && edgeUnderCursor==null) {
                createPopupMenu1(event, nodeUnderCursor);
            }
            if (nodeUnderCursor == null && edgeUnderCursor==null){
                createPopupMenu(event,nodeUnderCursor);}
            if (edgeUnderCursor != null && nodeUnderCursor==null)
                createPopupMenu(event, edgeUnderCursor);

        }
    }


    /*
     * Implementacja interfejsu MouseMotionListener
     *  - obsługa zdarzeń generowanych przez ruch myszki
     * gdy kursor myszki jest na tym panelu
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        if (mouseButtonLeft) {
            if (nodeUnderCursor != null) {
                moveNode(event.getX() - mouseX, event.getY() - mouseY, nodeUnderCursor);
            } else {
                moveAllNodes(event.getX() - mouseX, event.getY() - mouseY);
            }
        }
        mouseX = event.getX();
        mouseY = event.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        setMouseCursor(event);
    }


    /*
     *  Impelentacja interfejsu KeyListener - obsługa zdarzeń generowanych
     *  przez klawiaturę gdy focus jest ustawiony na ten obiekt.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        {  int dist;
            if (event.isShiftDown()) dist = 10;
            else dist = 1;
            switch (event.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    moveAllNodes(-dist, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    moveAllNodes(dist, 0);
                    break;
                case KeyEvent.VK_UP:
                    moveAllNodes(0, -dist);
                    break;
                case KeyEvent.VK_DOWN:
                    moveAllNodes(0, dist);
                    break;
                case KeyEvent.VK_DELETE:
                    if (nodeUnderCursor != null) {
                        graph.removeNode(nodeUnderCursor);
                        nodeUnderCursor = null;
                    }
                    break;
            }
        }
        repaint();
        setMouseCursor();
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    @Override
    public void keyTyped(KeyEvent event) {
        char znak=event.getKeyChar();
        if (nodeUnderCursor!=null){
            switch (znak) {
                case 'r':
                    nodeUnderCursor.setColor(Color.BLUE);
                    break;
                case 'g':
                    nodeUnderCursor.setColor(Color.ORANGE);
                    break;
                case 'b':
                    nodeUnderCursor.setColor(Color.cyan);
                    break;
                case '+':
                    int r = nodeUnderCursor.getR()+10;
                    nodeUnderCursor.setR(r);
                    break;
                case '-':
                    r = nodeUnderCursor.getR()-10;
                    if (r>=10) nodeUnderCursor.setR(r);
                    break;
            }
            repaint();
            setMouseCursor();
        }
    }


    /*
     *  Tworzenie i obsługa menu kontekstowego uruchanianego
     *  poprzez kliknięcie prawym przyciskiem myszki
     */
    protected void createPopupMenu(MouseEvent event) {
        JMenuItem menuItem;

        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Create new node");

        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((action) -> {
Node node= new Node(event.getX(), event.getY());
            Node node1= new Node(event.getX(), event.getY());
            graph.addNode(node1);
            repaint();
            repaint();
        });
		/*
		// Implementacja słuchacza zdarzeń za pomocą klasy anonimowej
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				graph.addNode(new Node(event.getX(), event.getY()));
				repaint();
			}
		});
		*/
        popup.add(menuItem);
        popup.show(event.getComponent(), event.getX(), event.getY());
    }

    protected void createPopupMenu(MouseEvent event,Node node) {
        JMenuItem menuItem;

        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Create new node");

        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((action) -> {
            Node node1= new Node(event.getX(), event.getY());
                graph.addNode(node1);
            repaint();
        });


        popup.add(menuItem);
        popup.show(event.getComponent(), event.getX(), event.getY());
    }
    protected void createPopupMenu(MouseEvent event, Edge edge) {
        JMenuItem menuItem;

        // Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Remove edge");

        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((a) -> {
                graph.removeEdge(edge);


            repaint();
        });
        popup.add(menuItem);
        if (edge instanceof Edge) {
            menuItem = new JMenuItem("Change color of this edge");
            menuItem.addActionListener((a) -> {
                this.runColorChooser(this);
                edge.setColor(this.currentColor);
                this.repaint();
            });
            popup.add(menuItem);
        }






        popup.show(event.getComponent(), event.getX(), event.getY());}

    protected void createPopupMenu1(MouseEvent event, Node node) {
        JMenuItem menuItem;
        // Create the popup menu.
        JPopupMenu popup = new JPopupMenu();





        menuItem = new JMenuItem("Remove this node");

        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((action) -> {

            graph.removeNode(node);
            repaint();
        });
        popup.add(menuItem);
        menuItem = new JMenuItem("Create new edge");
        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((action) -> {
            this.nodeFrom = node;
            this.mouseCursor = 3;



            repaint();

        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Change node color");
        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((action) -> {
            this.runColorChooser(this);
            (node).setColor(this.currentColor);
            this.repaint();



        });

        popup.add(menuItem);
        menuItem = new JMenuItem("Change node name");
        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((action) -> {

            String name = (String)JOptionPane.showInputDialog(this, "Enter new name:", "Change node name", 3, (Icon)null, (Object[])null, (node).getName());
            if (name != null) {
                node.setName(name);
            }

            repaint();

        });

        popup.add(menuItem);
        popup.show(event.getComponent(), event.getX(), event.getY());
    }

    private void runColorChooser(Component parent) {
        JColorChooser chooser = new JColorChooser(this.currentColor);
        chooser.setPreviewPanel(new JPanel());
        JDialog dialog = JColorChooser.createDialog(parent, "Select color", true, chooser, (a) -> {
            this.currentColor = chooser.getColor();
            chooser.setVisible(false);
        }, (a) -> {
            chooser.setVisible(false);
        });
        dialog.setVisible(true);
    }


}
