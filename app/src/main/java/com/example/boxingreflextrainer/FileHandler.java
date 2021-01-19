package com.example.boxingreflextrainer;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {
    String filePath = null;
    int profileNumber = 0;
    ArrayList<String> profilesArray = new ArrayList<String>();
    String selectedProfile = null;

    FileHandler(Context context, String profile) {
        filePath = context.getApplicationContext().getFilesDir().getPath() + "/" + context.getString(R.string.profilesjson) + ".json";
        selectedProfile = profile;
        parseJson(selectedProfile);
    }

    protected void insertDefaultData(String path) {
        JSONObject profileData = new JSONObject();
        JSONObject profileObject = new JSONObject();
        FileWriter file;

        try {
            file = new FileWriter(path);
            profileData.put("training_time", "01:00");
            profileData.put("rest_time", "00:10");
            profileData.put("preparation_time", "00:20");
            profileData.put("end_preparation", "00:10");
            profileData.put("end_rest", "00:05");
            profileData.put("end_round", "00:10");
            profileData.put("round_number", "2");

            profileObject.put("profile_1", profileData);

            file.write(profileObject.toString(2));
            file.flush();

            profilesArray.add("profile_1");
            profileNumber = 1;
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    //Parse json data
    protected JSONObject parseJson(String profileName) {
        JSONObject jsonObject = null;
        FileReader fileReader;
        System.out.println("PATH" + filePath);
        File file = new File(filePath);
        selectedProfile = profileName;

        try {
            if(!file.exists()) {
                file.createNewFile();
                insertDefaultData(filePath);
            } else {

                fileReader = new FileReader(filePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();

                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();

                jsonObject = new JSONObject(stringBuilder.toString());

                profileNumber = jsonObject.length();
                profilesArray.add(String.format("profile_%d", profileNumber));

                JSONObject result = jsonObject.getJSONObject(String.format("profile_%d", profileNumber));
                return result;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateJson(SharedPreferences sharedPreferences) {
        JSONObject profileData = new JSONObject();
        JSONObject profileObject = new JSONObject();
        FileWriter file;

        try {
            for(int i=1; i <= profileNumber; i++) {
                profileObject.put(String.format("profile_%d", i), parseJson(String.format("profile_%d", i)));
            }

            file = new FileWriter(filePath);

            profileData.put("training_time", sharedPreferences.getString("trainingTime", "0:0"));
            profileData.put("rest_time", sharedPreferences.getString("restTime", "0:0"));
            profileData.put("preparation_time", sharedPreferences.getString("preparationTime", "0:0"));
            profileData.put("end_preparation", sharedPreferences.getString("preparationAlert", "0:0"));
            profileData.put("end_rest", sharedPreferences.getString("restAlent", "0:0"));
            profileData.put("end_round", sharedPreferences.getString("roundAlert", "0:0"));
            profileData.put("round_number", sharedPreferences.getString("roundsNumber", "0:0"));

            profileObject.put(String.format("profile_%d", profileNumber+1), profileData);
            profileNumber = profileObject.length();

            file.write(profileObject.toString(2));
            file.flush();

        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

}
