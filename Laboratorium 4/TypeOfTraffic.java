/*
 *  Symulacja problemu przejazdu przez  most
 *
 *  Autor: Lukasz Mieczyński
 *   Data: 14 grudnia 2020 r.
 */
enum TypeOfTraffic {
    NOLIMITED("Przejazd bez ograniczeń"),
    TWOWAY("Przejazd w obie strony"),
    ONEWAY("Przejazd w jedną stronę"),
    ONEBUS("Jeden bus na moście");


    String typeName;

    private TypeOfTraffic(String type_name) {
        this.typeName = type_name;
    }


    @Override
    public String toString() {
        return typeName;
    }
}
