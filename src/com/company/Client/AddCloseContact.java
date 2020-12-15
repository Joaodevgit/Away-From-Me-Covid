package com.company.Client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AddCloseContact extends Application {

    private Label sceneLabel;
    private Label titleLabel;
    private Label descLabel;
    private Label idContact;
    private TextField idContactInput;
    protected Button addContactButton;
    protected Button returnMenuButton;

    private Scene addCloseContactScene;

    private Stage addCloseContactWindow;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.addCloseContactWindow = primaryStage;

        // Título da Scene
        this.sceneLabel = new Label();
        this.sceneLabel.setText("CONTACTOS PRÓXIMOS");
        this.sceneLabel.setFont(new Font(30));

        // Descrição
        this.titleLabel = new Label();
        this.titleLabel.setText("Se esteve em contato com alguém, adicione aqui o id: ");
        this.titleLabel.setFont(new Font(20));

        // Resumo
        this.descLabel = new Label();
        this.descLabel.setText("Se pretender adicionar vários contatos, separe os id's por virgulas.");
        this.descLabel.setFont(new Font(17));


        // ID Contacto
        this.idContact = new Label();
        this.idContact.setText("ID Contacto: ");
        this.idContact.setFont(new Font(15));


        // Input ID Contacto
        this.idContactInput = new TextField();

        // Botão Adicionar Contacto
        this.addContactButton = new Button("Adicionar");

        // Botão "Regressar ao Menu Principal
        this.returnMenuButton = new Button("Regressar ao menu principal");
        //Stage newStage = MainMenu.getStage();
        this.returnMenuButton.setOnAction(e -> {
            MenuPage menuPage = new MenuPage();
            try {
                menuPage.start(addCloseContactWindow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //newStage.setScene(MainMenu.getScene());
        });


        VBox titlesContainer = new VBox(10);
        titlesContainer.setAlignment(Pos.CENTER);
        titlesContainer.getChildren().addAll(this.sceneLabel, this.titleLabel, this.descLabel);

        HBox inputContainer = new HBox(10);
        inputContainer.setAlignment(Pos.CENTER);
        inputContainer.setPadding(new Insets(0, 40, 40, 40));
        inputContainer.getChildren().addAll(this.idContact, this.idContactInput);

        VBox contentContainer = new VBox(10);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.getChildren().addAll(inputContainer, this.addContactButton, this.returnMenuButton);

        BorderPane borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(contentContainer);
        borderPanelayout.setTop(titlesContainer);

        this.addCloseContactScene = new Scene(borderPanelayout, MainMenu.getSceneWidth(), MainMenu.getSceneHeight());

        this.addCloseContactWindow.setScene(this.addCloseContactScene);
        this.addCloseContactWindow.show();
    }
}
