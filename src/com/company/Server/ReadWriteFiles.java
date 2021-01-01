package com.company.Server;

import com.company.Models.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReadWriteFiles {

    private final File file;
    private JSONParser jsonParser;

    public ReadWriteFiles() {
        this.file = new File("src/com/company/Data/Users.json");
        this.jsonParser = new JSONParser();
    }

    // Quando o Utilizador fecha a aplicação, a sua informação fica guardada
    public void writeJSONFile(Client userInfo) {
        try {
            this.jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listUsers = (JSONArray) obj.get("Registo");

            boolean found = false;
            JSONObject user;
            int i = 0;

            while (!found && i < listUsers.size()) {
                user = (JSONObject) listUsers.get(i);

                if (userInfo.getId() == ((Long) user.get("id")).intValue()) {
                    found = true;
                    ((JSONObject) listUsers.get(i)).put("isInfected", userInfo.isInfected());
                }

                i++;
            }

            JSONArray county = (JSONArray) obj.get("Concelhos");
            JSONArray unregisteredUsers = (JSONArray) obj.get("UnregisteredUsers");

            if (unregisteredUsers == null) {
                unregisteredUsers = new JSONArray();
            }

            JSONObject objWrite = new JSONObject();

            objWrite.put("UnregisteredUsers", unregisteredUsers);
            objWrite.put("Registo", listUsers);
            objWrite.put("Concelhos", county);

            FileWriter fileWriter = new FileWriter(file.getPath());

            fileWriter.write(objWrite.toJSONString());

            fileWriter.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    //TODO: FAZER ACTUALIZAçÃO disto
    public boolean userExists(int id) {
        boolean exist = false;

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));


            JSONArray listUsers = (JSONArray) obj.get("Registo");

            if (id < listUsers.size()) {
                exist = true;
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return exist;
    }

    public boolean userExists(String name) {
        boolean isExist = false;

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray registerlist = (JSONArray) obj.get("Registo");

            for (int i = 0; !isExist && i < registerlist.size(); i++) {
                JSONObject register = (JSONObject) registerlist.get(i);

                if (register.get("name").equals(name))
                    isExist = true;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return isExist;
    }

    public void writeUserRegister(int id, String username, String password, String county) {
        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONObject newRegister = new JSONObject();

            JSONArray registerlist = (JSONArray) obj.get("Registo");

            System.out.println("TAM: " + registerlist.size());

            newRegister.put("id", id);
            newRegister.put("name", username);
            newRegister.put("password", password);
            newRegister.put("county", county);
            newRegister.put("isInfected", false);
            newRegister.put("isNotified", false);

            registerlist.add(newRegister);

            JSONArray countyList = (JSONArray) obj.get("Concelhos");
            JSONArray unregisteredUsers = (JSONArray) obj.get("UnregisteredUsers");


            JSONObject objWrite = new JSONObject();

            objWrite.put("Registo", registerlist);
            objWrite.put("Concelhos", countyList);
            objWrite.put("UnregisteredUsers", unregisteredUsers);

            FileWriter fileWriter = new FileWriter(file.getPath());

            fileWriter.write(objWrite.toJSONString());

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void updateNotificationContactUser(int id) {
        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listUsers = (JSONArray) obj.get("Registo");

            ((JSONObject) listUsers.get(id)).put("isNotified", true);

            JSONArray county = (JSONArray) obj.get("Concelhos");
            JSONArray unregisteredUsers = (JSONArray) obj.get("UnregisteredUsers");

            JSONObject objWrite = new JSONObject();

            objWrite.put("UnregisteredUsers", unregisteredUsers);
            objWrite.put("Registo", listUsers);
            objWrite.put("Concelhos", county);

            FileWriter fileWriter = new FileWriter(file.getPath());

            fileWriter.write(objWrite.toJSONString());

            fileWriter.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void writeUnregisteredUsers(int id) {
        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray unregisteredUser = (JSONArray) obj.get("UnregisteredUsers");

            boolean isExist = false;
            JSONObject idUnregistered;

            for (int i = 0; !isExist && i < unregisteredUser.size(); i++) {
                idUnregistered = (JSONObject) unregisteredUser.get(i);

                if (Integer.parseInt(idUnregistered.get("id").toString()) == id) {
                    isExist = true;
                }
            }

            if (!isExist) {
                JSONObject unregisteredUsers = new JSONObject();

                unregisteredUsers.put("id", id);

                unregisteredUser.add(unregisteredUsers);

                JSONArray county = (JSONArray) obj.get("Concelhos");
                JSONArray register = (JSONArray) obj.get("Registo");

                JSONObject objWrite = new JSONObject();

                objWrite.put("UnregisteredUsers", unregisteredUser);
                objWrite.put("Registo", register);
                objWrite.put("Concelhos", county);

                FileWriter fileWriter = new FileWriter(file.getPath());

                fileWriter.write(objWrite.toJSONString());

                fileWriter.close();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}
