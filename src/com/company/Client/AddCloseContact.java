package com.company.Client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AddCloseContact {

    private Label sceneLabel;
    private Label titleLabel;
    private Label descLabel;
    private Label idContact;
    private TextField idContactInput;
    private Button addContactButton;
    private Button returnMenuButton;

    public Label getDescLabel() {
        return descLabel;
    }

    public void setDescLabel(Label descLabel) {
        this.descLabel = descLabel;
    }

    public Label getIdContact() {
        return idContact;
    }

    public void setIdContact(Label idContact) {
        this.idContact = idContact;
    }

    public TextField getIdContactInput() {
        return idContactInput;
    }

    public void setIdContactInput(TextField idContactInput) {
        this.idContactInput = idContactInput;
    }

    public Button getAddContactButton() {
        return addContactButton;
    }

    public void setAddContactButton(Button addContactButton) {
        this.addContactButton = addContactButton;
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
        Stage newStage = MainMenu.getStage();
        this.returnMenuButton.setOnAction(e -> newStage.setScene(MainMenu.getScene()));


        VBox titlesContainer = new VBox(10);
        titlesContainer.setAlignment(Pos.CENTER);
        titlesContainer.getChildren().addAll(this.sceneLabel, this.titleLabel, this.descLabel);

        HBox inputContainer = new HBox(10);
        inputContainer.setAlignment(Pos.CENTER);
        inputContainer.setPadding(new Insets(0,40,40,40));
        inputContainer.getChildren().addAll(this.idContact, this.idContactInput);

        VBox contentContainer = new VBox(10);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.getChildren().addAll(inputContainer, this.addContactButton, this.returnMenuButton);

        BorderPane borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(contentContainer);
        borderPanelayout.setTop(titlesContainer);

        return borderPanelayout;
    }
}
