package com.company.Client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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

    private Scene mainScene;

    private Stage menuPageWindow;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.menuPageWindow = primaryStage;

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

        /* Botões */

        // Botão de acesso à scene de adicionar contatos próximos
        this.addContactSceneButton = new Button("Adicionar contato(s) próximo(s)");

        this.addContactSceneButton.setOnAction(event -> {
            AddCloseContact addCloseContact = new AddCloseContact();
            try {
                addCloseContact.start(this.menuPageWindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //MainMenu.setScene(addContactScene);
        });

        // Botão de acesso à scene do teste de covid-19
        this.covidTestSceneButton = new Button("Teste Covid-19");

        this.covidTestSceneButton.setOnAction(event -> {
            CovidTest covidTest = new CovidTest();
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
            LoginMenu loginMenu = new LoginMenu();
            loginMenu.start(this.menuPageWindow);
            //MainMenu.getStage().setScene(MainMenu.getScene());
        });

        /* Containers Menu Principal */

        // Container do Título
        VBox titleBoxContainer = new VBox(10);
        titleBoxContainer.setAlignment(Pos.CENTER);
        titleBoxContainer.getChildren().addAll(this.titleLabel, this.welcomeMsgLabel);

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
