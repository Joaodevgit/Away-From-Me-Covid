package com.company.Client;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertUserBox {

    public static void display(String title, String message) {
        Stage window = new Stage();

        // Bloqueio de interações com outras janelas
        // até esta janela ter sido fechada
        //window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(400);
        window.setHeight(150);

        Label label = new Label();
        label.setText(message);

        Button closeButton = new Button("Fechar");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);


        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
        Thread newThread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> window.close());
        });
        newThread.start();
    }
}
