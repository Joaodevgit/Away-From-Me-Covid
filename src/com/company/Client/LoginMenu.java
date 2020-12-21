package com.company.Client;

import javafx.application.Application;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoginMenu extends Application {

    /* Variáveis Constantes */
    private static final int SCENE_WIDTH = 500;
    private static final int SCENE_HEIGHT = 500;
    private static final int NOTIFICATION_WIDTH = 300;
    private static final int NOTIFICATION_HEIGHT = 100;

    /* Stage */
    private static Stage mainMenuWindow;

    /* Scenes */
    private static Scene mainMenuScene;
    private Scene registerMenuScene;

    /* Todas as outras scenes */
    //public static Scene menuPage = new MenuPage().sceneView();
    //public static Scene covidTestPage = new CovidTest().sceneView();


    /**
     * Botões Menu Principal
     **/
    Button registerAccount;
    Button loginAccount;

    /**
     * Labels Menu Principal
     **/
    Label titleLabel;
    Label textUsername;
    Label textPassword;

    /**
     * Layout Menu Principal
     **/
    BorderPane borderPanelayout;

    TextField inputUsername;
    TextField inputPassword;

    public static Stage getStage() {
        return mainMenuWindow;
    }

    public static int getSceneWidth() {
        return SCENE_WIDTH;
    }

    public static int getSceneHeight() {
        return SCENE_HEIGHT;
    }

    public static Scene getScene() {
        return mainMenuScene;
    }

    public static void setScene(Scene scn) {
        mainMenuScene = scn;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        mainMenuWindow = primaryStage;

        /**
         * Scene MenuPage
         **/
        //this.menuPage = new Scene(new MenuPage().sceneView(), SCENE_WIDTH, SCENE_HEIGHT);
        //this.registerMenuScene = new Scene(new RegisterPage().sceneView(), SCENE_WIDTH, SCENE_HEIGHT);

        /* Botões */
        this.registerAccount = new Button("Criar uma conta");
        this.registerAccount.setOnAction(e -> {
            RegisterPage registerPage = new RegisterPage();
            try {
                registerPage.start(mainMenuWindow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        this.loginAccount = new Button("LogIn");
        this.loginAccount.setOnAction(e -> {
            File file = new File("src/com/company/Data/Users.json");

            if (file.exists()) {
                // Caso que o ficheiro existe
                try {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

                    JSONArray listUsers = (JSONArray) obj.get("Registo");

                    boolean found = false;
                    JSONObject user = null;

                    String contentUsername = this.inputUsername.getText();
                    String contentPassword = this.inputPassword.getText();

                    for (int i = 0; !found && i < listUsers.size(); i++) {
                        user = (JSONObject) listUsers.get(i);

                        if (contentUsername.equals(user.get("name")) && contentPassword.equals(user.get("password")))
                            found = true;
                    }

                    if (found) {
                        this.inputUsername.setText("");
                        this.textPassword.setText("");

                        MenuPage menuPage = new MenuPage();

                        try {
                            menuPage.start(mainMenuWindow);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.out.println();
                        AlertUserBox.display("LogIn", "Utilizador não existente");
                    }

                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                // Caso que o ficheiro não existe
                System.out.println("O Ficheiro não existe");
            }
        });

        /* Labels Menu Principal */

        // Título do menu principal
        titleLabel = new Label();
        titleLabel.setText("LogIn");
        titleLabel.setFont(new Font(30));

        this.textUsername = new Label();
        this.textUsername.setText("Username");
        this.textUsername.setFont(new Font(15));

        this.textPassword = new Label();
        this.textPassword.setText("Password");
        this.textPassword.setFont(new Font(15));

        /* InputForm do Login */
        this.inputUsername = new TextField();
        this.inputPassword = new TextField();

        /* Containers Menu Principal */

        // Container do preenchimento do login
        HBox containerLoginUsername = new HBox(10);
        containerLoginUsername.setAlignment(Pos.CENTER);
        containerLoginUsername.getChildren().addAll(this.textUsername, this.inputUsername);

        HBox containerLoginPass = new HBox(10);
        containerLoginPass.setAlignment(Pos.CENTER);
        containerLoginPass.getChildren().addAll(this.textPassword, this.inputPassword);

        // Container do Título
        VBox titleBoxContainer = new VBox(10);
        titleBoxContainer.setAlignment(Pos.CENTER);
        titleBoxContainer.getChildren().addAll(titleLabel);

        // Container dos botões centrais
        VBox mainMenuButtons = new VBox(50);
        mainMenuButtons.setAlignment(Pos.CENTER);
        mainMenuButtons.getChildren().addAll(containerLoginUsername, containerLoginPass, this.loginAccount, this.registerAccount, titleBoxContainer);

        // Border Pane Layout
        borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(mainMenuButtons);
        borderPanelayout.setTop(titleBoxContainer);

        // Scene Menu Principal
        mainMenuScene = new Scene(borderPanelayout, SCENE_WIDTH, SCENE_HEIGHT);
        mainMenuWindow.setScene(mainMenuScene);
        mainMenuWindow.setTitle("Away From Me Covid");
        mainMenuWindow.show();
    }
}