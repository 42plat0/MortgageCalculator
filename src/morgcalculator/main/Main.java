package morgcalculator.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader landingLoader = new FXMLLoader(getClass().getResource("Landing.fxml"));
			SplitPane root = (SplitPane) landingLoader.load();
			Scene scene = new Scene(root, 1040, 540);

			LandingController landing = (LandingController) landingLoader.getController();

			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
