package application;
import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import org.junit.Test;
 
public class TestListCount {

	@Test
	public void test() throws FileNotFoundException {
 
		int returnedWords = TextAnalyzerGUI.getListCount();
		assertEquals(20,returnedWords);
	}

}
      