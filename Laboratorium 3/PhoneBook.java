/*
 *
 *  Autor: Lukasz Mieczyński
 *   Data: 30 grudnia 2020r.
 */

import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class PhoneBook implements Serializable {
    ConcurrentHashMap<String, String> book = new ConcurrentHashMap<>();

    public static String writeToFile(String fileName) throws Throwable {
        try {
            Throwable var2 = null;
            Object var3 = null;

            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));

                try {
                    outputStream.writeObject(PhoneBookServer.book);
                } finally {
                    if (outputStream != null) {
                        outputStream.close();
                    }

                    return "OK";
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
            return ("ERROR Nie znaleziono pliku <" + fileName + ">");
        } catch (IOException var16) {
            return ("ERROR Wystąpił błąd przy zapisie pliku <" + fileName + ">");
        }
    }


    public static String readFromFile(String file_name) throws Exception {
        try {
            ObjectInputStream o = new ObjectInputStream(new FileInputStream(file_name));
            PhoneBook obj = (PhoneBook) o.readObject(); // odczyt obiektu ze strumienia
            o.close();
            PhoneBookServer.book.book.putAll(obj.book); // wyświetlenie zawartości pliku
            return "OK";
        } catch (FileNotFoundException exception){
        return "ERROR podany plik nie istnieje";
    } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }






public String GET(String name)
    {if (book.containsKey(name)){
        return "OK "+ book.get(name);}
        else
            {
            return "ERROR podana osoba nie istnieje";
            }
    }


  public  String PUT(String name, String phoneNumber)
  {
book.put(name,phoneNumber);
return "OK";
  }
public String REPLACE(String name, String phoneNumber)
{
    if (book.containsKey(name))
    {
        book.put(name, phoneNumber);
        return "OK";
    }
    else{
        return "ERROR dana osoba nie istnieje";
    }
}
    public String DELETE(String name)
    {
        if (book.containsKey(name))
        {book.remove(name);
            return "OK";}
        else{
            return "ERROR dana osoba nie istnieje";
        }
    }
public String LIST()
{if (!book.isEmpty()){
    return "OK" + " "+book.keySet();}
    else
{
    return "ERROR książka jest pusta";
}
}

}


