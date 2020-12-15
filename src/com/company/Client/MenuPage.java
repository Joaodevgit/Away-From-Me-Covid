package com.company.Client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MenuPage {
    /**
     * Botões Menu Principal
     **/

    private Button addContactSceneButton;
    private Button covidTestSceneButton;
    private Button logoutButton;
    private Scene addContactScene;
    private Scene covidTestePage = MainMenu.covidTestPage;

    /**
     * Labels Menu Principal
     **/

    private Label titleLabel;
    private Label welcomeMsgLabel;

    private Scene mainScene;

    public Scene sceneView() {
        this.addContactScene = new Scene(new AddCloseContact().sceneView(), MainMenu.getSceneWidth(), MainMenu.getSceneHeight());

        //Stage newStage = MainMenu.getStage();
        /* Labels Menu Principal */

        // Título do menu principal
        titleLabel = new Label();
        titleLabel.setText("MENU");
        titleLabel.setFont(new Font(30));

        // Mensagem de boas vindas
        welcomeMsgLabel = new Label();
        welcomeMsgLabel.setText("Bem vindo ao Away From Me Covid!");
        welcomeMsgLabel.setFont(new Font(20));

        /* Botões */

        // Botão de acesso à scene de adicionar contatos próximos
        addContactSceneButton = new Button("Adicionar contato(s) próximo(s)");

        addContactSceneButton.setOnAction(event -> {
            MainMenu.getStage().hide();
            MainMenu.setScene(addContactScene);
            MainMenu.getStage().show();
        });

        // Botão de acesso à scene do teste de covid-19
        covidTestSceneButton = new Button("Teste Covid-19");

        covidTestSceneButton.setOnAction(event -> {
            MainMenu.getStage().hide();
            MainMenu.getStage().setScene(this.covidTestePage);
            MainMenu.getStage().show();
        });

        // Botão Logout
        logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> MainMenu.getStage().setScene(MainMenu.getScene()));

        /* Containers Menu Principal */

        // Container do Título
        VBox titleBoxContainer = new VBox(10);
        titleBoxContainer.setAlignment(Pos.CENTER);
        titleBoxContainer.getChildren().addAll(titleLabel, welcomeMsgLabel);

        // Container dos botões centrais
        VBox mainMenuButtons = new VBox(50);
        mainMenuButtons.setAlignment(Pos.CENTER);
        mainMenuButtons.getChildren().addAll(addContactSceneButton, covidTestSceneButton, titleBoxContainer);

        // Container do botão logout
        HBox bottonMenuButtons = new HBox(20);
        bottonMenuButtons.setAlignment(Pos.BOTTOM_RIGHT);
        bottonMenuButtons.setPadding(new Insets(30, 30, 30, 30));
        bottonMenuButtons.getChildren().add(logoutButton);


        // Border Pane Layout/**
        BorderPane borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(mainMenuButtons);
        borderPanelayout.setBottom(bottonMenuButtons);
        borderPanelayout.setTop(titleBoxContainer);

        this.mainScene = new Scene(borderPanelayout, MainMenu.getSceneWidth(), MainMenu.getSceneHeight());

        return this.mainScene;
    }

}
