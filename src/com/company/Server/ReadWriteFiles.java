package com.company.Server;

import com.company.Models.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

/**
 * Class responsible for managing all the operations of writing and reading from a JSON file
 *
 * @author João Pereira
 * @author Paulo da Cunha
 */
public class ReadWriteFiles {

    private final File file;
    private JSONParser jsonParser;

    public ReadWriteFiles() {
        this.file = new File("src/com/company/Data/Users.json");
        this.jsonParser = new JSONParser();
    }


    /**
     * Method responsible for writing client information to a JSON file while the client is using the application
     *
     * @param client client object (model)
     */
    public void writeJSONFile(Client client) {
        try {
            this.jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listUsers = (JSONArray) obj.get("Registry");

            boolean found = false;
            JSONObject user;
            int i = 0;

            while (!found && i < listUsers.size()) {
                user = (JSONObject) listUsers.get(i);

                if (client.getId() == ((Long) user.get("id")).intValue()) {
                    found = true;
                    ((JSONObject) listUsers.get(i)).put("isInfected", client.isInfected());
                    ((JSONObject) listUsers.get(i)).put("isNotified", client.isNotified());
                }

                i++;
            }

            JSONArray county = (JSONArray) obj.get("Counties");
            JSONArray unregisteredUsers = (JSONArray) obj.get("UnregisteredUsers");

            if (unregisteredUsers == null) {
                unregisteredUsers = new JSONArray();
            }

            JSONObject objWrite = new JSONObject();

            objWrite.put("UnregisteredUsers", unregisteredUsers);
            objWrite.put("Registry", listUsers);
            objWrite.put("Counties", county);

            FileWriter fileWriter = new FileWriter(file.getPath());

            fileWriter.write(objWrite.toJSONString());

            fileWriter.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method responsible for verifying whether a client exists or not, given its id in the JSON file
     *
     * @param id client's id
     * @return true if the client exists, otherwise it returns false
     */
    public boolean userExists(int id) {
        boolean exist = false;

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listUsers = (JSONArray) obj.get("Registry");

            int i = 0;
            JSONObject user;

            while (!exist && i < listUsers.size()) {
                user = (JSONObject) listUsers.get(i);

                if (Integer.parseInt(user.get("id").toString()) == id) {
                    exist = true;
                }

                i++;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return exist;
    }

    /**
     * Method responsible for returning the index of the client's position that is not yet registered in the application
     *
     * @param id client's id
     * @return the client's position if it exists, otherwise it returns -1
     */
    public int indexUnregisteredUsers(int id) {
        int position = -1;

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listUsers = (JSONArray) obj.get("UnregisteredUsers");

            int i = 0;
            JSONObject user;
            boolean found = false;

            while (!found && i < listUsers.size()) {
                user = (JSONObject) listUsers.get(i);

                if (Integer.parseInt(user.get("id").toString()) == id) {
                    found = true;
                } else {
                    i++;
                }
            }

            if (found) {
                position = i;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return position;
    }

    /**
     * Method responsible for removing the client from the JSON file, given its position in the array of registered clients
     *
     * @param position position of the removed client
     */
    public void removeUnregisteredUsers(int position) {
        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));
            JSONArray listUsersUnregistered = (JSONArray) obj.get("UnregisteredUsers");

            listUsersUnregistered.remove(position);

            JSONArray county = (JSONArray) obj.get("Counties");
            JSONArray unregisteredUsers = (JSONArray) obj.get("Registry");

            JSONObject objWrite = new JSONObject();

            objWrite.put("UnregisteredUsers", listUsersUnregistered);
            objWrite.put("Registry", unregisteredUsers);
            objWrite.put("Counties", county);

            FileWriter fileWriter = new FileWriter(file.getPath());

            fileWriter.write(objWrite.toJSONString());

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method responsible for writing client information to a JSON file when the client registers
     *
     * @param id         client's id
     * @param username   client's username
     * @param password   client's password
     * @param isNotified whether it has already been notified or not (true / false)
     * @param county     client's county
     */
    public void writeUserRegister(int id, String username, String password, boolean isNotified, String county) {
        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONObject newRegister = new JSONObject();

            JSONArray registerlist = (JSONArray) obj.get("Registry");

            newRegister.put("id", id);
            newRegister.put("name", username);
            newRegister.put("password", password);
            newRegister.put("county", county);
            newRegister.put("isInfected", false);
            newRegister.put("isNotified", isNotified);

            registerlist.add(newRegister);

            JSONArray countyList = (JSONArray) obj.get("Counties");
            JSONArray unregisteredUsers = (JSONArray) obj.get("UnregisteredUsers");

            JSONObject objWrite = new JSONObject();

            objWrite.put("UnregisteredUsers", unregisteredUsers);
            objWrite.put("Registry", registerlist);
            objWrite.put("Counties", countyList);

            FileWriter fileWriter = new FileWriter(file.getPath());

            fileWriter.write(objWrite.toJSONString());

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method responsible for adding a contact that is not yet registered in the application
     *
     * @param id unregistered contact id
     */
    public void updateNotificationContactUser(int id) {
        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listUsers = (JSONArray) obj.get("Registry");

            int i = 0;
            boolean found = false;
            JSONObject user;

            while (!found && i < listUsers.size()) {
                user = (JSONObject) listUsers.get(i);

                if (Integer.parseInt(user.get("id").toString()) == id) {
                    found = true;
                    ((JSONObject) listUsers.get(i)).put("isNotified", true);
                } else {
                    i++;
                }
            }

            JSONArray county = (JSONArray) obj.get("Counties");
            JSONArray unregisteredUsers = (JSONArray) obj.get("UnregisteredUsers");

            JSONObject objWrite = new JSONObject();

            objWrite.put("UnregisteredUsers", unregisteredUsers);
            objWrite.put("Registry", listUsers);
            objWrite.put("Counties", county);

            FileWriter fileWriter = new FileWriter(file.getPath());

            fileWriter.write(objWrite.toJSONString());

            fileWriter.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method responsible for writing to a JSON file users who have been added to nearby contacts and who are not yet
     * registered in the application
     *
     * @param id unregistered client id
     */
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

                JSONArray county = (JSONArray) obj.get("Counties");
                JSONArray register = (JSONArray) obj.get("Registry");

                JSONObject objWrite = new JSONObject();

                objWrite.put("UnregisteredUsers", unregisteredUser);
                objWrite.put("Registry", register);
                objWrite.put("Counties", county);

                FileWriter fileWriter = new FileWriter(file.getPath());

                fileWriter.write(objWrite.toJSONString());

                fileWriter.close();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method responsible for loading the spinner, from the JSON file with the name of the counties
     *
     * @return options list
     */
    public ArrayList<String> spinnerOptions() {
        ArrayList<String> options = new ArrayList<>();

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listCounty = (JSONArray) obj.get("Counties");

            JSONObject county;

            for (int i = 0; i < listCounty.size(); i++) {
                county = (JSONObject) listCounty.get(i);

                options.add(county.get("name").toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return options;
    }


    /**
     * Method responsible for obtaining the multicast ip port of a given client's county, receiving the client object as
     * a parameter
     *
     * @param client client object (model)
     * @return client's port
     */
    public int getClientCountyPort(Client client) {

        File file = new File("src/com/company/Data/Users.json");

        if (file.exists()) {
            // Caso o ficheiro exista
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

                JSONArray listCounties = (JSONArray) obj.get("Counties");

                JSONObject countyObj;

                for (int i = 0; i < listCounties.size(); i++) {
                    countyObj = (JSONObject) listCounties.get(i);
                    if (client.getCounty().equals(countyObj.get("name").toString())) {
                        return Integer.parseInt(countyObj.get("port").toString());
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }


    /**
     * Method responsible for obtaining the total number of infected people in a given county, taking the name of the
     * county as a parameter
     *
     * @param countyName county's name
     * @return total number of infected in the given county
     */
    public int getCountyTotalInfected(String countyName) {

        File file = new File("src/com/company/Data/Users.json");

        if (file.exists()) {
            // Caso que o ficheiro exista
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

                JSONArray listCounties = (JSONArray) obj.get("Counties");

                JSONObject countyObj;

                for (int i = 0; i < listCounties.size(); i++) {
                    countyObj = (JSONObject) listCounties.get(i);
                    if (countyName.equals(countyObj.get("name").toString())) {
                        return Integer.parseInt(countyObj.get("infectedTot").toString());
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -2;
    }


    /**
     * Method responsible for obtaining the total number of infected people in the sub-region of Tâmega and Vale do Sousa
     *
     * @return total number of infected in the Tâmega and Vale do Sousa sub-region
     */
    public int getSubRegionTotalInfected() {

        File file = new File("src/com/company/Data/Users.json");
        int totalInfected = 0;

        if (file.exists()) {
            // Caso que o ficheiro exista
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

                JSONArray listCounties = (JSONArray) obj.get("Counties");

                JSONObject countyObj;

                for (int i = 0; i < listCounties.size(); i++) {
                    countyObj = (JSONObject) listCounties.get(i);

                    totalInfected += Integer.parseInt(countyObj.get("infectedTot").toString());
                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return totalInfected;
    }


    /**
     * Method responsible for adding a person to the total number of infected in a given county
     *
     * @param county county's name
     */
    public void addInfectedCounty(String county) {

        File file = new File("src/com/company/Data/Users.json");

        if (file.exists()) {
            // Caso que o ficheiro exista
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

                JSONArray listCounties = (JSONArray) obj.get("Counties");

                JSONObject countyObj;
                boolean found = false;

                for (int i = 0; !found && i < listCounties.size(); i++) {
                    countyObj = (JSONObject) listCounties.get(i);

                    if (countyObj.get("name").equals(county)) {
                        found = true;
                        int totalAdd = Integer.parseInt(countyObj.get("infectedTot").toString()) + 1;

                        ((JSONObject) listCounties.get(i)).put("infectedTot", totalAdd);
                    }

                }

                JSONArray unregisteredUsers = (JSONArray) obj.get("UnregisteredUsers");
                JSONArray register = (JSONArray) obj.get("Registry");

                JSONObject objWrite = new JSONObject();

                objWrite.put("UnregisteredUsers", unregisteredUsers);
                objWrite.put("Registry", register);
                objWrite.put("Counties", listCounties);

                FileWriter fileWriter = new FileWriter(file.getPath());

                fileWriter.write(objWrite.toJSONString());

                fileWriter.close();

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
