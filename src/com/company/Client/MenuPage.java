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
import java.net.Socket;

public class MenuPage extends Application {
    /**
     * Botões Menu Principal
     **/

    private Button addContactSceneButton;
    private Button covidTestSceneButton;
    private Button logoutButton;
    private Scene addContactScene;
    //private Scene covidTestePage = MainMenu.covidTestPage;

    /**
     * Labels Menu Principal
     **/

    private Label titleLabel;
    private Label welcomeMsgLabel;

    /**
     * Imagem Menu Principal
     */
    private ImageView imageView;

    private Scene mainScene;

    private Stage menuPageWindow;
    private Socket socket;
    private PrintWriter out;
    private Client client;

    public MenuPage(Socket socket, Client name) throws IOException {
        this.socket = socket;
        this.client = name;
        System.out.println(this.client);
    }

    @Override
    public void start(Stage primaryStage) {
        this.menuPageWindow = primaryStage;

        // Method called when the user presses "X" on window
        this.menuPageWindow.setOnCloseRequest(e -> {
            e.consume();

            try {
                out = new PrintWriter(socket.getOutputStream(), true);

                this.client.setCommand("BOTÃO LOGOUT");
                out.println(this.client.toString());

                menuPageWindow.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });

        //this.addContactScene = new Scene(new AddCloseContact().sceneView(), MainMenu.getSceneWidth(), MainMenu.getSceneHeight());

        //Stage newStage = MainMenu.getStage();
        /* Labels Menu Principal */

        // Título do menu principal
        this.titleLabel = new Label();
        this.titleLabel.setText("MENU");
        this.titleLabel.setFont(new Font(30));

        // Mensagem de boas vindas
        this.welcomeMsgLabel = new Label();
        this.welcomeMsgLabel.setText("Bem vindo ao Away From Me Covid!");
        this.welcomeMsgLabel.setFont(new Font(20));

        // Imagem do menu principal
        //Creating an image
        //Setting the image view
        this.imageView = new ImageView("https://user-images.githubusercontent.com/44362304/103881998-1bd1fc00-50d3-11eb-82d4-e5f842f3d61f.png");

        //Setting the position of the image
        imageView.setX(50);
        imageView.setY(25);

        //setting the fit height and width of the image view
        imageView.setFitHeight(455);
        imageView.setFitWidth(500);

        //Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);


        /* Botões */

        // Botão de acesso à scene de adicionar contatos próximos
        this.addContactSceneButton = new Button("Adicionar contato(s) próximo(s)");

        if (!this.client.isInfected()) {
            this.addContactSceneButton.setDisable(true);
        } else {
            this.addContactSceneButton.setOnAction(event -> {
                AddCloseContact addCloseContact = new AddCloseContact(this.socket, this.client);
                try {
                    addCloseContact.start(this.menuPageWindow);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //MainMenu.setScene(addContactScene);
            });
        }

        // Botão de acesso à scene do teste de covid-19
        this.covidTestSceneButton = new Button("Teste Covid-19");

        this.covidTestSceneButton.setOnAction(event -> {
            CovidTest covidTest = new CovidTest(this.socket, this.client);

            try {
                covidTest.start(this.menuPageWindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //MainMenu.getStage().setScene(this.covidTestePage);
        });

        // Botão Logout
        this.logoutButton = new Button("Logout");
        this.logoutButton.setOnAction(e -> {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                this.client.setCommand("BOTÃO LOGOUT");
                out.println(this.client.toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            LoginMenu loginMenu = new LoginMenu();
            loginMenu.start(this.menuPageWindow);

            //MainMenu.getStage().setScene(MainMenu.getScene());
        });


        /* Containers Menu Principal */

        // Container do Título
        VBox titleBoxContainer = new VBox(10);
        titleBoxContainer.setAlignment(Pos.CENTER);
        titleBoxContainer.getChildren().addAll(this.titleLabel, this.welcomeMsgLabel, this.imageView);

        // Container dos botões centrais
        VBox mainMenuButtons = new VBox(50);
        mainMenuButtons.setAlignment(Pos.CENTER);
        mainMenuButtons.getChildren().addAll(this.addContactSceneButton, this.covidTestSceneButton, titleBoxContainer);

        // Container do botão logout
        HBox bottonMenuButtons = new HBox(20);
        bottonMenuButtons.setAlignment(Pos.BOTTOM_RIGHT);
        bottonMenuButtons.setPadding(new Insets(30, 30, 30, 30));
        bottonMenuButtons.getChildren().add(this.logoutButton);


        // Border Pane Layout/**
        BorderPane borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(mainMenuButtons);
        borderPanelayout.setBottom(bottonMenuButtons);
        borderPanelayout.setTop(titleBoxContainer);

        this.mainScene = new Scene(borderPanelayout, LoginMenu.getSceneWidth(), LoginMenu.getSceneHeight());

        this.menuPageWindow.setScene(this.mainScene);
        this.menuPageWindow.show();
    }
}
