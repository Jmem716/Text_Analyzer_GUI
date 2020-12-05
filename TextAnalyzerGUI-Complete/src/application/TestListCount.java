package application;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import org.junit.Test;

public class TestListCount extends GetListCount{

	@Test
	public void test() throws FileNotFoundException {

		int returnedWords = GetListCount.getListCount();
		assertEquals(20, returnedWords);
	}

}
