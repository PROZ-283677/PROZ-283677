import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**************************
 * Okienko logowania, PROZ
 * @author Kacper Klimczuk 
 * @version v2
 **************************/
public class Main extends Application {
	
	// utworzenie dialog
	private Dialog<ButtonType> dialog = new Dialog<>();
	
	// ustawianie obrazka
	//Image image = new Image(ClassLoader.getSystemResourceAsStream("lock2.png"));
	//ImageView imageView = new ImageView(image);
	//dialog.setGraphic(imageView);
	
	// utworzenie buttonow
	private ButtonType loginButtonType = new ButtonType("Logon");
	private ButtonType anulujButtonType = new ButtonType("Anuluj");
	
	// utworzenie Grid
	private GridPane grid = new GridPane();
	
	// utworzenie Password Field
	private PasswordField password = new PasswordField();
	
	// utworzenie Choice box
	private ChoiceBox<String> choiceBox = new ChoiceBox<>();
	
	// utworzenie Combo box
	private ComboBox<String> comboBox = new ComboBox<>();
	
	private Node loginButton;
	
	// imitacja bazy danych uzytkownikow
	ObservableList<String> produkcyjne = FXCollections.observableArrayList("adam.nowak","ewa.jakas","ktos.inny");
	ObservableList<String> testowe = FXCollections.observableArrayList("jan.janek","alan.wake","nico.gajtan");
	ObservableList<String> deweloperskie = FXCollections.observableArrayList("pawel.pablito","em.ce","tolek.banan");
	ObservableList<String> h_produkcyjne = FXCollections.observableArrayList("123","admin","ja");
	ObservableList<String> h_testowe = FXCollections.observableArrayList("haslo","rower","stopro");
	ObservableList<String> h_deweloperskie = FXCollections.observableArrayList("jola2","adminadmin","tolus");
	
	/**
	 * Procedura uruchamiajaca okno
	 * 
	 * @param args - przekazywany do javafx.application.Application.launch
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Procedura blokuje/odblokowuje logon button w zaleznosci od tego czy srodowisko i uzytkownik wybrany, a haslo wpisane
	 * 
	 * @param args - przekazywany do javafx.application.Application.launch
	 */
	private void changed() {
		loginButton.setDisable(choiceBox.getValue() == null || 
							   comboBox.getEditor().getText().trim().isEmpty() ||
							   password.getText().isEmpty() ); 
	}

	/**
	 * Metoda sprawdza czy srodowisko, nazwa uzytkownika i haslo zgadzaja sie ze soba
	 * 
	 * @param srodowisko - jedna z wartosci: Produkcyjne, Testowe, Deweloperskie
	 * @param uzytkownik - w zaleznosci od srodowiska wybrany uzytkownik, lub wpisany recznie
	 * @param haslo - zawiera haslo wpisane do pola password
	 * @return boolean - jesli srodowisko, nazwa uzytkownika i haslo zgadzaja sie ze soba, zwroci true
	 */
	private boolean isPassCorrect(String srodowisko, String uzytkownik, String haslo) {
		if(choiceBox.getValue() == "Produkcyjne") {
			for(int i=0; i<produkcyjne.size(); ++i){
				if(produkcyjne.get(i).equals(uzytkownik) && h_produkcyjne.get(i).equals(haslo)) return true;
			}
		}
		else if(choiceBox.getValue() == "Testowe") {
			for(int i=0; i<testowe.size(); ++i){
				if(testowe.get(i).equals(uzytkownik) && h_testowe.get(i).equals(haslo)) return true;
			}
		}
		else if(choiceBox.getValue() == "Deweloperskie") {
			for(int i=0; i<deweloperskie.size(); ++i){
				if(deweloperskie.get(i).equals(uzytkownik) && h_deweloperskie.get(i).equals(haslo)) return true;
			}
		}
		return false;
	}
	
	/**
	 * Metoda konwertuje buttontype na pair
	 * 
	 * @param buttonType - zwracane przez okno Dialog
	 * @param loginButtonType - sluzy do sprawdzenia ktory przycisk zostal nacisniety
	 * @return boolean - para uzytkownik, srodowisko, jesli dane sie zgadzaja, null gdy sie nie zgadzaja
	 */
	private Pair<String,String> resultConverter(Optional<ButtonType> buttonType, ButtonType loginButtonType)
	{
		if(buttonType.get() == loginButtonType)
		{
			if(isPassCorrect(choiceBox.getValue(), comboBox.getEditor().getText(), password.getText()))
				    return new Pair<>(choiceBox.getValue(), comboBox.getEditor().getText() );
		}
		return null;
	}

	/**
	 * Metoda ustawiajaca kazdy komponent i pokazujaca rezultat logowania
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// ustawianie elementow dialog
		dialog.setTitle("Logowanie");
		dialog.setHeaderText("Logowanie do systemu STYLEman");
		// dodanie buttonow do dialog
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, anulujButtonType);
		
		// ustawianie grid
		grid.setHgap(50);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 10, 50));
		
		password.setPromptText("Hasło");
		password.textProperty().addListener((obs, oldVal, newVal) -> changed());
		grid.add(new Label("Hasło:"), 0, 2);
		grid.add(password, 1, 2);
		
		choiceBox.getItems().addAll("Produkcyjne","Testowe","Deweloperskie");
		choiceBox.setValue("Produkcyjne");
		choiceBox.valueProperty().addListener((obs, oldVal, newVal) -> changed());
		grid.add(new Label("Środowisko:"), 0, 0);
		grid.add(choiceBox, 1, 0);
		
		comboBox.setItems(produkcyjne);
		comboBox.setEditable(true);
		comboBox.getEditor().textProperty().addListener((obs, oldVal, newVal) -> changed());
		comboBox.setValue("adam.nowak");
		grid.add(new Label("Użytkownik:"), 0, 1);
		grid.add(comboBox, 1, 1);
		
		// loginButton
		loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);
		
		// dodanie zarzadcy do okienka
		dialog.getDialogPane().setContent(grid);
		
		// zaleznie od srodowiska, przypisuje listy uzytkownikow
		choiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldV, newV)-> {
			if(choiceBox.getValue() == "Produkcyjne") comboBox.setItems(produkcyjne);
			else if(choiceBox.getValue() == "Testowe") comboBox.setItems(testowe);
			else comboBox.setItems(deweloperskie);
		});

		Pair<String, String> result = resultConverter(dialog.showAndWait(), loginButtonType);
		
		if(result == null) System.out.println("Logowanie nieudane, haslo, nazwa uzytkownika lub srodowisko nie poprawne");
		else System.out.println("Srodowisko=" + result.getKey() + ", Uzytkownik=" + result.getValue());
	}
} 


