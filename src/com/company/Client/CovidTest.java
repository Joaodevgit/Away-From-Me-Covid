package com.company.Client;

import com.company.CentralNode;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class CovidTest extends Application {

    private Label sceneLabel;
    private Label descLabel;
    private Label testResultLabel;
    private Button covidTestButton;
    private Button returnMenuButton;
    private Scene covidTestScene;

    private Stage covidTestWindow;
    private BufferedReader in;
    private PrintWriter out;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.covidTestWindow = primaryStage;

        // Título da Scene
        this.sceneLabel = new Label();
        this.sceneLabel.setText("TESTE COVID-19");
        this.sceneLabel.setFont(new Font(30));

        // Descrição
        this.descLabel = new Label();
        this.descLabel.setText("Clique no botão abaixo para fazer o teste:");
        this.descLabel.setFont(new Font(20));

        // Resultado Teste Covid
        this.testResultLabel = new Label();
        this.testResultLabel.setText("Clique no botão abaixo para fazer o teste:");
        this.testResultLabel.setFont(new Font(15));

        // Botão "Fazer Teste"
        this.covidTestButton = new Button("Fazer Teste");
        Socket socket = new Socket("Asus", 2048);
        this.covidTestButton.setOnAction(e -> {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("BOTÃO COVID");
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String serverMsg;
                if ((serverMsg = in.readLine()) != null) {
                    AlertUserBox.display("Resultado Teste Covid", serverMsg);
                    if (serverMsg.contains("positivo")) {
                        this.covidTestButton.setDisable(true);
                    }
                }
            } catch (UnknownHostException ex) {
                System.out.println("Unknown Host.");
                System.exit(1);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        // Botão "Regressar ao Menu Principal
        this.returnMenuButton = new Button("Regressar ao menu principal");

        this.returnMenuButton.setOnAction(e -> {
            MenuPage menuPage = new MenuPage();
            try {
                menuPage.start(this.covidTestWindow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox titlesContainer = new VBox(20);
        titlesContainer.setAlignment(Pos.CENTER);
        titlesContainer.getChildren().addAll(this.sceneLabel, this.descLabel);

        VBox contentContainer = new VBox(20);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.setPadding(new Insets(0, 40, 40, 40));
        contentContainer.getChildren().addAll(this.covidTestButton, this.testResultLabel, this.returnMenuButton);

        BorderPane borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(contentContainer);
        borderPanelayout.setTop(titlesContainer);

        this.covidTestScene = new Scene(borderPanelayout, LoginMenu.getSceneWidth(), LoginMenu.getSceneHeight());

        this.covidTestWindow.setScene(this.covidTestScene);
        this.covidTestWindow.show();

    }
}
