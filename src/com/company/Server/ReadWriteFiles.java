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

    File file;

    public ReadWriteFiles() {
        this.file = new File("src/com/company/Data/Users.json");
    }

    public void writeJSONFile(Client userInfo) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listUsers = (JSONArray) obj.get("Registo");

            boolean found = false;
            JSONObject user = null;
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

            JSONObject obgWrite = new JSONObject();

            obgWrite.put("Registo", listUsers);
            obgWrite.put("Concelhos", county);

            FileWriter fileWriter = new FileWriter(file.getPath());

            fileWriter.write(obgWrite.toJSONString());

            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}