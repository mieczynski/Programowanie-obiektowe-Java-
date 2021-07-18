import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/*
 * Program: Aplikacja okienkowa z GUI, która umożliwia zarządzanie
 *          grupami obiektów klasy Person.
 *
 *
 *   Autor: Lukasz Mieczyński
 *    Data: listopad 2020 r.
 */

/*
 *  Typ wyliczeniowy GroupType reprezentuje typy kolekcji,
 *  które mogą być wykorzystane do tworzenia grupy osób.
 *  w programie można wybrać dwa rodzaje kolekcji: listy i zbiory.
 *  Każdy rodzaj kolekcji może być implementowany przy pomocy
 *  różnych klas:
 *      Listy: klasa Vector, klasa ArrayList, klasa LinnkedList;
 *     Zbiory: klasa TreeSet, klasa HashSet.
 */
enum GroupType {
    VECTOR("Lista   (klasa Vector)"),
    ARRAY_LIST("Lista   (klasa ArrayList)"),
    LINKED_LIST("Lista   (klasa LinkedList)"),
    HASH_SET("Zbiór   (klasa HashSet)"),
    TREE_SET("Zbiór   (klasa TreeSet)");

    String typeName;

    private GroupType(String type_name) {
        typeName = type_name;
    }


    @Override
    public String toString() {
        return typeName;
    }


    public static GroupType find(String type_name){
        for(GroupType type : values()){
            if (type.typeName.equals(type_name)){
                return type;
            }
        }
        return null;
    }


    // Metoda createCollection() dla wybranego typu grupy
    // tworzy kolekcję obiektów klasy Person implementowaną
    // za pomocą właściwej klasy z pakietu Java Collection Framework.
    public Collection<Arduino> createCollection() throws ArduinoException {
        switch (this) {
            case VECTOR:      return new Vector<Arduino>();
            case ARRAY_LIST:  return new ArrayList<Arduino>();
            case HASH_SET:    return new HashSet<Arduino>();
            case LINKED_LIST: return new LinkedList<Arduino>();
            case TREE_SET:    return new TreeSet<Arduino>();
            default:          throw new ArduinoException("Podany typ kolekcji nie został zaimplementowany.");
        }
    }

} public class GroupOfBoard implements Iterable<Arduino>, Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private GroupType type;
    private Collection<Arduino> collection;

    public GroupOfBoard(GroupType type, String name) throws ArduinoException {
        setName(name);
        if (type == null) {
            throw new ArduinoException("Nieprawidłowy typ kolekcji.");
        }
        this.type = type;
        collection = this.type.createCollection();

    }

    public GroupOfBoard(String type_name, String name) throws ArduinoException {
        setName(name);
        GroupType type = GroupType.find(type_name);
        if (type == null) {
            throw new ArduinoException("Nieprawidłowy typ kolekcji.");
        }
        this.type = type;
        collection = this.type.createCollection();
    }


    public String getName() {
        return name;
    }


    public void setName(String name) throws ArduinoException {
        if ((name == null) || name.equals(""))
            throw new ArduinoException("Nazwa grupy musi być określona.");
        this.name = name;
    }


    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) throws ArduinoException {
        if (type == null) {
            throw new ArduinoException("Typ kolekcji musi być określny.");
        }
        if (this.type == type)
            return;
        // Gdy następuje zmiana typu kolekcji to osoby zapamiętane
        // w dotychczasowej kolekcji muszą zostać przepisane do nowej
        // kolekcji, która jest implementowana, przy pomocy
        // klasy właściwej dla nowego typu kolekcji.
        Collection<Arduino> oldCollection = collection;
        collection = type.createCollection();
        this.type = type;
        for (Arduino board : oldCollection)
            collection.add(board);
    }

    public void setType(String type_name) throws ArduinoException {
        for (GroupType type : GroupType.values()) {
            if (type.toString().equals(type_name)) {
                setType(type);
                return;
            }
        }
        throw new ArduinoException("Nie ma takiego typu kolekcji.");
    }

    public boolean add(Arduino e) {
        return collection.add(e);
    }


    public Iterator<Arduino> iterator() {
        return collection.iterator();
    }

    public int size() {
        return collection.size();
    }

    public void sortName() throws ArduinoException {
        if (type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
            throw new ArduinoException("Kolekcje typu SET nie mogą być sortowane.");
        }
        // Przy sortowaniu jako komparator zostanie wykorzystana
        // metoda compareTo będąca implementacją interfejsu
        // Comparable w klasie Person.
        Collections.sort((List<Arduino>) collection);
    }


    public void sortPrice() throws ArduinoException {
        if (type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
            throw new ArduinoException("Kolekcje typu SET nie mogą być sortowane.");
        }
        // Przy sortowaniu jako komparator zostanie wykorzystany
        // obiekt klasy anonimowej (klasa bez nazwy), która implementuje
        // interfejs Comparator i zawiera tylko jedną metodę compare.
        Collections.sort((List<Arduino>) collection, new Comparator<Arduino>() {

            @Override
            public int compare(Arduino o1, Arduino o2) {
                if (o1.getPrice() < o2.getPrice())
                    return -1;
                if (o1.getPrice() > o2.getPrice())
                    return 1;
                return 0;
            }

        });
    }

    public void sortType() throws ArduinoException {
        if (type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
            throw new ArduinoException("Kolekcje typu SET nie mogą być sortowane.");
        }
        // Przy sortowaniu jako komparator zostanie wykorzystany
        // obiekt klasy anonimowej (klasa bez nazwy), która implementuje
        // interfejs Comparator i zawiera tylko jedną metodę compare.
        Collections.sort((List<Arduino>) collection, new Comparator<Arduino>() {

            @Override
            public int compare(Arduino o1, Arduino o2) {
                return o1.getType().toString().compareTo(o2.getType().toString());
            }

        });
    }

    @Override
    public String toString() {
        return name + "  [" + type + "]";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupOfBoard)) return false;
        GroupOfBoard arduinos = (GroupOfBoard) o;
        return Objects.equals(collection, arduinos.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection);
    }

    public static void printToFile(PrintWriter writer, GroupOfBoard group) {
        writer.println(group.getName());
        writer.println(group.getType());
        for (Arduino person : group.collection)
            Arduino.printToFile(writer, person);
    }


    public static void printToFile(String file_name, GroupOfBoard group) throws ArduinoException {
        try (PrintWriter writer = new PrintWriter(file_name)) {
            printToFile(writer, group);
        } catch (FileNotFoundException e) {
            throw new ArduinoException("Nie odnaleziono pliku " + file_name);
        }
    }


    public static GroupOfBoard readFromFile(BufferedReader reader) throws ArduinoException {
        try {
            String group_name = reader.readLine();
            String type_name = reader.readLine();
            GroupOfBoard groupOfBoard = new GroupOfBoard(type_name, group_name);

            Arduino board;
            while ((board = Arduino.readFromFile(reader)) != null)
                groupOfBoard.collection.add(board);
            return groupOfBoard;
        } catch (IOException e) {
            throw new ArduinoException("Wystąpił błąd podczas odczytu danych z pliku.");
        }
    }


    public static GroupOfBoard readFromFile(String file_name) throws ArduinoException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
            return GroupOfBoard.readFromFile(reader);
        } catch (FileNotFoundException e) {
            throw new ArduinoException("Nie odnaleziono pliku " + file_name);
        } catch (IOException e) {
            throw new ArduinoException("Wystąpił błąd podczas odczytu danych z pliku.");
        }
    }


    public static GroupOfBoard sum(GroupOfBoard board1, GroupOfBoard board2) throws ArduinoException {
        GroupType type;
        if (board2.collection instanceof Set && !(board1.collection instanceof Set)) {
            type = board2.type;
        } else {
            type = board1.type;
        }
        GroupOfBoard group= new GroupOfBoard(type, board1.getName() + " OR " + board2.getName());
        group.collection.addAll(board1.collection);
        group.collection.addAll(board2.collection);
        return group;

    }

    public static GroupOfBoard product(GroupOfBoard board1, GroupOfBoard board2) throws ArduinoException
    {
        GroupType type;
        if (board2.collection instanceof Set && !(board1.collection instanceof Set)) {
            type = board2.type;
        } else {
            type = board1.type;
        }
        GroupOfBoard g1 = new GroupOfBoard(board1.getType(),"g1");
        GroupOfBoard g2 = new GroupOfBoard(board2.getType(),"g2");
        GroupOfBoard group = new GroupOfBoard(type, board1.getName() + " AND " + board2.getName());
        g1.collection.addAll(board1.collection);
        g2.collection.addAll(board2.collection);
        g1.collection.retainAll(g2.collection);
        group.collection.addAll(g1.collection);

    return group;
    }
public static GroupOfBoard  subtraction(GroupOfBoard board1, GroupOfBoard board2) throws ArduinoException
{GroupType type;
    if (board2.collection instanceof Set && !(board1.collection instanceof Set)) {
        type = board2.type;
    } else {
        type = board1.type;
    }
    GroupOfBoard g1 = new GroupOfBoard(board1.getType(),"g1");
    GroupOfBoard g2 = new GroupOfBoard(board2.getType(),"g2");
    GroupOfBoard group = new GroupOfBoard(type, board1.getName() + " SUB " + board2.getName());
    g1.collection.addAll(board1.collection);
    g2.collection.addAll(board2.collection);
    g1.collection.removeAll(g2.collection);
    group.collection.addAll(g1.collection);
    return group;

}

    public static GroupOfBoard  xsub(GroupOfBoard board1, GroupOfBoard board2) throws ArduinoException
    {GroupType type;
        if (board2.collection instanceof Set && !(board1.collection instanceof Set)) {
            type = board2.type;
        } else {
            type = board1.type;
        }
        GroupOfBoard group = new GroupOfBoard(type, board1.getName() + " XOR " + board2.getName());
        GroupOfBoard g1 = new GroupOfBoard(board1.getType(),"g1");
        GroupOfBoard g2 = new GroupOfBoard(board2.getType(),"g2");
        GroupOfBoard g3 = new GroupOfBoard(board1.getType(),"g1");
        GroupOfBoard g4 = new GroupOfBoard(board2.getType(),"g2");
        g1.collection.addAll(board1.collection);
        g2.collection.addAll(board2.collection);
        g3.collection.addAll(board1.collection);
        g4.collection.addAll(board2.collection);
        g1.collection.removeAll(g2.collection);
        g4.collection.removeAll(g3.collection);
        group.collection.addAll(g4.collection);
        group.collection.addAll(g1.collection);
        return group;

    }
}

