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
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class AddCloseContact extends Application {

    private Label sceneLabel;
    private Label titleLabel;
    private Label descLabel;
    private Label contact;
    private Label idContactInput;
    protected Button addContactButton;
    protected Button returnMenuButton;
    private Scene addCloseContactScene;
    private Stage addCloseContactWindow;
    private ArrayList<String> listUserName;
    private ComboBox<String> comboBox;

    BufferedReader in;
    PrintWriter out;

    @Override
    public void start(Stage primaryStage) throws IOException {

        this.addCloseContactWindow = primaryStage;

        File file = new File("src/com/company/Data/Users.json");

        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listCounty = (JSONArray) obj.get("Registo");

            this.listUserName = new ArrayList<>();
            JSONObject county;
            int idPerson;
            String namePerson;

            for (int i = 0; i < listCounty.size(); i++) {
                county = (JSONObject) listCounty.get(i);

                namePerson = county.get("name").toString();

                listUserName.add(namePerson);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

        // Botão Adicionar Contacto
        this.addContactButton = new Button("Adicionar");

        // Input ID Contacto
        this.idContactInput = new Label();
        Socket socket = new Socket("Asus", 2048);
        this.addContactButton.setOnAction(e -> {
            try {
                if (this.idContactInput.getText() != "") {
                    this.out = new PrintWriter(socket.getOutputStream(), true);
                    this.out.println(this.idContactInput.getText());
                    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String closeContact;

                    if ((closeContact = in.readLine()) != null) {
                        AlertUserBox.display("Contatos Próximos", closeContact);
                    }
                }else{
                    AlertUserBox.display("Contatos Próximos", "Adicione um contato primeiro!");
                }
            } catch (UnknownHostException ex) {
                System.out.println("Unknown Host.");
                System.exit(1);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });

        this.contact = new Label();
        this.contact.setText("Contactos: ");

        // ComboBox de Pessoas registado
        this.comboBox = new ComboBox<>();
        this.comboBox.setValue("Escolha as pessoas");
        this.comboBox.getItems().addAll(listUserName);

        // Click on a item from combobox
        this.comboBox.setOnAction(e -> {
            System.out.println("User selected: " + comboBox.getValue());

            System.out.println(this.comboBox.getSelectionModel().getSelectedIndex());

            String idPerson = String.valueOf(this.comboBox.getSelectionModel().getSelectedIndex());

            String content = this.idContactInput.getText();

            if (content.equals(""))
                content += idPerson;
            else {
                if (!content.contains(idPerson))
                    content += ";" + idPerson;
            }

            this.idContactInput.setText(content);
        });

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
        inputContainer.getChildren().addAll(this.contact, this.comboBox, this.idContactInput);

        VBox contentContainer = new VBox(10);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.getChildren().addAll(inputContainer, this.addContactButton, this.returnMenuButton);

        BorderPane borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(contentContainer);
        borderPanelayout.setTop(titlesContainer);

        this.addCloseContactScene = new Scene(borderPanelayout, LoginMenu.getSceneWidth(), LoginMenu.getSceneHeight());

        this.addCloseContactWindow.setScene(this.addCloseContactScene);
        this.addCloseContactWindow.show();
    }
}
