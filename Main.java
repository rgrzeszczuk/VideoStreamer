package LavaGCA;

import org.opencv.core.Core;

import LavaGCA.FXController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @SuppressWarnings("unchecked")
	@Override
    public void start(Stage primaryStage) {
            try {
                FXController controller = new FXController();

                BorderPane root = new BorderPane();
                root.setPadding(new Insets(10, 10, 10, 10));
                root.setPrefWidth(600);
                root.setPrefHeight(400);
                
                ImageView imageView = new ImageView();
                imageView.setFitWidth(400);
                imageView.setFitHeight(300);
                BorderPane.setAlignment(imageView, Pos.CENTER);
                root.setCenter(imageView);
                
                Button button = new Button("Start Capture");
                BorderPane.setAlignment(button, Pos.CENTER);
                root.setBottom(button);
                
                button.setOnAction(new EventHandler() {
					@Override
					public void handle(Event event) {
						controller.startCamera(event);
					}
                });
                
                // set a reference of this class for its controller
                controller.setRootElement(root);
                
            	// create and style a scene
                Scene scene = new Scene(root);
                // scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                // create the stage with the given title and the previously created scene
                primaryStage.setTitle("JavaFX meets OpenCV");
                primaryStage.setScene(scene);
                // show the GUI
                primaryStage.show();

                primaryStage.setOnCloseRequest(e -> Platform.exit());
                
            } catch(Exception e) {
                    e.printStackTrace();
            }
    }

    public static void main(String[] args) {
            // load the native OpenCV library
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            launch(args);
    }

}