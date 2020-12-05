package application;
import java.io.FileNotFoundException;
import java.util.List;

public class GetListCount extends TextAnalyzer{
	 /**
	  * The getListCount() method calls the countEachWord() method that returns the all key/value text items in a String list.
	  * The size() method is called on the returned list and subsequently returned after being placed in a variable.
	  * @return Returns count of words in in generic String List.
	  * @throws FileNotFoundException
	  */
	 public static int getListCount() throws FileNotFoundException{
		 List<String> wordSize = countEachWord();
		 int wordCount = wordSize.size();
		 return wordCount;
	 }
}
