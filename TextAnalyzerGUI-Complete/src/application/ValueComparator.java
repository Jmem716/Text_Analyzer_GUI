package application;
import java.util.Comparator;
import java.util.Map;

/**
 * The ValueComparator class implements the Comparator interface and implements the comparator method that is used to compare two argumentds for order.
 * @author Jaime Maldonado
 *
 */
class ValueComparator implements Comparator<String> {
    Map<String, Integer> words;

    public ValueComparator(Map<String, Integer> words) {
        this.words = words; 
    }

    // Received two (key/value) arguments and compares both
    public int compare(String a, String b) {
        if (words.get(a) >= words.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}