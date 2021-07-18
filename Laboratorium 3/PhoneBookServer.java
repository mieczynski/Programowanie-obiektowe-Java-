/*
 *
 *  Autor: Lukasz Mieczyński
 *   Data: 30 grudnia 2020r.
 */


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


class PhoneBookServer extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    static final int SERVER_PORT = 25000;

    public static void main(String [] args){
        new PhoneBookServer();
    }
static PhoneBook book = new PhoneBook();
    private JLabel clientLabel   = new JLabel("Odbiorca:");
    private JLabel textAreaLabel = new JLabel("Dialog:");
    private JComboBox<ClientThread> clientMenu = new JComboBox<ClientThread>();
    private JTextArea  textArea  = new JTextArea(15,18);
    private JScrollPane scroll = new JScrollPane(textArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    PhoneBookServer(){
        super("SERWER");
        setSize(300,340);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.add(clientLabel);
        clientMenu.setPrototypeDisplayValue(new ClientThread("#########################"));
        panel.add(clientMenu);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        panel.add(textAreaLabel);
        textArea.setEditable(false);
        panel.add(scroll);
        setContentPane(panel);
        setVisible(true);
        new Thread(this).start();// Uruchomienie dodatkowego watka
        // czekajacego na nowych klientow
    }

    synchronized public void printReceivedMessage(ClientThread client, String message){
        String text = textArea.getText();
        textArea.setText(client.getName() + " >>> " + message + "\n" + text);
    }

    synchronized public void printSentMessage(ClientThread client, String message){
        String text = textArea.getText();
        textArea.setText(client.getName() + " <<< " + message + "\n" + text);
    }

    synchronized void addClient(ClientThread client){
        clientMenu.addItem(client);
    }

    synchronized void removeClient(ClientThread client){
        clientMenu.removeItem(client);
    }



    public void run() {
        boolean socket_created = false;

        // inicjalizacja po��cze� sieciowych
        try (ServerSocket serwer = new ServerSocket(SERVER_PORT)) {
            String host = InetAddress.getLocalHost().getHostName();
            System.out.println("Serwer zosta� uruchomiony na hoscie " + host);
            socket_created = true;
            // koniec inicjalizacji po��cze� sieciowych

            while (true) {  // oczekiwanie na po��czenia przychdz�ce od klient�w
                Socket socket = serwer.accept();
                if (socket != null) {
                    // Tworzy nowy w�tek do obs�ugi klienta, kt�re
                    // w�a�nie po��czy� si� z serwerem.
                    new ClientThread(this, socket);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            if (!socket_created) {
                JOptionPane.showMessageDialog(null, "Gniazdko dla serwera nie może byc utworzone");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
            }
        }
    }
} // koniec klasy MyServer



class ClientThread implements Runnable {
    private Socket socket;
    private String name;
    private PhoneBookServer myServer;


    private ObjectOutputStream outputStream = null;

    // UWAGA: Ten konstruktor tworzy nieaktywny obiekt ClientThread,
    // kt�ry posiada tylko nazw� prototypow�, potrzebn� dla
    // metody setPrototypeDisplayValue z klasy JComboBox
    ClientThread(String prototypeDisplayValue){
        name = prototypeDisplayValue;
    }

    ClientThread(PhoneBookServer server, Socket socket) {
        myServer = server;
        this.socket = socket;
        new Thread(this).start();  // Utworzenie dodatkowego watka
        // do obslugi komunikacji sieciowej
    }

    public String getName(){ return name; }

    public String toString(){ return name; }

    public void sendMessage(String message){
        try {
            outputStream.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        String message;
        try( ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream()); )
        {
            outputStream = output;
            name = (String)input.readObject();
            myServer.addClient(this);
            while(true){
                message = (String)input.readObject();
                String[] actualValue = message.split(" ");
                myServer.printReceivedMessage(this,message);
                if (actualValue[0].equals("BYE")&& actualValue.length==1){
                    myServer.removeClient(this);
                    input.close();
                    output.close();
                    socket.close();
                    socket=null;
                }
                else if (actualValue[0].equals("CLOSE")&& actualValue.length==1)
                {
                    myServer.removeClient(this);
                    System.exit(0);
                    socket.close();
                    socket=null;
                }
                else if (actualValue[0].equals("GET")&& actualValue.length==2) {
                    this.sendMessage(PhoneBookServer.book.GET(actualValue[1]));
                }
                else if (actualValue[0].equals("PUT") && actualValue.length==3) {
                    this.sendMessage(PhoneBookServer.book.PUT(actualValue[1],actualValue[2]));
            }
                else if (actualValue[0].equals("REPLACE") && actualValue.length==3) {
                    this.sendMessage(PhoneBookServer.book.REPLACE(actualValue[1], actualValue[2]));
                } else if (actualValue[0].equals("LIST")&& actualValue.length==1) {
                    this.sendMessage(PhoneBookServer.book.LIST());
                }
                else if (actualValue[0].equals("DELETE") && actualValue.length==2) {
                    this.sendMessage(PhoneBookServer.book.DELETE(actualValue[1]));}
                else if (actualValue[0].equals("LOAD")&& actualValue.length==2){
                    if (PhoneBookServer.book!=null)
                        PhoneBookServer.book.book.clear();
                    this.sendMessage(PhoneBookServer.book.readFromFile(actualValue[1]));

                }
                else if (actualValue[0].equals("SAVE") && actualValue.length==2)
                {
                    this.sendMessage(PhoneBookServer.book.writeToFile(actualValue[1]));

                }
            }

        }
         catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

} // koniec klasy ClientThread