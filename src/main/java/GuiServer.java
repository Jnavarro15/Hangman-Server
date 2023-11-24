
import java.util.HashMap;

import com.sun.org.apache.xpath.internal.objects.XBoolean;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{

	static int portNum;
	
	TextField  portNumber, c1;
	Button b1, pButton;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox, portBox;
	Scene startScene;
	BorderPane startPane;
	Server serverConnection;
	Text portTop;
	Stage primaryStage;
	Client clientConnection;

	ListView<String> listItems, listItems2;


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	public int getPortNum() {
		return Integer.parseInt(portNumber.getText());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Server");
		sceneMap = new HashMap<>();

		sceneMap.put("LandingPage", createPortGui());
		sceneMap.put("ServerPage", createServerGui());



		primaryStage.setScene(sceneMap.get("LandingPage"));
		primaryStage.show();
	}

	public Scene createPortGui(){
		BorderPane pane = new BorderPane();
		portNumber = new TextField("Enter Port Number:");

		pButton = new Button("Connect");
		portBox = new VBox(150);
		listItems = new ListView<String>();
		listItems2 = new ListView<String>();

		portBox.setAlignment(Pos.CENTER);
		portBox.getChildren().addAll(portNumber, pButton);
		pane.setCenter(portBox);


		pButton.setOnAction(event -> {
			String portText = portNumber.getText();
			portNum = Integer.parseInt(portText);
			portTop.setText("Port Number: " + String.valueOf(portNum));
			serverConnection = new Server(data -> {
				Platform.runLater(() -> {
					listItems.getItems().add(data.toString());
				});
			}, portNum);

			primaryStage.setScene(sceneMap.get("ServerPage")); // Change scene to server view
		});



		return new Scene(pane, 500, 400);
	}
	
	public Scene createServerGui() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: coral");
		HBox port = new HBox();
		System.out.println(portNum);
		portTop = new Text("PortNumber: " + String.valueOf(portNum));
		port.setAlignment(Pos.TOP_CENTER);
		port.getChildren().setAll(portTop);
		pane.setTop(port);
		pane.setCenter(this.listItems);
	
		return new Scene(pane, 500, 400);

	}
	
	public Scene createClientGui() {
		
		clientBox = new VBox(10, c1,b1,listItems2);
		clientBox.setStyle("-fx-background-color: blue");
		return new Scene(clientBox, 400, 300);
		
	}

}
