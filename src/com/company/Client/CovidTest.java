package com.company.Client;

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
import sun.applet.Main;

public class CovidTest extends Application {

    private Label sceneLabel;
    private Label descLabel;
    private Label testResultLabel;
    private Button covidTestButton;
    private Button returnMenuButton;
    //private Scene menuPage = MainMenu.menuPage;
    private Scene covidTestScene;

    private Stage covidTestWindow;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.covidTestWindow = primaryStage;
        //this.menuPage = new Scene(new MenuPage().sceneView(), MainMenu.getSceneWidth(), MainMenu.getSceneHeight());

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
        covidTestButton = new Button("Fazer Teste");

        // Botão "Regressar ao Menu Principal
        this.returnMenuButton = new Button("Regressar ao menu principal");

        this.returnMenuButton.setOnAction(e -> {
            MenuPage menuPage = new MenuPage();
            try {
                menuPage.start(this.covidTestWindow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            MainMenu.getStage().hide();
//            //MainMenu.getStage().setScene(menuPage);
//            MainMenu.getStage().show();
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

        this.covidTestScene = new Scene(borderPanelayout, MainMenu.getSceneWidth(), MainMenu.getSceneHeight());

        this.covidTestWindow.setScene(this.covidTestScene);
        this.covidTestWindow.show();

    }
}
