package com.company.Client;

import com.company.Models.Client;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.net.MulticastSocket;
import java.net.Socket;

/**
 * Class responsible for managing the actions in "Main Menu" stage
 *
 * @author João Pereira
 * @author Paulo da Cunha
 */
public class MenuPage extends Application {

    /* Main Menu Buttons */
    private Button addContactSceneButton;
    private Button covidTestSceneButton;
    private Button logoutButton;

    /* Main Menu Labels */
    private Label titleLabel;
    private Label welcomeMsgLabel;

    /* Main Menu Image */
    private ImageView imageView;

    /* Main Menu Scene */
    private Scene mainScene;

    /* Main Menu Stage */
    private Stage menuPageWindow;

    private Socket socket;
    private PrintWriter out;
    private Client client;


    public MenuPage(Socket socket, Client name) throws IOException {
        this.socket = socket;
        this.client = name;
        System.out.println(this.client);
    }

    /**
     * Method responsible for starting the user interface "Main Menu"
     *
     * @param primaryStage - Main Container of JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        this.menuPageWindow = primaryStage;

        // Method responsible for saving user information, when the user interface window is closed
        this.menuPageWindow.setOnCloseRequest(e -> {
            e.consume();

            try {
                out = new PrintWriter(socket.getOutputStream(), true);

                this.client.setCommand("LOGOUT BUTTON");
                out.println(this.client.toString());

                menuPageWindow.close();
                System.exit(-1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });

        /* Main Menu Labels */

        // Main Menu Títle
        this.titleLabel = new Label();
        this.titleLabel.setText("MENU");
        this.titleLabel.setFont(new Font(30));

        // Welcome message
        this.welcomeMsgLabel = new Label();
        this.welcomeMsgLabel.setText("Welcome to Away From Me Covid!");
        this.welcomeMsgLabel.setFont(new Font(20));

        // Main Menu Image
        // Creates the image view
        // Sets the image view
        this.imageView = new ImageView("https://user-images.githubusercontent.com/44362304/103881998-1bd1fc00-50d3-11eb-82d4-e5f842f3d61f.png");

        // Sets the image position
        imageView.setX(50);
        imageView.setY(25);

        // Sets the height an width of the image
        imageView.setFitHeight(455);
        imageView.setFitWidth(500);

        // Sets the image proportion
        imageView.setPreserveRatio(true);


        /* Buttons */

        // Access button to the scene of adding nearby contacts
        this.addContactSceneButton = new Button("Add nearby contacts");

//        if (!this.client.isInfected()) {
//            this.addContactSceneButton.setDisable(true);
//        } else {
        this.addContactSceneButton.setOnAction(event -> {
            AddCloseContact addCloseContact = new AddCloseContact(this.socket, this.client);
            try {
                addCloseContact.start(this.menuPageWindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
//        }

        // Access button to the scene of covid-19 test
        this.covidTestSceneButton = new Button("Covid-19 Test");

        this.covidTestSceneButton.setOnAction(event -> {
            CovidTest covidTest = new CovidTest(this.socket, this.client);

            try {
                covidTest.start(this.menuPageWindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Logout button
        this.logoutButton = new Button("Logout");
        this.logoutButton.setOnAction(e -> {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                this.client.setCommand("LOGOUT BUTTON");
                out.println(this.client.toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            LoginMenu loginMenu = new LoginMenu();
            loginMenu.start(this.menuPageWindow);
        });


        /* Main Menu Containers */

        // Title Container
        VBox titleBoxContainer = new VBox(10);
        titleBoxContainer.setAlignment(Pos.CENTER);
        titleBoxContainer.getChildren().addAll(this.titleLabel, this.welcomeMsgLabel, this.imageView);

        // Central Buttons Container
        VBox mainMenuButtons = new VBox(50);
        mainMenuButtons.setAlignment(Pos.CENTER);
        mainMenuButtons.getChildren().addAll(this.addContactSceneButton, this.covidTestSceneButton, titleBoxContainer);

        // Logout Button Container
        HBox bottonMenuButtons = new HBox(20);
        bottonMenuButtons.setAlignment(Pos.BOTTOM_RIGHT);
        bottonMenuButtons.setPadding(new Insets(30, 30, 30, 30));
        bottonMenuButtons.getChildren().add(this.logoutButton);

        // Border Pane Layout
        BorderPane borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(mainMenuButtons);
        borderPanelayout.setBottom(bottonMenuButtons);
        borderPanelayout.setTop(titleBoxContainer);

        this.mainScene = new Scene(borderPanelayout, LoginMenu.getSceneWidth(), LoginMenu.getSceneHeight());

        this.menuPageWindow.setScene(this.mainScene);
        this.menuPageWindow.show();
    }
}
