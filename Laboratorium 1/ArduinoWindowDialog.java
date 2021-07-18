/*Autor: Lukasz Mieczyński
 Data:  28 pazdziernik 2020 r.
 Temat: Laboratorium 2
 Nr indeksu: 256764*/
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ArduinoWindowDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private Arduino board;
    Font font = new Font("MonoSpaced", Font.BOLD, 12);

    // Etykiety wyświetlane na panelu
    JLabel nameLabel = new JLabel("      Nazwa: ");
    JLabel priceLabel  = new JLabel("  Cena: ");
    JLabel ramLabel      = new JLabel("   Ilość ram: ");
    JLabel typeLabel       = new JLabel("Typ komunikacji: ");

    // Pola tekstowe wyświetlane na panelu w głównym oknie aplikacji
    JTextField nameField = new JTextField(10);
    JTextField priceField    = new JTextField(10);
    JTextField ramField    = new JTextField(10);
    JComboBox<ComumunicationType> typeBox = new JComboBox<ComumunicationType>(ComumunicationType.values());


    // Przyciski wyświetlane na panelu
    JButton OKButton = new JButton("  OK  ");
    JButton CancelButton = new JButton("Anuluj");
    private ArduinoWindowDialog(Window parent, Arduino board) {

        super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(230, 200);
        setLocationRelativeTo(parent);
this.board=board;

if (board==null){
    setTitle("Nowa płytka");
}
else{
    setTitle(board.toString());
    nameField.setText(board.getName());
    priceField.setText(""+board.getPrice());
    ramField.setText(""+board.getRam());
    typeBox.setSelectedItem(board.getType());

    nameLabel.setFont(font);
    priceLabel.setFont(font);
    ramLabel.setFont(font);
    typeLabel.setFont(font);
}
        OKButton.addActionListener( this );
        CancelButton.addActionListener( this );
        // Zmiana koloru tła głównego panelu okna dialogowego
        JPanel panel = new JPanel();
        panel.setBackground(Color.blue);

        // Dodanie i rozmieszczenie na panelu wszystkich komponentów GUI.
        panel.add(nameLabel);
        panel.add(nameField);

        panel.add(priceLabel);
        panel.add(priceField);

        panel.add(ramLabel);
        panel.add(ramField);

        panel.add(typeLabel);
        panel.add(typeBox);

        panel.add(OKButton);
        panel.add(CancelButton);

        // Umieszczenie Panelu w oknie dialogowym.
        setContentPane(panel);


        setVisible(true);

}
    @Override
    public void actionPerformed(ActionEvent event) {
        // Odczytanie referencji do obiektu, który wygenerował zdarzenie.
        Object source = event.getSource();

        if (source == OKButton) {
            try {
                if (board == null) { // utworzenie nowej płytki
                    board = new Arduino(nameField.getText(), priceField.getText());
                    board.setName(nameField.getText());
                    board.setPrice(priceField.getText());
                    board.setRam(ramField.getText());
                    board.setType((ComumunicationType) typeBox.getSelectedItem());
                } else {
                    board.setName(nameField.getText());
                    board.setPrice(priceField.getText());
                }
                // Aktualizacja pozostałych danych
                board.setRam(ramField.getText());
                board.setType((ComumunicationType) typeBox.getSelectedItem());

                // Zamknięcie okna i zwolnienie wszystkich zasobów.
                dispose();
            } catch (ArduinoException e) {
                // Tu są wychwytywane wyjątki zgłaszane przez metody klasy Arduino
                // gdy nie są spełnione ograniczenia nałożone na dopuszczelne wartości
                // poszczególnych atrybutów.
                // Wyświetlanie modalnego okna dialogowego
                // z komunikatem o błędzie zgłoszonym za pomocą wyjątku ArduinoException.
                JOptionPane.showMessageDialog(this, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (source == CancelButton) {
            // Zamknięcie okna i zwolnienie wszystkich zasobów.
            dispose();
        }
    }

    public static Arduino createNewBoard(Window parent, Arduino currentBoard) {
        ArduinoWindowDialog dialog = new ArduinoWindowDialog(parent, null);
        return dialog.board;


    }
    public static void changeBoardData(Window parent, Arduino board) {
        new ArduinoWindowDialog(parent, board);
    }



}