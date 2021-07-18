
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/*
 *  Symulacja problemu przejazdu przez  most
 *
 *  Autor: Lukasz Mieczyński
 *   Data: 14 grudnia 2020 r.
 */

public abstract class NarrowBridgeConsole extends JDialog implements ActionListener {


    public static void main(String[] args) {
        NarrowBridge bridge = new NarrowBridge();

        while(true) {

            Bus bus = new Bus(bridge);
            (new Thread(bus)).start();

            try {
                Thread.sleep((long)(5500 - bridge.TRAFFIC));
            } catch (InterruptedException var5) {
            }
        }

    }


    }
