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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

    private ArrayList<String> listContyName;

    @Override
    public void start(Stage primaryStage) {
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

        File file = new File("src/com/company/Data/Users.json");

        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listCounty = (JSONArray) obj.get("Concelhos");

            listContyName = new ArrayList<>();
            JSONObject county = null;

            for (int i = 0; i < listCounty.size(); i++) {
                county = (JSONObject) listCounty.get(i);

                listContyName.add(county.get("name").toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Botões
        this.register = new Button("Criar Conta");
        Stage mainMenuPage = LoginMenu.getStage();
        this.register.setOnAction(e -> {
            String contentUsername = this.inputUsername.getText();
            String contentPassword = this.inputPass.getText();
            String contentCounty = this.comboBox.getValue();

            boolean conditionRegist = false;

            if (contentUsername != "" && contentPassword.length() > 5 && contentCounty != "Escolha o seu concelho") {
                conditionRegist = true;
            }

            if (conditionRegist) {
                System.out.println("RESPEITO !");

                this.inputUsername.setText("");
                this.inputPass.setText("");

                try {
                    JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

                    JSONArray registerlist = (JSONArray) obj.get("Registo");

                    boolean isExist = false;

                    for (int i = 0; !isExist && i < registerlist.size(); i++) {
                        JSONObject register = (JSONObject) registerlist.get(i);

                        if (register.get("name").equals(contentUsername))
                            isExist = true;
                    }

                    if (!isExist) {
                        JSONObject newRegister = new JSONObject();

                        System.out.println("TAM: " + registerlist.size());

                        newRegister.put("id", registerlist.size());
                        newRegister.put("name", contentUsername);
                        newRegister.put("password", contentPassword);
                        newRegister.put("county", contentCounty);
                        newRegister.put("isInfected", false);

                        registerlist.add(newRegister);

                        JSONArray county = (JSONArray) obj.get("Concelhos");

                        JSONObject obgWrite = new JSONObject();

                        obgWrite.put("Registo", registerlist);
                        obgWrite.put("Concelhos", county);

                        FileWriter fileWriter = new FileWriter(file.getPath());

                        fileWriter.write(obgWrite.toJSONString());

                        fileWriter.close();

                        mainMenuPage.setScene(LoginMenu.getScene());
                    } else {
                        AlertUserBox.display("Registo", "Conta já existente");
                    }

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

            } else {
                AlertUserBox.display("Registo", "Não respeita as condições para se registar");
            }

        });

        this.backMenu = new Button("Voltar");
        this.backMenu.setOnAction(e -> mainMenuPage.setScene(LoginMenu.getScene()));

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
        this.comboBox.getItems().addAll(listContyName);

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


        this.registerPageScene = new Scene(borderPanelayout, LoginMenu.getSceneWidth(), LoginMenu.getSceneHeight());

        registerPageWindow.setScene(registerPageScene);
        registerPageWindow.show();
    }
}
