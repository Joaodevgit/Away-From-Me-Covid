package com.company.Client;

import com.company.Server.ReadWriteFiles;
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

import java.util.ArrayList;

public class RegisterPage extends Application {

    private Label titleContent;
    private Label textUsername;
    private Label textPassword;
    private Label textId;
    private TextField inputUsername;
    private TextField inputPass;
    private TextField inputId;
    private ComboBox<String> comboBox;
    private Button register;
    private Button backMenu;

    private Scene registerPageScene;

    private Stage registerPageWindow;

    private ArrayList<String> listContyName;

    private ReadWriteFiles readWriteFiles;

    /**
     * Method responsible for starting the user interface "Account Registry "
     *
     * @param primaryStage - Main Container of JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        this.registerPageWindow = primaryStage;

        this.readWriteFiles = new ReadWriteFiles();

        // Method responsible for saving user information, when the user interface window is closed
        this.registerPageWindow.setOnCloseRequest(e -> {
            e.consume();

            AlertUserBox.display("Recommendation", "Follow the recommendations of your government and stay at home!");

            this.registerPageWindow.close();
        });

        // Scene Title
        this.titleContent = new Label();
        this.titleContent.setText("Register Account");
        this.titleContent.setFont(new Font(30));

        // Registry Form
        this.textUsername = new Label();
        this.textUsername.setText("Username");
        this.textUsername.setFont(new Font(15));


        this.textPassword = new Label();
        this.textPassword.setText("Password");
        this.textPassword.setFont(new Font(15));

        this.textId = new Label();
        this.textId.setText("Health User no.");
        this.textId.setFont(new Font(13));

        this.inputUsername = new TextField();
        this.inputPass = new TextField();
        this.inputId = new TextField();

        this.listContyName = this.readWriteFiles.spinnerOptions();

        // Buttons
        this.register = new Button("Create Account");
        Stage mainMenuPage = LoginMenu.getStage();
        this.register.setOnAction(e -> {
            try {
                int id = Integer.parseInt(this.inputId.getText());

                String contentUsername = this.inputUsername.getText();
                String contentPassword = this.inputPass.getText();
                String contentCounty = this.comboBox.getValue();

                boolean conditionRegist = false;

                // ID must be between 100000000 and 999999999
                if ((id >= 100000000 && id <= 999999999) && contentUsername != "" && contentPassword.length() > 5
                        && contentCounty != "Choose your county") {
                    conditionRegist = true;
                }

                if (conditionRegist) {
                    boolean isExist = this.readWriteFiles.userExists(id);

                    if (!isExist) {

                        int checkUserUnregisterIndex = this.readWriteFiles.indexUnregisteredUsers(id);

                        this.inputUsername.setText("");
                        this.inputPass.setText("");

                        // If the account does not exist, verifies if the user is not in the list of unregistered users
                        if (checkUserUnregisterIndex != -1) {
                            this.readWriteFiles.removeUnregisteredUsers(checkUserUnregisterIndex);
                            this.readWriteFiles.writeUserRegister(id, contentUsername, contentPassword, true, contentCounty);
                        } else {
                            this.readWriteFiles.writeUserRegister(id, contentUsername, contentPassword, false, contentCounty);
                        }

                        mainMenuPage.setScene(LoginMenu.getScene());
                    } else {
                        AlertUserBox.display("Registry", "Existing account");
                    }

                } else {
                    AlertUserBox.display("Registry", "Don't respect the conditions to register");
                }

            } catch (NumberFormatException ex) {
                AlertUserBox.display("Registry", "Invalid Health User no.. Please insert only numbers");
            }
        });

        this.backMenu = new Button("Return");
        this.backMenu.setOnAction(e -> mainMenuPage.setScene(LoginMenu.getScene()));

        // Title Container
        VBox titleContainer = new VBox(10);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.getChildren().addAll(this.titleContent);

        HBox containerId = new HBox(10);
        containerId.setAlignment(Pos.CENTER);
        containerId.getChildren().addAll(this.textId, this.inputId);

        // Login Container
        HBox containerLoginUsername = new HBox(10);
        containerLoginUsername.setAlignment(Pos.CENTER);
        containerLoginUsername.getChildren().addAll(this.textUsername, this.inputUsername);

        HBox containerLoginPass = new HBox(10);
        containerLoginPass.setAlignment(Pos.CENTER);
        containerLoginPass.getChildren().addAll(this.textPassword, this.inputPass);

        this.comboBox = new ComboBox<>();
        this.comboBox.setValue("Choose your county");
        this.comboBox.getItems().addAll(this.listContyName);

        // Ações do click na ComboBox
        this.comboBox.setOnAction(e -> System.out.println("User selected: " + this.comboBox.getValue()));

        VBox mainMenuButtons = new VBox(10);
        mainMenuButtons.setAlignment(Pos.CENTER);
        mainMenuButtons.getChildren().addAll(containerId, containerLoginUsername, containerLoginPass, this.comboBox);

        // Buttons Container
        HBox containerButton = new HBox(10);
        containerButton.setAlignment(Pos.CENTER);
        containerButton.setPadding(new Insets(0, 40, 40, 40));
        containerButton.getChildren().addAll(this.backMenu, this.register);

        BorderPane borderPanelayout = new BorderPane();
        borderPanelayout.setTop(titleContainer);
        borderPanelayout.setCenter(mainMenuButtons);
        borderPanelayout.setBottom(containerButton);

        this.registerPageScene = new Scene(borderPanelayout, LoginMenu.getSceneWidth(), LoginMenu.getSceneHeight());

        this.registerPageWindow.setScene(registerPageScene);
        this.registerPageWindow.show();
    }
}
