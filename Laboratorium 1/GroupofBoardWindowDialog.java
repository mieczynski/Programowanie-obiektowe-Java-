/*Autor: Lukasz Mieczyński
 Data:  19 listopad 2020 r.
 Temat: Laboratorium 3
 Nr indeksu: 256764*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.*;
import java.lang.Object;
import java.util.List;
import  javax.swing.JMenuBar;
import  javax.swing.JFrame;
import  javax.swing.JMenu;
import  javax.swing.JMenuItem;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class GroupofBoardWindowDialog extends JDialog implements ActionListener{





    private static final long serialVersionUID = 1L;

    private static final String GREETING_MESSAGE =
            "Program Arduino - wersja okienkowa\n" +
                    "Autor: Lukasz Mieczyński\n" +
                    "Data:  listopad 2020 r.\n";



    public void main(String[] args) throws ArduinoException {
    new GroupofBoardWindowDialog(this, currentGroup);
    }

    private Arduino currentBoard;
    private GroupOfBoard currentGroup;






    Font font = new Font("Monospace", Font.BOLD,12);

    JMenuBar menuBar;
    JMenu menu, menuOfSortBar, menuOfChangeGroupBar;


    // Etykiety wyświetlane na panelu w głównym oknie aplikacji
    JLabel gruopLabel = new JLabel("      Nazwa grupy: ");
    JLabel typeOfGroupLabel  = new JLabel("  Typ grupy: ");
    JComboBox<GroupType> typeBox = new JComboBox(GroupType.values());




    // Pola tekstowe wyświetlane na panelu w głównym oknie aplikacji
    JTextField groupField = new JTextField(10);
    JTextField typeOfGroupField    = new JTextField(10);
    JTextField testField    = new JTextField(10);


    // Przyciski wyświetlane na panelu w głównym oknie aplikacji
    JButton newButton    = new JButton("Nowa płytka");
    JButton deleteButton = new JButton("Usuń płytkę");
    JButton editButton   = new JButton("Zmień dane");
    JButton saveButton   = new JButton("Zapisz do pliku");
    JButton loadButton   = new JButton("Wczytaj z pliku");
    JButton infoButton   = new JButton("O programie");
    JButton exitButton   = new JButton("Zamknij okno");
    // przyciski wyświetlane w rozwijaym menu
    JMenuItem newBar = new JMenuItem("Nowa płytka");
    JMenuItem deleteBar = new JMenuItem("Usuń płytkę");
    JMenuItem editBar = new JMenuItem("Edytuj płytkę");
    JMenuItem saveBar = new JMenuItem("Zapisz do pliku");
    JMenuItem loadBar = new JMenuItem("Wczytaj z pliku");
    JMenuItem infoBar = new JMenuItem("O programie");

    JMenuItem sortfOfNameBar = new JMenuItem("Sortowanie alfabetyczne");
    JMenuItem sortfOfPriceBar = new JMenuItem("Sortowanie wg ceny");
    JMenuItem sortOfTypeBar = new JMenuItem("Sortowanie wg typu");
    JMenuItem changeTypeOfGroupBar = new JMenuItem("Zmień typ kolekcji");
    JMenuItem changeNameOfGroupBar = new JMenuItem("Zmień nazwę kolekcji");

    ViewGroupList viewList;


    public GroupofBoardWindowDialog(Window parent, GroupOfBoard group) throws ArduinoException {
        super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
        setTitle("ArduinoWindowApp");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500,450);
        setResizable(true);
        setLocationRelativeTo(null);
        menuBar = new JMenuBar();
        menuOfSortBar = new JMenu("Sortowanie");
        menuOfChangeGroupBar = new JMenu("Menu grupy");
        menu = new JMenu("Menu");
        setJMenuBar(menuBar);

this.currentGroup=group;


        gruopLabel.setFont(font);
        typeOfGroupLabel.setFont(font);


        groupField.setEditable(false);
        typeOfGroupField.setEditable(false);


        newButton.addActionListener(this);
        editButton.addActionListener(this);
        saveButton.addActionListener(this);
        loadButton.addActionListener(this);
        deleteButton.addActionListener(this);
        infoButton.addActionListener(this);
        exitButton.addActionListener(this);
//dołączenie słuchaczy do obiektów kalsy JMenuItem
        newBar.addActionListener(this);
        editBar.addActionListener(this);
        saveBar.addActionListener(this);
        loadBar.addActionListener(this);
        deleteBar.addActionListener(this);
        infoBar.addActionListener(this);

        sortfOfNameBar.addActionListener(this);
        sortfOfPriceBar.addActionListener(this);
        sortOfTypeBar.addActionListener(this);
        changeNameOfGroupBar.addActionListener(this);
        changeTypeOfGroupBar.addActionListener(this);

        viewList = new ViewGroupList(currentGroup, 400, 250);
        this.viewList.refreshView();

        // Dodanie i rozmieszczenie na panelu wszystkich
        // komponentów GUI.
        JPanel panel = new JPanel();
        panel.add(gruopLabel);
        panel.add(groupField);

        panel.add(typeOfGroupLabel);
        panel.add(typeOfGroupField);
        panel.add(viewList);
        panel.add(newButton);
        panel.add(deleteButton);
        panel.add(editButton);
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(infoButton);
        panel.add(exitButton);
//dodanie rozwijanego menu do paska
        menuBar.add(menu);
        menuBar.add(menuOfSortBar);
        menuBar.add(menuOfChangeGroupBar);
        //dodanie obiektów kalsy JItemMenu do rozwijanego menu
        menu.add(newBar);
        menu.add(deleteBar);
        menu.add(editBar);
        menu.add(saveBar);
        menu.add(loadBar);
        menu.add(infoBar);
        menu.addSeparator();

        menuOfSortBar.add(sortfOfNameBar);
        menuOfSortBar.add(sortfOfPriceBar);
        menuOfSortBar.add(sortOfTypeBar);
        menuOfChangeGroupBar.add(changeNameOfGroupBar);
        menuOfChangeGroupBar.add(changeTypeOfGroupBar);


        setContentPane(panel);
        showCurrentGroup();
        setVisible(true);

    }
    void showCurrentGroup() {
        if (currentGroup == null) {
            groupField.setText("");
            typeOfGroupField.setText("");

        } else {
            groupField.setText(currentGroup.getName());
            typeOfGroupField.setText("" + currentGroup.getType());

        }
    }

    public static GroupOfBoard createNewGroupOfBoard(Window parent) throws ArduinoException {
        String name = enterGroupName(parent);
        GroupOfBoard new_group;

        if ((name == null) || (name.equals(""))) {
            return null;
        }
        GroupType type = chooseGroupType(parent, null);
        if (type == null) {
            return null;
        }
        try
        {
            new_group = new GroupOfBoard(type, name);
        }
        catch (ArduinoException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Błąd", 0);
            return null;
        }
        GroupofBoardWindowDialog dialog;
        dialog = new GroupofBoardWindowDialog(parent, new_group);
        return dialog.currentGroup;
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        // Odczytanie referencji do obiektu, który wygenerował zdarzenie.
        Object eventSource = event.getSource();

        try {
            if (eventSource == newButton || eventSource == newBar) {
                currentBoard = ArduinoWindowDialog.createNewBoard(this, currentBoard);
                if (currentBoard != null) {
                    currentGroup.add(currentBoard);
                }

            }
            if (eventSource == editButton || eventSource == editBar) {
                int index = viewList.getSelectedIndex();
                if (index >= 0) {
                    Iterator<Arduino> iterator = currentGroup.iterator();
                    while (index-- > 0)
                        iterator.next();
                    ArduinoWindowDialog.changeBoardData(this, iterator.next());
                }

            }

            if (eventSource == deleteButton|| eventSource == deleteBar) {
                int index = viewList.getSelectedIndex();
                if (index >= 0) {
                    Iterator<Arduino> iterator = currentGroup.iterator();
                    while (index-- >= 0)
                        iterator.next();
                    iterator.remove();
                }
            }
            if (eventSource == saveButton|| eventSource == saveBar) {

                int index = viewList.getSelectedIndex();
                if (index >= 0) {
                    Iterator<Arduino> iterator = currentGroup.iterator();
                    while (index-- > 0)
                        iterator.next();
                    JFileChooser fc= new JFileChooser();
                    fc.setCurrentDirectory(new File("."));
                    if(fc.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){

                        File fileName = fc.getSelectedFile();
                        String plik = "";
                        plik=("" + fileName);
                        Arduino.printToFile(plik, iterator.next());}}
            }
            if (eventSource == loadButton|| eventSource == loadBar) {
                JFileChooser fc= new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
                {
                    File fileName = fc.getSelectedFile();
                    String plik = "";
                    plik=("" + fileName);
                    currentBoard = Arduino.readFromFile(plik);
                    currentGroup.add(currentBoard);
                }}


            if (eventSource == infoButton|| eventSource == infoBar) {
                JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
            }
            if (eventSource == exitButton){
                dispose();
            }
            if ( eventSource == sortfOfNameBar)
            {
                currentGroup.sortName();
            }
            if ( eventSource == sortfOfPriceBar)
            {
                currentGroup.sortPrice();
            }
            if ( eventSource == sortOfTypeBar)
            {
                currentGroup.sortType();
            }
            if ( eventSource == changeNameOfGroupBar)
            {
                currentGroup.setName(JOptionPane.showInputDialog(this, "Podaj nową nazwę kolekcji"));
            }
            if ( eventSource == changeTypeOfGroupBar){
                Object[] groups = GroupType.values();
                currentGroup.setType( (GroupType) JOptionPane.showInputDialog(this   ,"Podaj nowy typ","Typ kolekcji",JOptionPane.QUESTION_MESSAGE,
                        null,
                        groups,
                        null));


            }
        }
        catch (ArduinoException e) {
            // Tu są wychwytywane wyjątki zgłaszane przez metody klasy Arduino
            // gdy nie są spełnione ograniczenia nałożone na dopuszczelne wartości
            // poszczególnych atrybutów.
            // Wyświetlanie modalnego okna dialogowego
            // z komunikatem o błędzie zgłoszonym za pomocą wyjątku ArduinoException.
            JOptionPane.showMessageDialog(this, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }

        // Aktualizacja zawartości wszystkich pól tekstowych.
        showCurrentGroup();
        this.viewList.refreshView();
    }
    private static String enterGroupName(Window parent)
    {
        return JOptionPane.showInputDialog(parent, "Podaj nazwę dla tej grupy: ");
    }

    private static GroupType chooseGroupType(Window parent, GroupType current_type)
    {
        Object[] possibilities = GroupType.values();
        GroupType type = (GroupType)JOptionPane.showInputDialog(
                parent,
                "Wybierz typ kolekcji (Lista, Zbiór) \n i sposób implementacji:",

                "Zmień typ kolekcji",
                3, null, possibilities,

                current_type);
        return type;
    }



}

class ViewGroupList extends JScrollPane {
    private static final long serialVersionUID = 1L;

    private GroupOfBoard boards;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewGroupList(GroupOfBoard boards, int width, int height){
        this.boards=boards;
        setPreferredSize(new Dimension(width, height));
        setBorder(BorderFactory.createTitledBorder("Lista płytek:"));

        String[] tableHeader = { "Nazwa", "cena", "Ilość RAM","Typ komunikacji" };
        tableModel = new DefaultTableModel(tableHeader, 0);
        table = new JTable(tableModel) {

            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; // Blokada możliwości edycji komórek tabeli
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        setViewportView(table);
    }

    void refreshView(){
        this.tableModel.setRowCount(0);
        for (Arduino currentBoard : boards) {
            if (currentBoard != null) {

                String[] row = { currentBoard.getName(), String.valueOf(currentBoard.getPrice()), String.valueOf(currentBoard.getRam()), String.valueOf(currentBoard.getType())};
                this.tableModel.addRow(row);
            }
        }
    }

    int getSelectedIndex(){
        int index = table.getSelectedRow();
        if (index<0) {
            JOptionPane.showMessageDialog(this, "Żadana grupa nie jest zaznaczona.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return index;
    }


    }