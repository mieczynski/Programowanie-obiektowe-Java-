/*
 * Program: Prosta biblioteka metod do realizacji dialogu z użytkownikiem
 *          w prostych aplikacjach bez graficznego interfejsu użytkownika.
 *    Plik: ConsoleUserDialog.java
 *
 *   Autor: Paweł Rogalinski
 *    Data: pazdziernik 2017 r.
 *
 */

import java.util.Scanner;

/**
 * Klasa <code> ConsoleUserDialog </code> implementuje pomocnicze
 * metody do tworzenia w programie tekstowego interfejsu użytkownika.
 *
 * Do realizacji dialogu z użytkownikiem wykorzystywane są standardowe
 * strumienie wejścia <code>System.in</code>, wyjścia <code> System.out </code>
 * oraz błędów <code> System.err </code>.
 *
 * Program demonstruje następujące zagadnienia:
 * <ul>
 *  <li> wyświetlanie komunikatów tekstowych w oknie konsoli, </li>
 *  <li> czytanie w oknie konsoli danych różnych typów ze standardowego
 *       strumienia wejsciowego za pomocą klasy <code> Scanner </code></li>
 *  <li> konwersję obiektów klasy <code>String</code> na znaki lub liczby
 *       typu <code>char, int, double</code>,
 *  <li> obsługę wyjątków przy konwersji danych</li>
 * </ul>
 *
 * @author Pawel Rogaliński
 * @version 1 październik 2017
 */

public class ConsoleUserDialog {

    /** Komunikat o błędnym formacie wprowadzonych danych. */
    private final String ERROR_MESSAGE = "Nieprawidłowe dane!\nSpróbuj jeszcze raz.";

    /** Pomocniczy obiekt klasy <code> Scanner </code>
     *  do czytania danych w oknie konsoli.
     *
     *  Domyślnie <code>scanner</code> podłączony jest do standardowego
     *  strumienia wejściowego.
     */
    private Scanner scanner = new Scanner(System.in);


    /**
     * Metoda drukuje komunikat do standardowego strumienia wyjściowego.
     *
     * @param message tekst komunikatu.
     */
    public void printMessage(String message) {
        System.out.println(message);
    }


    /**
     * Metoda drukuje komunikat do standardowego strumienia wyjściowego
     * i czeka na potwierdzenie od użytkownika.
     *
     * Po wydrukowaniu komunikatu program jest wstrzymywany do momentu
     * naciśnięcia klawisza ENTER.
     *
     * @param message tekst komunikatu.
     */
    public void printInfoMessage(String message) {
        System.out.println(message);
        enterString("Nacisnij ENTER");
    }


    /**
     * Metoda drukuje komunikat do standardowego strumienia błędów
     * i czeka na potwierdzenie od użytkownika.
     *
     * Po wydrukowaniu komunikatu program jest wstrzymywany do momentu
     * naciśnięcia klawisza ENTER.
     * @param message tekst komunikatu.
     */
    public void printErrorMessage(String message) {
        System.err.println(message);
        System.err.println("Nacisnij ENTER");
        enterString("");
    }

    /**
     * Metoda czyści konsolę tekstową.
     *
     * Metoda faktycznie drukuje trzy puste linie, które
     * tworzą dodatkowy odstęp przed kolejnymi komunikatami.
     */
    public void clearConsole(){
        System.out.println("\n\n");
    }

    /**
     * Metoda umożliwia użytkownikowi wprowadzenie łańcucha znaków.
     *
     * @param prompt tekst zachęty do wprowadzania danych.
     * @return obiekt reprezentujący wprowadzony ciąg znaków.
     */
    public String enterString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Metoda umożliwia użytkownikowi wprowadzenie pojedyńczego znaku.
     *
     * Metoda faktycznie czyta cały łańcuch znaków, z którego
     * wybierany jest tylko pierwszy znak.
     * @param prompt tekst zachęty do wprowadzania danych.
     * @return wprowadzony znak.
     */
    public char enterChar(String prompt) {
        boolean isError;
        char c = ' ';
        do {
            isError = false;
            try {
                c = enterString(prompt).charAt(0);
            } catch (IndexOutOfBoundsException e) {
                System.err.println(ERROR_MESSAGE);
                isError = true;
            }
        } while (isError);
        return c;
    }

    /**
     * Metoda umożliwia użytkownikowi wprowadzenie liczby całkowitej.
     *
     * Metoda faktycznie czyta cały łańcuch znaków, który
     * następnie jest kowertowany na liczbę całkowitą.
     * @param prompt tekst zachęty do wprowadzania danych.
     * @return wprowadzona liczba.
     */
    public int enterInt(String prompt) {
        boolean isError;
        int i = 0;
        do{
            isError = false;
            try{
                i = Integer.parseInt(enterString(prompt));
            } catch(NumberFormatException e){
                System.err.println(ERROR_MESSAGE);
                isError = true;
            }
        }while(isError);
        return i;
    }

    /**
     * Metoda umożliwia użytkownikowi wprowadzenie liczby rzeczywistej.
     *
     * Metoda faktycznie czyta cały łańcuch znaków, który
     * następnie jest kowertowany na liczbę rzeczywistą.
     * @param prompt tekst zachęty do wprowadzania danych.
     * @return wprowadzona liczba.
     */
    public float enterFloat(String prompt) {
        boolean isError;
        float d = 0;
        do{
            isError = false;
            try{
                d = Float.parseFloat(enterString(prompt));
            } catch(NumberFormatException e){
                System.err.println(ERROR_MESSAGE);
                isError = true;
            }
        } while(isError);
        return d;
    }

    /**
     * Metoda umożliwia użytkownikowi wprowadzenie liczby
     * rzeczywistej podwójnej precyzji.
     *
     * Metoda faktycznie czyta cały łańcuch znaków, który
     * następnie jest kowertowany na liczbę rzeczywistą
     * podwójnej precyzji.
     * @param prompt tekst zachęty do wprowadzania danych.
     * @return wprowadzona liczba.
     */
    public double enterDouble(String prompt) {
        boolean isError;
        double d = 0;
        do{
            isError = false;
            try{
                d = Double.parseDouble(enterString(prompt));
            } catch(NumberFormatException e){
                System.err.println(ERROR_MESSAGE);
                isError = true;
            }
        }while(isError);
        return d;
    }

} // koniec kasy ConsoleUserDialog



