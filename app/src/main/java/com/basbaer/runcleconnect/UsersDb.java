package com.basbaer.runcleconnect;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class UsersDb {

    private static SQLiteDatabase usersDatabase;
    private Context context;


    public UsersDb(Context context) {

        this.context = context;

        //<SQLite Database>
        try {

            //creating the Database
            //mode: simplest one is MODE_PRIVATE
            //ErrorCatchingMethod/Factory: simplest one is null
            usersDatabase = context.openOrCreateDatabase("usersDb", MODE_PRIVATE, null);

            //creates the table with the coloums and their corresponding data types
            usersDatabase.execSQL("CREATE TABLE IF NOT EXISTS usersInfo (id VARCHAR, username VARCHAR, follows VARCHAR)");


        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public static void insertUserIntoDb(String id, String username) {


        String sqlCode = "INSERT INTO usersInfo (id, username, follows) VALUES";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("followingAL", new JSONArray(new ArrayList<String>()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String arrayListString = jsonObject.toString();


        //adding the id and the Username to the sql code
        sqlCode += " ('" + id + "', '" + username + "', '" + arrayListString+ "')";

        usersDatabase.execSQL(sqlCode);



    }

    public static String getUsernameFromId(String id) {

        String sql = "SELECT * FROM usersInfo WHERE id = '" + id + "' LIMIT 1";

        Cursor c = usersDatabase.rawQuery(sql, null);

        int usernameIndex = c.getColumnIndex("username");

        c.moveToFirst();

        Log.i("Username", c.getString(usernameIndex));

        return c.getString(usernameIndex);

    }

    public static void updateFollowsAl(){

        String sqlCode = "UPDATE usersInfo SET follows = ";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("followingAL", new JSONArray(MainActivity.userFollows));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String arrayListString = jsonObject.toString();

        //getting the id of the current user
        String id = ParseUser.getCurrentUser().getObjectId();

        //adding the id and the Username to the sql code
        sqlCode += " '"
                + arrayListString
                + "' WHERE id = '"
                + id
                + "'";

        usersDatabase.execSQL(sqlCode);


    }





}
