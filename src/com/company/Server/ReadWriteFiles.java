package com.company.Server;

import com.company.Models.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class ReadWriteFiles {

    private final File file;
    private JSONParser jsonParser;

    public ReadWriteFiles() {
        this.file = new File("src/com/company/Data/Users.json");
        this.jsonParser = new JSONParser();
    }


    /**
     * Método responsável por escrever para ficheiro as informações do cliente enquanto este usa a aplicação
     *
     * @param client objeto do cliente (modelo)
     */
    public void writeJSONFile(Client client) {
        try {
            this.jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listUsers = (JSONArray) obj.get("Registo");

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

    /**
     * Método responsável por verificar se um cliente, existe ou não, dado o seu id no ficheiro JSON
     *
     * @param id id do cliente
     * @return true se o cliente existe, caso contrário retorna false
     */
    public boolean userExists(int id) {
        boolean exist = false;

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listUsers = (JSONArray) obj.get("Registo");

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
     * Método responsável por retornar o índice da posição do cliente que ainda não está registado na aplicação
     *
     * @param id id do cliente
     * @return a posição do cliente se este existir, caso contrário retorna -1
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
     * Método responsável por remover o cliente do ficheiro JSON, dada a sua posição no array de clientes registados
     *
     * @param position posição do cliente a ser removido
     */
    public void removeUnregisteredUsers(int position) {
        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));
            JSONArray listUsersUnregistered = (JSONArray) obj.get("UnregisteredUsers");

            listUsersUnregistered.remove(position);

            JSONArray county = (JSONArray) obj.get("Concelhos");
            JSONArray unregisteredUsers = (JSONArray) obj.get("Registo");

            JSONObject objWrite = new JSONObject();

            objWrite.put("UnregisteredUsers", listUsersUnregistered);
            objWrite.put("Registo", unregisteredUsers);
            objWrite.put("Concelhos", county);

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
     * Método responsável por escrever para ficheiro as informações do cliente quando este se regista
     *
     * @param id         id do cliente
     * @param username   username do cliente
     * @param password   password do cliente
     * @param isNotified se já foi notificado ou não (true/false)
     * @param county     concelho do cliente
     */
    public void writeUserRegister(int id, String username, String password, boolean isNotified, String county) {
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
            newRegister.put("isNotified", isNotified);

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

    /**
     * Método responsável por adicionar um contato que ainda não está registado na aplicação
     *
     * @param id id do contato não registado
     */
    public void updateNotificationContactUser(int id) {
        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listUsers = (JSONArray) obj.get("Registo");

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

    /**
     * Método responsável por escrever para ficheiro os utilizadores que foram adicionados aos contatos próximos e que
     * ainda não se encontram registados na aplicação
     *
     * @param id id do cliente não registado
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

    /**
     * Método responsável por carregar o spinner, do ficheiro JSON com o nome dos concelhos
     *
     * @return a lista de opções
     */
    public ArrayList<String> spinnerOptions() {
        ArrayList<String> options = new ArrayList<>();

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

            JSONArray listCounty = (JSONArray) obj.get("Concelhos");

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
     * Método responsável por obter a porta do ip multicast do concelho de um dado cliente, recebendo como parâmetro o objeto cliente
     *
     * @param client objeto do cliente (modelo)
     * @return a porta do cliente
     */
    public int getClientCountyPort(Client client) {

        File file = new File("src/com/company/Data/Users.json");

        if (file.exists()) {
            // Caso o ficheiro exista
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

                JSONArray listCounties = (JSONArray) obj.get("Concelhos");

                JSONObject countyObj;

                for (int i = 0; i < listCounties.size(); i++) {
                    countyObj = (JSONObject) listCounties.get(i);
                    if (client.getCounty().equals(countyObj.get("name").toString())) {
                        return Integer.parseInt(countyObj.get("porta").toString());
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
     * Método responsável por obter o nº total de infetados de um dado concelho, recebendo como parâmetro o nome do concelho
     *
     * @param countyName nome do concelho
     * @return nº total de infetados do concelho pretendido
     */
    public int getCountyTotalInfected(String countyName) {

        File file = new File("src/com/company/Data/Users.json");

        if (file.exists()) {
            // Caso que o ficheiro exista
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

                JSONArray listCounties = (JSONArray) obj.get("Concelhos");

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
     * Método responsável por obter o nº total de infetados da sub-região do Tâmega e Vale do Sousa
     *
     * @return nº total de infetados da sub-região do Tâmega e Vale do Sousa
     */
    public int getSubRegionTotalInfected() {

        File file = new File("src/com/company/Data/Users.json");
        int totalInfected = 0;

        if (file.exists()) {
            // Caso que o ficheiro exista
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

                JSONArray listCounties = (JSONArray) obj.get("Concelhos");

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
     * Método responsável por adicionar mais uma pessoa no total de infectados de um determinado concelho
     *
     * @param county nome do concelho
     */
    public void addInfectedCounty(String county) {

        File file = new File("src/com/company/Data/Users.json");

        if (file.exists()) {
            // Caso que o ficheiro exista
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(file.getPath()));

                JSONArray listCounties = (JSONArray) obj.get("Concelhos");

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
                JSONArray register = (JSONArray) obj.get("Registo");

                JSONObject objWrite = new JSONObject();

                objWrite.put("UnregisteredUsers", unregisteredUsers);
                objWrite.put("Registo", register);
                objWrite.put("Concelhos", listCounties);

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
