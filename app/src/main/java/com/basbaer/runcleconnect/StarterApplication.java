package com.basbaer.runcleconnect;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class StarterApplication extends Application{

    @Override
    public void onCreate(){
        super.onCreate();

        //Enable Local Data store.
        Parse.enableLocalDatastore(this);

        //Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myappID")
                .clientKey("Ca8aV9doEXBP")
                .server("http://18.223.102.117/parse/")
                .build()
        );

        /*
        ParseObject testobject = new ParseObject("ExampleClass");
        testobject.put("myNumber","123");
        testobject.put("myString","kk_coding");

        testobject.saveInBackground(new SaveCallback(){
            @Override
            public void done(ParseException ex){
                if(ex == null){
                    Log.i("ParseResult","Successful!");
                }else{
                    Log.i("ParseResult","Failed"+ex.toString());
                }
            }
        });

         */


        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL,true);

    }
}
