/*
        *  Symulacja problemu przejazdu przez wąski most
        *  Wersja okienkowa
        *
        *  Autor: Lukasz Mieczyński
        *   Data: 14 grudnia 2020 r.
        */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

enum BusDirection{
    EAST,
    WEST;

    @Override
    public String toString(){
        switch(this){
            case EAST: return "W";
            case WEST: return "Z";
        }
        return "";
    }
} // koniec typu wyliczeniowego BusDirection



public class NarrowBridge extends JFrame{

    List<Bus> allBuses = new LinkedList<Bus>();

    List<Bus> busesWaiting = new LinkedList<Bus>();

    List<Bus> busesOnTheBridge = new LinkedList<Bus>();
    public static int TRAFFIC=2725    ;

    Font font = new Font("MonoSpaced", Font.BOLD, 12);
    private static final long serialVersionUID = 1L;

    // Etykiety wyświetlane na panelu
    JLabel limitLabel = new JLabel("Ograniczenie ruchu: ");
    JLabel intensityLabel  = new JLabel(" Natężenie ruchu: ");
    JLabel busOnBridgeLabel      = new JLabel("Busy na moście: ");
    JLabel queueLabel       = new JLabel("Kolejka: ");

    // Pola tekstowe wyświetlane na panelu w głównym oknie aplikacji
    JComboBox<TypeOfTraffic> limitBox = new JComboBox<TypeOfTraffic>(TypeOfTraffic.values());
    final JSlider intensitySlider = new JSlider(0, 500, 5000, TRAFFIC);
    JTextField busOnBrigeField;
    JTextField queueField;

int type;
int buses=0;
BusDirection curDir;
    JTextArea textArea;
    TypeOfTraffic trafficType=TypeOfTraffic.NOLIMITED;
    void printBridgeInfo(Bus bus, String message){
        StringBuilder sb = new StringBuilder();
        sb.append("Bus["+bus.id+"->"+bus.dir+"]  ");
        sb.append(message+"\n");
        textArea.insert(sb.toString(),0);
        sb = new StringBuilder();
        for(Bus b: busesOnTheBridge)
            sb.append(b.id + "  ");
        this.busOnBrigeField.setText(sb.toString());


        sb = new StringBuilder();

        for(Bus b: busesWaiting)
            sb.append(b.id + "  ");
        this.queueField.setText(sb.toString());

    }

    synchronized void getOnTheBridge(Bus bus) {
        boolean Message = true;

        label1:
        while(true) {
            switch(choiceOfType()) {
                case 1:
                    break label1;
                case 2:
                    if (busesOnTheBridge.size() < 5) {
                        break label1;
                    }
                    break;
                case 3:
                    if (busesWaiting.isEmpty() &&busesOnTheBridge.isEmpty() ) {
                        buses = 0;
                        break label1;
                    }

                    if (busesOnTheBridge.isEmpty()) {
                        if (bus.dir == curDir || bus.dir != curDir && buses < 8) {
                            break label1;
                        }
                    } else if (bus.dir == curDir && buses < 8 && busesOnTheBridge.size() < 4) {
                        break label1;
                    }
                    break;
                case 4:
                    if (busesOnTheBridge.isEmpty()) {
                        break label1;
                    }
            }

            this.busesWaiting.add(bus);
            if (Message)
            {
                printBridgeInfo(bus, "CZEKA NA WJAZD");
                Message = false;
            }

            try {
                wait();
            } catch (InterruptedException e) { }

            busesWaiting.remove(bus);
        }

        if (curDir == bus.dir) {
            ++buses;
        } else {
            curDir = bus.dir;
            buses = 1;
        }
        busesOnTheBridge.add(bus);
        printBridgeInfo(bus, "WJEŻDŻA NA MOST");
    }

    // Procedura monitora, która rejestruje busy opuszczające most
    // i powiadamia inne busy oczekujące w kolejce na wjazd
    synchronized void getOffTheBridge(Bus bus){
        // usunięcie busa z listy poruszających się przez most
        busesOnTheBridge.remove(bus);
        printBridgeInfo(bus, "OPUSZCZA MOST");
        // powiadomienie innych oczekujacych.
        notify();
    }

    NarrowBridge() {
        textArea = new JTextArea(21, 50);
        setSize(420, 600);
        setDefaultCloseOperation(3);
        this.busOnBrigeField = new JTextField(30);
        this.queueField = new JTextField(30);

        setResizable(false);
        JMenuItem menuAutor = new JMenuItem("Autor");
        menuAutor.addActionListener((action) -> {
            JOptionPane.showMessageDialog(this, "Autor: Łukasz Mieczyński\n Data: 14 grudnia 2020 r.");
        });
        JMenuItem menuExit = new JMenuItem("Zakończ");
        menuExit.addActionListener((action) -> {
            System.exit(0);
        });

        this.limitBox.setSelectedItem(this.trafficType);
        this.limitBox.addActionListener((a) -> {
            this.trafficType = (TypeOfTraffic) this.limitBox.getSelectedItem();
        });
        JMenu menu = new JMenu("Menu");
        menu.add(menuAutor);
        menu.add(menuExit);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        setJMenuBar(menuBar);
        intensitySlider.setFont(font);
        intensitySlider.setSize(300, 20);
        intensitySlider.setMajorTickSpacing(1000);
        intensitySlider.setMinorTickSpacing(500);
        intensitySlider.setPaintLabels(true);
        Hashtable<Integer, JLabel> labelTable = new Hashtable();
        labelTable.put(500, new JLabel("Małe"));
        labelTable.put(2725, new JLabel("Średnie"));
        labelTable.put(5000, new JLabel("Duże"));
        intensitySlider.setLabelTable(labelTable);
        intensitySlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                NarrowBridge.TRAFFIC = intensitySlider.getValue();
            }
        });


        limitLabel.setFont(font);
        intensityLabel.setFont(font);
        busOnBridgeLabel.setFont(font);
        queueLabel.setFont(font);
        textArea.setFont(font);
        busOnBrigeField.setFont(font);
        queueField.setFont(font);
        JPanel panel = new JPanel();

        panel.add(limitLabel);
        panel.add(limitBox);
        panel.add(intensityLabel);
        panel.add(intensitySlider);

        panel.add(busOnBridgeLabel);
        panel.add(busOnBrigeField);
        panel.add(queueLabel);
        panel.add(queueField);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scroll_bars = new JScrollPane(textArea, 22, 30);
        panel.add(scroll_bars);

        // Umieszczenie Panelu w oknie dialogowym.
        setContentPane(panel);
        setVisible(true);
    }

    int choiceOfType()
    {
        if (trafficType==TypeOfTraffic.NOLIMITED)
        {type=1;}
        else if(trafficType==TypeOfTraffic.TWOWAY)
        {type=2;}
        else if(trafficType==TypeOfTraffic.ONEWAY)
        {type=3;}
        else if(trafficType==TypeOfTraffic.ONEBUS)
        {type=4;}

        return type;
    }



}  // koniec klasy NarrowBridge