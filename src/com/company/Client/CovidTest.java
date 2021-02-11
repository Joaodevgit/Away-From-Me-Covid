package com.company.Client;

import com.company.Models.Client;
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

/**
 * Class responsible for managing the actions in "Covid-19 Test" stage
 *
 * @author João Pereira
 * @author Paulo da Cunha
 */
public class CovidTest extends Application {

    private Label sceneLabel;
    private Label descLabel;
    protected static Button covidTestButton;
    private Button returnMenuButton;
    private Scene covidTestScene;

    private Stage covidTestWindow;
    private PrintWriter out;
    private Socket socket;
    protected static Client client;

    public CovidTest(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    /**
     * Method responsible for starting the user interface "Covid Test"
     *
     * @param primaryStage - Main Container of JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        this.covidTestWindow = primaryStage;

        // Method responsible for saving user information, when the user interface window is closed
        this.covidTestWindow.setOnCloseRequest(e -> {
            e.consume();

            try {
                out = new PrintWriter(socket.getOutputStream(), true);

                this.client.setCommand("LOGOUT");
                out.println(this.client.toString());

                covidTestWindow.close();
                System.exit(-1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });

        // Scene Title
        this.sceneLabel = new Label();
        this.sceneLabel.setText("COVID-19 TEST");
        this.sceneLabel.setFont(new Font(30));

        // Description
        this.descLabel = new Label();
        this.descLabel.setText("Click the button below to do the test:");
        this.descLabel.setFont(new Font(20));

        // Button "Take Test"
        this.covidTestButton = new Button("Take Test");

        if (!this.client.isInfected()) {
            this.covidTestButton.setOnAction(e -> {
                try {
                    out = new PrintWriter(socket.getOutputStream(), true);
                    this.client.setCommand("COVID TEST BUTTON");
                    out.println(this.client.toString());
                } catch (UnknownHostException ex) {
                    System.out.println("Unknown Host.");
                    System.exit(1);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            });
        } else {
            this.covidTestButton.setDisable(true);
        }

        // Button "Return to Main Menu"
        this.returnMenuButton = new Button("Return to Main Menu");

        this.returnMenuButton.setOnAction(e -> {
            MenuPage menuPage = null;
            try {
                menuPage = new MenuPage(socket, client);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
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
        contentContainer.getChildren().addAll(this.covidTestButton, this.returnMenuButton);

        BorderPane borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(contentContainer);
        borderPanelayout.setTop(titlesContainer);

        this.covidTestScene = new Scene(borderPanelayout, LoginMenu.getSceneWidth(), LoginMenu.getSceneHeight());

        this.covidTestWindow.setScene(this.covidTestScene);
        this.covidTestWindow.show();

    }
}
