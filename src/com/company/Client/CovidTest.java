package com.company.Client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CovidTest {

    private Label sceneLabel;
    private Label descLabel;
    private Label testResultLabel;
    private Button covidTestButton;
    private Button returnMenuButton;


    public Label getTestResultLabel() {
        return testResultLabel;
    }

    public void setTestResultLabel(Label testResultLabel) {
        this.testResultLabel = testResultLabel;
    }

    public Button getCovidTestButton() {
        return covidTestButton;
    }

    public void setCovidTestButton(Button covidTestButton) {
        this.covidTestButton = covidTestButton;
    }

    public Button getReturnMenuButton() {
        return returnMenuButton;
    }

    public void setReturnMenuButton(Button returnMenuButton) {
        this.returnMenuButton = returnMenuButton;
    }

    public BorderPane sceneView() {

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
        Stage newStage = MainMenu.getStage();
        this.returnMenuButton.setOnAction(e -> newStage.setScene(MainMenu.getScene()));

        VBox titlesContainer = new VBox(20);
        titlesContainer.setAlignment(Pos.CENTER);
        titlesContainer.getChildren().addAll(this.sceneLabel, this.descLabel);

        VBox contentContainer = new VBox(20);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.setPadding(new Insets(0,40,40,40));
        contentContainer.getChildren().addAll(this.covidTestButton, this.testResultLabel, this.returnMenuButton);

        BorderPane borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(contentContainer);
        borderPanelayout.setTop(titlesContainer);

        return borderPanelayout;
    }
}
