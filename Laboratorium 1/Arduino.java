/*
   Program: Operacje na obiektach klasy Arduino
      Plik: Arduino.java

            definicja klasy ArduinoException
            definicja publicznej klasy Arduino

      Autor: Lukasz Mieczyński
      Data:  28 pazdziernik 2020 r.
      Nr indeksu: 256764
 */


import java.io.*;
import java.util.Objects;

enum ComumunicationType{
        UNKNOWN("-------"),
        WIFI("WIFI"),
        BLUETOOTH("BLUETOOTH"),
        MICROUSB("MICROUSB"),
        ETHERNET("ETHERNET"),
        USB("USB");

        String typeName;

        private ComumunicationType(String type_name) {
            this.typeName = type_name;
        }


        @Override
        public String toString() {
            return typeName;
        }
        }
        class ArduinoException extends Exception {
        private static final long serialVersionUID = 1L;

        public ArduinoException(String message) {
            super(message);
        }

    }
    public class Arduino implements Comparable<Arduino>,Serializable{
        private String name;
        private double price;
        private int ram;
        private ComumunicationType type;



        public Arduino(String name1) throws ArduinoException {
            setName(name1);

        }

        public Arduino(String name1, double price1, int ram1, String type_name) {
            this.name = name1;
            this.price = price1;
            this.ram = ram1;
            type = ComumunicationType.UNKNOWN;
        }

        public Arduino() {

        }

        public Arduino(String s, String s1) {
        }


        public String getName() {
            return name;
        }

        public void setName(String name1) throws ArduinoException {
            if ((name1 == null) || name1.equals("")) throw new ArduinoException("Pole <Nazwa> musi być wypełnione.");
            this.name = name1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Arduino)) return false;
            Arduino arduino = (Arduino) o;
            return Double.compare(arduino.price, price) == 0 &&
                    ram == arduino.ram &&
                    Objects.equals(name, arduino.name) &&
                    type == arduino.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, price, ram, type);
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price1) throws ArduinoException {
            if (price1 <= 0) throw new ArduinoException("Pole <Cena> musi być wypełnione liczbami większymi od 0.");
            this.price = price1;
        }

        public void setPrice(String price1) throws ArduinoException {
            if ((price1 == null) || price1.equals("")) throw new ArduinoException("Pole <Cena> musi być wypełnione.");
            try {
                setPrice(Double.parseDouble(price1));
            } catch (NumberFormatException e) {
                throw new ArduinoException("Cena musi być liczbą zmiennoprzecinkową.");
            }
        }

        public int getRam() {
            return ram;
        }

        public void setRam(int ram1) throws ArduinoException {
            if (ram1 <= 0) throw new ArduinoException("Pole <Ram> musi być większe od 0.");
            this.ram = ram1;
        }

        public void setRam(String ram1) throws ArduinoException {
            try {
                setRam(Integer.parseInt(ram1));
            } catch (NumberFormatException e) {
                throw new ArduinoException("Ram musi być liczbą całkowitą.");
            }
        }

        public ComumunicationType getType() {
            return type;
        }

        public void setType(ComumunicationType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return price+ "";
        }


        public String setType(String type_name) throws ArduinoException {
            if (type_name == null || type_name.equals("")) {
                this.type = ComumunicationType.UNKNOWN;
                return type_name;
            }
            for (ComumunicationType type : ComumunicationType.values()) {
                if (type.typeName.equals(type_name)) {
                    this.type = type;
                    return type_name;
                }
            }
            throw new ArduinoException("Nie ma takiego typu.");
        }


        public static void printToFile(PrintWriter writer, Arduino board) {
            writer.println(board.name + ";" + board.price + ";" + board.ram + ";" + board.type+";");
            writer.close();
        }

        public static void printToFile(String file_name, Arduino board) throws ArduinoException {
            try (PrintWriter writer = new PrintWriter(file_name)) {
                printToFile(writer, board);
            } catch (FileNotFoundException e) {
                throw new ArduinoException("Nie odnaleziono pliku " + file_name);
            }
        }

        public static Arduino readFromFile(BufferedReader reader) throws ArduinoException {
            try {
                String line = reader.readLine();
                String[] txt = line.split(";");
                Arduino board = new Arduino();
                board.setName(txt[0]);
                board.setPrice(txt[1]);
                board.setRam(txt[2]);
                board.setType(txt[3]);
                return board;
            } catch (IOException e) {
                throw new ArduinoException("Wystąpił błąd podczas odczytu danych z pliku.");
            }
        }


        public static Arduino readFromFile(String file_name) throws ArduinoException {
            try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
                return Arduino.readFromFile(reader);
            } catch (FileNotFoundException e) {
                throw new ArduinoException("Nie odnaleziono pliku " + file_name);
            } catch (IOException e) {
                throw new ArduinoException("Wystąpił błąd podczas odczytu danych z pliku.");
            }
        }

        @Override
        public int compareTo(Arduino o) {
            return this.name.compareTo(o.getName());
        }
    }




