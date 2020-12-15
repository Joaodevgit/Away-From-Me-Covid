package com.company.Client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RegisterPage extends Application {

    private Label titleContent;
    private Label textUsername;
    private Label textPassword;
    private TextField inputUsername;
    private TextField inputPass;
    private ComboBox<String> comboBox;
    private Button register;
    private Button backMenu;

    private Scene registerPageScene;

    private Stage registerPageWindow;

    @Override
    public void start(Stage primaryStage) throws Exception {

        registerPageWindow = primaryStage;

        //Titulo da Scene
        this.titleContent = new Label();
        this.titleContent.setText("Registar Conta");
        this.titleContent.setFont(new Font(30));

        //Formulario do registo
        this.textUsername = new Label();
        this.textUsername.setText("Username");
        this.textUsername.setFont(new Font(15));

        this.textPassword = new Label();
        this.textPassword.setText("Password");
        this.textPassword.setFont(new Font(15));

        this.inputUsername = new TextField();
        this.inputPass = new TextField();

        //Botões
        this.register = new Button("Criar Conta");
        Stage mainMenuPage = MainMenu.getStage();
        this.register.setOnAction(e -> {
            mainMenuPage.setScene(MainMenu.getScene());
        });

        this.backMenu = new Button("Voltar");
        this.backMenu.setOnAction(e -> mainMenuPage.setScene(MainMenu.getScene()));

        //Container do titulo
        VBox titleContainer = new VBox(10);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.getChildren().addAll(this.titleContent);

        // Container do preenchimento do login
        HBox containerLoginUsername = new HBox(10);
        containerLoginUsername.setAlignment(Pos.CENTER);
        containerLoginUsername.getChildren().addAll(this.textUsername, this.inputUsername);

        HBox containerLoginPass = new HBox(10);
        containerLoginPass.setAlignment(Pos.CENTER);
        containerLoginPass.getChildren().addAll(this.textPassword, this.inputPass);

        this.comboBox = new ComboBox<>();
        this.comboBox.setValue("Escolha o seu concelho");
        this.comboBox.getItems().addAll(
                "Good Will Hunting",
                "St. Vincent",
                "Blackhat"
        );

        //this.comboBox.setPromptText("Escolha o seu concelho");

        // Click on a item from combobox
        this.comboBox.setOnAction(e -> System.out.println("User selected: " + comboBox.getValue()));

        VBox mainMenuButtons = new VBox(10);
        mainMenuButtons.setAlignment(Pos.CENTER);
        mainMenuButtons.getChildren().addAll(containerLoginUsername, containerLoginPass, comboBox);

        // Container dos butões
        HBox containerButton = new HBox(10);
        containerButton.setAlignment(Pos.CENTER);
        containerButton.setPadding(new Insets(0, 40, 40, 40));
        containerButton.getChildren().addAll(this.backMenu, this.register);

        BorderPane borderPanelayout = new BorderPane();
        borderPanelayout.setTop(titleContainer);
        borderPanelayout.setCenter(mainMenuButtons);
        borderPanelayout.setBottom(containerButton);



        this.registerPageScene = new Scene(borderPanelayout, MainMenu.getSceneWidth(), MainMenu.getSceneHeight());

        registerPageWindow.setScene(registerPageScene);
        registerPageWindow.show();
    }
}
