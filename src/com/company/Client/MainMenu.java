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

public class MainMenu extends Application {

    /* Variáveis Constantes */
    final int SCENE_WIDTH = 500;
    final int SCENE_HEIGHT = 500;
    final int NOTIFICATION_WIDTH = 300;
    final int NOTIFICATION_HEIGHT = 100;

    public static void main(String[] args) {
        launch(args);
    }

    /* Stage */
    private static Stage window;

    public static Stage getStage() {
        return window;
    }

    /* Scenes */
    Scene covidTestScene, addContactScene;
    private static Scene mainMenuScene;

    public static Scene getScene() {
        return mainMenuScene;
    }

    /**
     * Botões Menu Principal
     **/

    Button addContactSceneButton;
    Button covidTestSceneButton;
    Button logoutButton;

    /**
     * Labels Menu Principal
     **/

    Label titleLabel;
    Label welcomeMsgLabel;

    /**
     *  Layout Menu Principal
     **/
    BorderPane borderPanelayout;


    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        /* Botões */

        // Botão de acesso à scene de adicionar contatos próximos
        addContactSceneButton = new Button("Adicionar contato(s) próximo(s)");

        addContactSceneButton.setOnAction(event -> {
            window.setScene(addContactScene);
        });

        // Botão de acesso à scene do teste de covid-19
        covidTestSceneButton = new Button("Teste Covid-19");

        covidTestSceneButton.setOnAction(event -> {
            window.setScene(covidTestScene);
        });

        // Botão Logout
        logoutButton = new Button("Logout");

        logoutButton.setOnAction(event -> {
            System.out.println("Scene do Login");
        });


        /* Labels Menu Principal */

        // Título do menu principal
        titleLabel = new Label();
        titleLabel.setText("MENU");
        titleLabel.setFont(new Font(30));

        // Mensagem de boas vindas
        welcomeMsgLabel = new Label();
        welcomeMsgLabel.setText("Bem vindo ao Away From Me Covid!");
        welcomeMsgLabel.setFont(new Font(20));


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


        // Border Pane Layout
        borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(mainMenuButtons);
        borderPanelayout.setBottom(bottonMenuButtons);
        borderPanelayout.setTop(titleBoxContainer);


        // Scene Menu Principal
        mainMenuScene = new Scene(borderPanelayout, SCENE_WIDTH, SCENE_HEIGHT);
        window.setScene(mainMenuScene);
        window.setTitle("Away From Me Covid");
        window.show();

        /**
         * Scene AddCloseContacts
         **/

        addContactScene = new Scene(new AddCloseContact().sceneView(), SCENE_WIDTH, SCENE_HEIGHT);

        /**
         * Scene CovidTest
         **/

        covidTestScene = new Scene(new CovidTest().sceneView(), SCENE_WIDTH, SCENE_HEIGHT);
    }
}













