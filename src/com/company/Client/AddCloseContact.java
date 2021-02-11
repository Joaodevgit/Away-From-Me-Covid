package com.company.Client;

import com.company.Models.Client;
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

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Class responsible for managing the actions in "Add Close Contact" stage
 *
 * @author João Pereira
 * @author Paulo da Cunha
 */
public class AddCloseContact extends Application {

    private Label sceneLabel;
    private Label titleLabel;
    private Label descLabel;
    private Label contact;
    private TextField idContactInput;
    protected Button addContactButton;
    protected Button returnMenuButton;
    private Scene addCloseContactScene;
    private Stage addCloseContactWindow;
    private ArrayList<String> listUserId;
    private ComboBox<String> comboBox;
    private Socket socket;

    private PrintWriter out;
    protected static Client client;

    public AddCloseContact(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    /**
     * Method responsible for starting the user interface "Add contacts"
     *
     * @param primaryStage - Main Container of JavaFX
     */
    @Override
    public void start(Stage primaryStage) {

        this.addCloseContactWindow = primaryStage;

        // Method responsible for saving user information, when the user interface window is closed
        this.addCloseContactWindow.setOnCloseRequest(e -> {
            e.consume();

            try {
                out = new PrintWriter(socket.getOutputStream(), true);

                this.client.setCommand("LOGOUT");
                out.println(this.client.toString());

                addCloseContactWindow.close();
                System.exit(-1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        File file = new File("src/com/company/Data/Users.json");

        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listCounty = (JSONArray) obj.get("Registry");

            this.listUserId = new ArrayList<>();
            JSONObject county;
            String idPerson;

            for (int i = 0; i < listCounty.size(); i++) {
                county = (JSONObject) listCounty.get(i);

                if (!(((Long) county.get("id")).intValue() == (this.client.getId()))) {
                    idPerson = county.get("id").toString();
                    listUserId.add(idPerson);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Scene Title
        this.sceneLabel = new Label();
        this.sceneLabel.setText("NEARBY CONTACTS");
        this.sceneLabel.setFont(new Font(30));

        // Description
        this.titleLabel = new Label();
        this.titleLabel.setText("If you were in contact with someone, add the person Health User no. here:");
        this.titleLabel.setFont(new Font(20));

        // Summary
        this.descLabel = new Label();
        this.descLabel.setText("If you want to add multiple contacts, separate the it by \";\".");
        this.descLabel.setFont(new Font(17));

        // Button add contact
        this.addContactButton = new Button("Add");

        // Input Contact ID
        this.idContactInput = new TextField();
        this.addContactButton.setOnAction(e -> {
            try {
                if (this.idContactInput.getText() != "") {

                    this.client.setCommand("ADD CONTACTS");
                    this.client.setListContact(this.client.getId() + ";" + this.idContactInput.getText());

                    this.out = new PrintWriter(socket.getOutputStream(), true);
                    this.out.println(this.client.toString());

                } else {
                    AlertUserBox.display("Nearby Contacts", "Add a contact first!");
                }
            } catch (UnknownHostException ex) {
                System.out.println("Unknown Host.");
                System.exit(1);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });

        this.contact = new Label();
        this.contact.setText("Contacts: ");

        // ComboBox of the registered users
        this.comboBox = new ComboBox<>();
        this.comboBox.setValue("Identify the users");
        this.comboBox.getItems().addAll(listUserId);

        // Click actions on comboBox
        this.comboBox.setOnAction(e -> {
            // System.out.println("User selected: " + comboBox.getValue());

            // System.out.println(this.comboBox.getSelectionModel().getSelectedItem());

            String idPerson = String.valueOf(this.comboBox.getSelectionModel().getSelectedItem());

            String content = this.idContactInput.getText();

            if (content.equals(""))
                content += idPerson;
            else {
                if (!content.contains(idPerson))
                    content += ";" + idPerson;
            }

            this.idContactInput.setText(content);
        });

        // Button "Return to Main Menu"
        this.returnMenuButton = new Button("Return to Main Menu");
        this.returnMenuButton.setOnAction(e -> {
            MenuPage menuPage = null;

            try {
                menuPage = new MenuPage(socket, this.client);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            try {
                menuPage.start(addCloseContactWindow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
