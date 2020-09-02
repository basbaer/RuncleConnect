package com.basbaer.runcleconnect;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
            usersDatabase.execSQL("CREATE TABLE IF NOT EXISTS usersInfo (id VARCHAR, username VARCHAR, following INT(1))");


        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public static void insertUserIntoDb(String id, String username, boolean following) {


        String sqlCode = "INSERT INTO usersInfo (id, username, following) VALUES";

        int followingInt;
        if(following) { followingInt = 1; } else{followingInt = 0;}


        //adding the id and the Username to the sql code
        sqlCode += " ('" + id + "', '" + username + "', " + followingInt + ")";

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

    public static boolean getFollowingstatus(String id){

        String sql = "SELECT * FROM usersInfo WHERE id = '" + id + "' LIMIT 1";

        Cursor c = usersDatabase.rawQuery(sql, null);

        int followingIndex = c.getColumnIndex("following");

        c.moveToFirst();


        if(c.getInt(followingIndex) == 1){
            return true;
        }else{
            return false;
        }

    }

    public static void changeFollowingStatus(String id){

        String sql = "SELECT * FROM usersInfo WHERE id = '" + id + "' LIMIT 1";

        Cursor c = usersDatabase.rawQuery(sql, null);

        int followingIndex = c.getColumnIndex("following");

        c.moveToFirst();

        if(c.getInt(followingIndex) == 1){

            String sqlUpdate = "UPDATE usersInfo SET following = 0 WHERE id = '"
                    + id
                    +"'";

            usersDatabase.execSQL(sqlUpdate);

        }else{
            String sqlUpdate = "UPDATE usersInfo SET following = 1 WHERE id = '"
                    + id
                    + "'";
            usersDatabase.execSQL(sqlUpdate);
        }

    }





}
