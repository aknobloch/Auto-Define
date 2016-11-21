import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;


public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Aaron's Auto Define");
		
		GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(25,25,25,25));
		
		Text welcomeText = new Text("Welcome");
		welcomeText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		root.add(welcomeText, 0, 0, 2, 1);
		
		Label instructions = new Label("Please select a file...");
		root.add(instructions, 0, 1, 2, 1);
		
		TextField filepathField = new TextField();
		filepathField.setEditable(false);
		root.add(filepathField, 0, 2);
		
		Button searchButton = new Button("Search");

		
		HBox hbButton = new HBox();
		hbButton.setAlignment(Pos.BOTTOM_RIGHT);
		hbButton.getChildren().add(searchButton);
		root.add(hbButton, 1, 2);
		
		final Text actionTarget = new Text();
		root.add(actionTarget, 0, 4);
		
		searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				fileChooser.getExtensionFilters().add(
						new ExtensionFilter("Text Files", "*.txt"));
				File selectedFile = fileChooser.showOpenDialog(primaryStage);
				if (selectedFile != null) {
					filepathField.setText(selectedFile.getAbsolutePath());
					
					Runnable fileConvertor = new FileDefiner(selectedFile);
					Thread convertorThread = new Thread(fileConvertor);
					convertorThread.start();
					
					actionTarget.setFill(Color.FIREBRICK);
					
					while(convertorThread.isAlive()) {
						actionTarget.setText("Converting file...");
					}
					
					actionTarget.setText("File Converted.");
					
				} else {
					actionTarget.setText("Error Parsing File.");
				}
				
			}
			
		});
		
		// root.setGridLinesVisible(true);
		
		Scene myScene = new Scene(root, 350, 225);
		primaryStage.setScene(myScene);
		primaryStage.show();
		
	}
}

