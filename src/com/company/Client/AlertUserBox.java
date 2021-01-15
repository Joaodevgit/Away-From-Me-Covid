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

public class AlertUserBox {

    /**
     * Método responsável por inicar a interface grafica da notificação de todos os eventos do Servidor
     *
     * @param title   Titulo da notificação
     * @param message Conteudo da mensagem na Notificação
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
