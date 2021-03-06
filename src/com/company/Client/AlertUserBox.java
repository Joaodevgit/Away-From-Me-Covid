package com.company.Client;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class responsible for building a notification box
 *
 * @author João Pereira
 * @author Paulo da Cunha
 */
public class AlertUserBox {

    /**
     * Method responsible for starting the notification of all server events
     * @param title   notification title
     * @param message notification content message
     */
    public static void display(String title, String message) {
        Platform.runLater(() -> {
            Stage window = new Stage();

            window.getIcons().add(new Image("https://user-images.githubusercontent.com/44362304/103882959-761f8c80-50d4-11eb-9e3d-6f4f3c0e276c.png%22"));

            window.setTitle(title);
            window.setWidth(500);
            window.setHeight(150);

            Label label = new Label();
            label.setText(message);

            Button closeButton = new Button("Close");
            closeButton.setOnAction(e -> window.close());

            VBox layout = new VBox(10);
            layout.getChildren().addAll(label, closeButton);
            layout.setAlignment(Pos.CENTER);


            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.show();

            Thread newThread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                Platform.runLater(() -> window.close());
            });

            newThread.start();
        });
    }
}
