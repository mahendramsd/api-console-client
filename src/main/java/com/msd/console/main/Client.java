package com.msd.console.main;


import com.msd.console.util.ApiService;
import com.msd.console.util.HttpRequestClient;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.json.JSONArray;
import org.json.JSONObject;


public class Client {

    static volatile String token = "";
    static volatile TextIO console = null;

    public static void main(String args[]) throws Exception {
        init();
    }

    private static void init() {
        if (console == null) {
            console = TextIoFactory.getTextIO();
        }
        console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< Welcome API-Console Client >>>>>>>>>>>>>>>>>>>");

        String userName = console.newStringInputReader().read("Username");
        String password = console.newStringInputReader().withInputMasking(true).read("Password");

        token = getToken(console, userName, password);
        if (!token.equals("")) {
            newOption();
        } else {
            exitConsole();
        }
    }


    /**
     * Get service option
     *
     * @param apiService
     */
    private static void loadOptions(ApiService apiService) {

        switch (apiService) {
            case ADD_PERSON:
                addPerson(console);
                break;
            case VIEW_PERSON:
                viewPerson(console);
                break;
            case EDIT_PERSON:
                editPerson(console);
                break;
            case DELETE_PERSON:
                deletePerson(console);
                break;
            case LOGOUT_PERSON:
                logOutPerson(console);
                break;
            case EXIT:
                exitConsole();
                break;
        }
    }

    /**
     * Logout Console
     *
     * @param console
     */
    private static void logOutPerson(TextIO console) {
        console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< Logout the console  >>>>>>>>>>>>>>>>>>>");
        console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< Thank You  >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        console.dispose();
        init();
    }

    private static void deletePerson(TextIO console) {

        console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<<<<< Delete Person  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        int personId = console.newIntInputReader().read("Person Id");
        int save = console.newIntInputReader().read("Enter 1 : Delete to Confirmation ! ");
        if (save == 1) {
            JSONObject delete = null;
            try {
                String response = HttpRequestClient.deletePerson(token, personId);
                if (response.equals("")) {
                    console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< Person delete Successfully >>>>>>>>>>>>>>>>>>");
                } else {
                    delete = new JSONObject(response);
                    console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< " + delete.getString("error") + " >>>>>>>>>>>>>>>>>>");
                }
                newOption();
            } catch (Exception e) {
                console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< " + delete.getString("message") + " >>>>>>>>>>>>>>>>>>");
                String exit = console.newStringInputReader().read("Exit to console : Enter 1");
                if (exit.equals("1")) {
                    console.dispose();
                }
            }
        } else {
            console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< Wrong input >>>>>>>>>>>>>>>>>>");
            newOption();
        }
    }

    private static void editPerson(TextIO console) {
        console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<<<<< Edit Person  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        int personId = console.newIntInputReader().read("Enter Person Id ");
        JSONObject person = null;
        try {
            person = HttpRequestClient.getPerson(token, personId);

            int id = person.getInt("id");
            String firstName = person.getString("firstName");
            String lastName = person.getString("lastName");
            int age = person.getInt("age");
            String favouriteColor = person.getString("favouriteColor");
            String hobby = person.getString("hobby");

            console.getTextTerminal().printf("Id: %d  FirstName: %s  LastName: %s  Age: %d  Favourite Color: %s  Hobby: %s \n",
                    id, firstName, lastName, age, favouriteColor, hobby);


            int edit = console.newIntInputReader().read("Enter 1 : Edit to Person | Enter 0 : Exit to Console");
            if (edit == 1) {
                String firstName1 = console.newStringInputReader().read("FirstName");
                String lastName1 = console.newStringInputReader().read("LastName");
                int age1 = console.newIntInputReader().read("Age");
                String favouriteColor1 = console.newStringInputReader().read("FavouriteColor");
                String hobby1 = console.newStringInputReader().read("Hobby (GAME,FILM)");
                int save = console.newIntInputReader().read("Enter 1 : Edit Confirmation | Enter 2 : Cancel to Edit");
                if (save == 1) {
                    JSONObject personNew = null;
                    try {
                        personNew = HttpRequestClient.savePerson(token, id, firstName1, lastName1, age1, favouriteColor1, hobby1);
                        console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< Person Update Successfully >>>>>>>>>>>>>>>>>>");

                        newOption();
                    } catch (Exception e) {
                        console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< " + personNew.getString("error") + " >>>>>>>>>>>>>>>>>>");

                        String exit = console.newStringInputReader().read("Exit to console : Enter 1");
                        if (exit.equals("1")) {
                            console.dispose();
                        }
                    }
                } else if (save == 2) {
                    console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<<<<< Canceled edit  : " + firstName + " >>>>>>>>>>>>>>>>>>>>>>");
                    newOption();
                } else {
                    exitConsole();
                }
            }
        } catch (Exception e) {
            console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< " + person.getString("error") + " >>>>>>>>>>>>>");
            String exit = console.newStringInputReader().read("Exit to console : Enter 1");
            if (exit.equals("1")) {
                exitConsole();
            }
        }
    }

    /**
     * view all persons
     *
     * @param console
     */
    private static void viewPerson(TextIO console) {
        console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<<<<< View Person details  >>>>>>>>>>>>>>>>>>>>");
        console.getTextTerminal().println("-----------------------------------------------------------------------------------");
        try {
            JSONArray persons = HttpRequestClient.getAllPerson(token);
            if (persons.isEmpty()) {
                console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<<<<< No Person details  >>>>>>>>>>>>>>>>>>>>");
                newOption();
            }
            for (Object ob : persons) {
                JSONObject person = new JSONObject(ob.toString());
                int id = person.getInt("id");
                String firstName = person.getString("firstName");
                String lastName = person.getString("lastName");
                int age = person.getInt("age");
                String favouriteColor = person.getString("favouriteColor");
                String hobby = person.getString("hobby");
                console.getTextTerminal().printf("Id: %d  FirstName: %s  LastName: %s  Age: %d  Favourite Color: %s  Hobby: %s \n",
                        id, firstName, lastName, age, favouriteColor, hobby);
                console.getTextTerminal().println("-----------------------------------------------------------------------------------");
            }
            newOption();
        } catch (Exception e) {
            console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<<<<<  " + e.getMessage() + "  >>>>>>>>>>>>>>>>>>>>");

        }

    }

    private static void addPerson(TextIO console) {
        console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<<<<< Add Person  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        String firstName = console.newStringInputReader().read("FirstName");
        String lastName = console.newStringInputReader().read("LastName");
        int age = console.newIntInputReader().read("Age");
        String favouriteColor = console.newStringInputReader().read("FavouriteColor");
        String hobby = console.newStringInputReader().read("Hobby (GAME,FILM)");
        int save = console.newIntInputReader().read("Enter 1 : Save to Person | Enter 0 : Exit to Console");
        if (save == 1) {
            JSONObject person = null;
            try {
                person = HttpRequestClient.savePerson(token, 0, firstName, lastName, age, favouriteColor, hobby);
                console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< Person Added Successfully >>>>>>>>>>>>>>>>>>");
                newOption();
            } catch (Exception e) {
                console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< " + person.getString("error") + " >>>>>>>>>>>>>>>>>>");

                String exit = console.newStringInputReader().read("Exit to console : Enter 0");
                if (exit.equals("0")) {
                    exitConsole();
                }
            }
        }
    }

    /**
     * Go to the new Option
     */
    private static void newOption() {
        ApiService apiService = console.newEnumInputReader(ApiService.class)
                .read("What service were you want ?");
        loadOptions(apiService);
    }


    /**
     * Exit console
     */
    private static void exitConsole() {
        console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< System Closed ! >>>>>>>>>>>>>>>>>>>");
        console.getTextTerminal().println("................... Good Bye ...........................");
        console.dispose();
    }

    /**
     * Get Token
     *
     * @param console
     * @param userName
     * @param password
     * @return
     */
    private static String getToken(TextIO console, String userName, String password) {
        String tokenString = "";
        JSONObject token = null;
        try {
            token = HttpRequestClient.authenticate(userName, password);
            if (token.has("token")) {
                console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< Authentication Successfully >>>>>>>>>>>>>>>>>>");
                console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< API Connected >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                tokenString = "Bearer " + token.getString("token");
            } else {
                console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< " + token.getString("error") + " >>>>>>>>>>>>>>>>>>");
                console.getTextTerminal().println("<<<<<< USERNAME OR PASSWORD INCORRECT | TRY AGAIN  >>>>>>>>>>>>>>");
                init();
            }
        } catch (Exception e) {
            console.getTextTerminal().println("<<<<<<<<<<<<<<<<<<< " + e.getMessage() + " >>>>>>>>>>>>>>>>>>");
            String exit = console.newStringInputReader().read("Exit to console : Enter 1");
            if (exit.equals("1")) {
                console.dispose();
            }
        }
        return tokenString;
    }

}


