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
     * Método responsável por inicar a interface grafica de "Registar Conta"
     *
     * @param primaryStage - Container principal do JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        this.registerPageWindow = primaryStage;

        this.readWriteFiles = new ReadWriteFiles();

        // Método responsável por quando o evento de fechar a janela da interface grafica guarda a informação do
        // Utilizador
        this.registerPageWindow.setOnCloseRequest(e -> {
            e.consume();

            AlertUserBox.display("Recomendação", "Siga as recomendações da DGS e fique em casa !");

            this.registerPageWindow.close();
        });

        // Titulo da Scene
        this.titleContent = new Label();
        this.titleContent.setText("Registar Conta");
        this.titleContent.setFont(new Font(30));

        // Formulario do registo
        this.textUsername = new Label();
        this.textUsername.setText("Username");
        this.textUsername.setFont(new Font(15));

        this.textPassword = new Label();
        this.textPassword.setText("Password");
        this.textPassword.setFont(new Font(15));

        this.textId = new Label();
        this.textId.setText("Nº Utente Saude");
        this.textId.setFont(new Font(13));

        this.inputUsername = new TextField();
        this.inputPass = new TextField();
        this.inputId = new TextField();

        this.listContyName = this.readWriteFiles.spinnerOptions();

        // Botões
        this.register = new Button("Criar Conta");
        Stage mainMenuPage = LoginMenu.getStage();
        this.register.setOnAction(e -> {
            try {
                int id = Integer.parseInt(this.inputId.getText());

                String contentUsername = this.inputUsername.getText();
                String contentPassword = this.inputPass.getText();
                String contentCounty = this.comboBox.getValue();

                boolean conditionRegist = false;

                //id >= 100000000 && id <= 999999999 Muito trabalho para testar
                if ((id >= 100000000 && id <= 999999999) && contentUsername != "" && contentPassword.length() > 5 && contentCounty != "Escolha o seu concelho") {
                    conditionRegist = true;
                }

                if (conditionRegist) {
                    System.out.println("RESPEITO !");

                    boolean isExist = this.readWriteFiles.userExists(id);

                    if (!isExist) {

                        int checkUserUnregisterIndex = this.readWriteFiles.indexUnregisteredUsers(id);

                        this.inputUsername.setText("");
                        this.inputPass.setText("");

                        //Caso que a conta não existe, verifica se o Utilizador não se encontra na lista dos não registado
                        if (checkUserUnregisterIndex != -1) {
                            this.readWriteFiles.removeUnregisteredUsers(checkUserUnregisterIndex);
                            this.readWriteFiles.writeUserRegister(id, contentUsername, contentPassword, true, contentCounty);
                        } else {
                            this.readWriteFiles.writeUserRegister(id, contentUsername, contentPassword, false, contentCounty);
                        }

                        mainMenuPage.setScene(LoginMenu.getScene());
                    } else {
                        AlertUserBox.display("Registo", "Conta já existente");
                    }

                } else {
                    AlertUserBox.display("Registo", "Não respeita as condições para se registar");
                }

            } catch (NumberFormatException ex) {
                AlertUserBox.display("Registo", "Nº Utente de Saúde inválido. Introduza só números");
            }
        });

        this.backMenu = new Button("Voltar");
        this.backMenu.setOnAction(e -> mainMenuPage.setScene(LoginMenu.getScene()));

        // Container do titulo
        VBox titleContainer = new VBox(10);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.getChildren().addAll(this.titleContent);

        HBox containerId = new HBox(10);
        containerId.setAlignment(Pos.CENTER);
        containerId.getChildren().addAll(this.textId, this.inputId);

        // Container do preenchimento do login
        HBox containerLoginUsername = new HBox(10);
        containerLoginUsername.setAlignment(Pos.CENTER);
        containerLoginUsername.getChildren().addAll(this.textUsername, this.inputUsername);

        HBox containerLoginPass = new HBox(10);
        containerLoginPass.setAlignment(Pos.CENTER);
        containerLoginPass.getChildren().addAll(this.textPassword, this.inputPass);

        this.comboBox = new ComboBox<>();
        this.comboBox.setValue("Escolha o seu concelho");
        this.comboBox.getItems().addAll(this.listContyName);

        // Ações do click na ComboBox
        this.comboBox.setOnAction(e -> System.out.println("User selected: " + this.comboBox.getValue()));

        VBox mainMenuButtons = new VBox(10);
        mainMenuButtons.setAlignment(Pos.CENTER);
        mainMenuButtons.getChildren().addAll(containerId, containerLoginUsername, containerLoginPass, this.comboBox);

        // Container dos butões
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
