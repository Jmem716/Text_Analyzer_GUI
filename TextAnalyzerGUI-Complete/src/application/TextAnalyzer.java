package application;

import java.util.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 
 * The TextAnalyzerGUI class creates a word occurrence program and adds it to a
 * Graphical User Interface. The program reads a text file and prints out the
 * top 20 words with most frequency followed by its respective count. The
 * Graphical User Interface presents a user with the options to either print out
 * the file content or the word occurrence list.
 * 
 * @author Jaime Maldonado
 *
 */
public class TextAnalyzer extends Application {

	static Text text = new Text("Choose an action:");

	@Override
	public void start(Stage primaryStage) throws Exception {

		Button button1 = new Button("View Text");
		Button button2 = new Button("Count Word Frequency");

		text.setFont(new Font(14));

		GridPane layout = new GridPane();
		layout.setVgap(5);
		layout.setHgap(5);
		layout.setAlignment(Pos.CENTER);
		layout.add(button1, 0, 2);
		layout.add(button2, 1, 2);
		layout.add(text, 0, 0);

		Scene scene = new Scene(layout, 400, 200);
		primaryStage.setTitle("Text Analyzer");
		primaryStage.setScene(scene);
		primaryStage.show();

		// =============================================EVENT HANDLER FOR UPLOADED FILE=====================================================//

		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				StringBuilder stringBuffer = new StringBuilder();
				BufferedReader bufferedReader = null;

				try {

					bufferedReader = new BufferedReader(new FileReader("testfile.txt"));

					String readText;
					while ((readText = bufferedReader.readLine()) != null) {
						stringBuffer.append(readText + "\n\n");
					}

					Text readFile = new Text(stringBuffer.toString());
					readFile.setWrappingWidth(700);
					GridPane readFileLayout = new GridPane();
					readFileLayout.add(readFile, 2, 2);
					ScrollPane sp = new ScrollPane();
					sp.setFitToWidth(true);
					readFileLayout.add(sp, 0, 1);

					Scene scene2 = new Scene(readFileLayout, 1000, 1000);
					primaryStage.setTitle("Uploaded Text File");
					primaryStage.setScene(scene2);
					primaryStage.show();

				} catch (FileNotFoundException ex) {

				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
		};
		button1.addEventFilter(ActionEvent.ACTION, eventHandler);

		// =============================================EVENT HANDLER FOR WORD FREQUENCY BUTTON ==========================================//
		EventHandler<ActionEvent> eventHandler2 = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				try {

					ObservableList<String> observableList = FXCollections.observableList(countEachWord());
					Text wordFrequencies = new Text(observableList.toString().replaceAll(",", "\n"));
					wordFrequencies.setFont(Font.font(null, FontWeight.BOLD, 15));
					wordFrequencies.setX(150);
					wordFrequencies.setY(50);
					wordFrequencies.setWrappingWidth(200);
					Group root = new Group(wordFrequencies);
					Scene scene3 = new Scene(root, 500, 500);
					primaryStage.setTitle("Word Frequency & Count");
					primaryStage.setScene(scene3);
					primaryStage.show();

				} catch (FileNotFoundException e1) {

					e1.printStackTrace();
				}
			}
		};
		button2.addEventFilter(ActionEvent.ACTION, eventHandler2);

	}

	/**
	 * The countEachWord() method scans and reads content of a text file. The
	 * content is scanned using a while loop that is tasked with placing all
	 * collected text strings inside a HashMap object. The HashMap is then sorted
	 * and placed in a TreeMap object.
	 * 
	 * @return The countEachWord() method returns the completed key/value collection
	 *         that is previously added to a string ArrayList.
	 * @throws FileNotFoundException
	 */
	public static List<String> countEachWord() throws FileNotFoundException {

		Map<String, Integer> words = new HashMap<String, Integer>();
		ValueComparator compared = new ValueComparator(words);
		TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(compared);
		Scanner file = new Scanner(new File("testfile.txt"));
		List<String> wordList = new ArrayList<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wordOccurrences", "root","Milam88@@");

			Statement myStmt = myConn.createStatement();

			int counter = 0;
			
			// Reads file and increments word frequency counter
			while (file.hasNext()) {
				String word = file.next();
				
				Integer count = words.get(word); //Returns NULL if string not in HashMap (First Iteration is NULL)

				if(count != null) {
					count++;//String exists in HashMap, adding to frequency count
				}
				else
					count = 1;
					words.put(word, count);//Adding `string & count to HashMap
			
				counter++;
				String query1 = "INSERT INTO wordOccurrences.word(word) VALUES ('"+word.replace('\'', ' ')+"')";
				if( counter % 100 == 0 ){
					Scanner myObj = new Scanner(System.in);  // Create a Scanner object
				    System.out.println("Enter new word");
				    String newWord = myObj.nextLine(); 
				    String query2 = "INSERT INTO wordOccurrences.word(word) VALUES ('"+newWord.replace('\'', ' ')+"')";
				    myStmt.executeUpdate(query2);
				}
				myStmt.executeUpdate(query1);
			}

			ResultSet myRs = myStmt.executeQuery("select * from word");  
			
			while(myRs.next()) {
				System.out.println(myRs.getString(2)); 
			}
			
			file.close();

			sorted_map.putAll(words); // Puts all key/value pairs in sorted TreeMap

			int i = 1;

			for (String key : sorted_map.keySet()) {// Prints key/value pair
				if (i > 20)
					break; // Show top 20
				String value = words.get(key).toString();
				key = key.replaceAll("[ .]", " ").replaceAll("\"", "").replaceAll(",", "");// Removes unwanted characters
				System.out.println("(" + i + ") " + key + " - " + value);
				wordList.add("(" + i + ") " + key + " - " + value);
				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}

		return wordList;

	}

	public static void main(String[] args) throws FileNotFoundException {
		launch(args);
		countEachWord();
	}
}
