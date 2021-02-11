package com.company.Client;

import com.company.Models.Client;
import com.company.Server.ReadWriteFiles;
import com.company.Server.UDPClientMsgReceiverThread;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

public class LoginMenu extends Application {

    /* Final variables */
    private static final int SCENE_WIDTH = 700;
    private static final int SCENE_HEIGHT = 650;
    private static final int BROADCAST_PORT = 5000;

    /* Stage */
    private static Stage mainMenuWindow;

    /* Scenes */
    private static Scene mainMenuScene;

    /* Main Menu Buttons */
    private Button registerAccount;
    private Button loginAccount;

    /* Main Menu Labels */
    private Label titleLabel;
    private Label textUsername;
    private Label textPassword;

    /* Main Menu Layout */
    private BorderPane borderPanelayout;

    /* Main Menu Text Fields */
    private TextField inputUsername;
    private TextField inputPassword;

    private ReadWriteFiles readWriteFiles = new ReadWriteFiles();

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        mainMenuWindow = primaryStage;

        mainMenuWindow.getIcons().add(new Image("https://user-images.githubusercontent.com/44362304/103882959-761f8c80-50d4-11eb-9e3d-6f4f3c0e276c.png"));

        /* Buttons */
        this.registerAccount = new Button("Create an account");
        this.registerAccount.setOnAction(e -> {
            RegisterPage registerPage = new RegisterPage();
            try {
                registerPage.start(mainMenuWindow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        this.loginAccount = new Button("Login");
        this.loginAccount.setOnAction(e -> {
            File file = new File("src/com/company/Data/Users.json");

            if (file.exists()) {
                // Caso que o ficheiro existe
                try {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

                    JSONArray listUsers = (JSONArray) obj.get("Registry");

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

                        Client client = new Client(((Long) user.get("id")).intValue(),
                                user.get("name").toString(), Boolean.parseBoolean(user.get("isInfected").toString()),
                                Boolean.parseBoolean(user.get("isNotified").toString()),
                                user.get("county").toString());

                        // Client socket responsible for TCP communications with the server
                        Socket socket = new Socket("localhost", 2048);

                        // Client socket responsible for UDP (Multicast) communications with the server
                        MulticastSocket clientMulticastSocket = new MulticastSocket(this.readWriteFiles.getClientCountyPort(client));
                        InetAddress groupMulticast = InetAddress.getByName("230.0.0.1");
                        clientMulticastSocket.joinGroup(groupMulticast);

                        // Client socket responsible for UDP (Broadcast) communications with the server
                        MulticastSocket clientBroadcastSocket = new MulticastSocket(BROADCAST_PORT);
                        InetAddress groupBroadcast = InetAddress.getByName("230.0.0.2");
                        clientBroadcastSocket.joinGroup(groupBroadcast);

                        client.setCommand("LOGIN BUTTON");

                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(client.toString());

                        Platform.runLater(() -> {
                            try {
                                new MsgReceiverThread(socket).start();
                                new UDPClientMsgReceiverThread(clientMulticastSocket).start();
                                new UDPClientMsgReceiverThread(clientBroadcastSocket).start();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        });

                        MenuPage menuPage = new MenuPage(socket, client);

                        try {
                            menuPage.start(mainMenuWindow);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        AlertUserBox.display("Login", "User does not exist");
                    }

                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                // In case of file not existing
                System.out.println("The file does not exist");
            }
        });

        /* Main Menu Labels */

        // Main Menu Title
        titleLabel = new Label();
        titleLabel.setText("Login");
        titleLabel.setFont(new Font(30));

        this.textUsername = new Label();
        this.textUsername.setText("Username");
        this.textUsername.setFont(new Font(15));

        this.textPassword = new Label();
        this.textPassword.setText("Password");
        this.textPassword.setFont(new Font(15));

        /* Login InputForm */
        this.inputUsername = new TextField();
        this.inputPassword = new TextField();

        /* Main Menu Containers */

        // Login Container (Username)
        HBox containerLoginUsername = new HBox(10);
        containerLoginUsername.setAlignment(Pos.CENTER);
        containerLoginUsername.getChildren().addAll(this.textUsername, this.inputUsername);

        // Login Container (Password)
        HBox containerLoginPass = new HBox(10);
        containerLoginPass.setAlignment(Pos.CENTER);
        containerLoginPass.getChildren().addAll(this.textPassword, this.inputPassword);

        // Title Container
        VBox titleBoxContainer = new VBox(10);
        titleBoxContainer.setAlignment(Pos.CENTER);
        titleBoxContainer.getChildren().addAll(titleLabel);

        // Central Buttons Container
        VBox mainMenuButtons = new VBox(50);
        mainMenuButtons.setAlignment(Pos.CENTER);
        mainMenuButtons.getChildren().addAll(containerLoginUsername, containerLoginPass, this.loginAccount,
                this.registerAccount, titleBoxContainer);

        // Border Pane Layout
        borderPanelayout = new BorderPane();
        borderPanelayout.setCenter(mainMenuButtons);
        borderPanelayout.setTop(titleBoxContainer);

        // Main Menu Scene
        mainMenuScene = new Scene(borderPanelayout, SCENE_WIDTH, SCENE_HEIGHT);
        mainMenuWindow.setScene(mainMenuScene);
        mainMenuWindow.setTitle("Away From Me Covid");
        mainMenuWindow.show();
    }
}