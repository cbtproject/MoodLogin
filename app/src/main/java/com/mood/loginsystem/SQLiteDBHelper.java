package com.mood.loginsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AZ on 7/11/2017.
 */

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "login.db"; //<----Name of the database
    private static final int DATABASE_VERSION = 1;          //<----version of the database

    public static final String TABLE_NAME = "users";        //<----Name of the table
    public static final String COLUMN_ID = "userid";        //<----Name of the column userid
    public static final String COLUMN_USERNAME = "username";//<----Name of the column username
    public static final String COLUMN_EMAIL = "email";      //<----Name of the column email
    public static final String COLUMN_PASSWORD = "password";//<----Name of the column password /
         ///You can add more if u want :)

        /***this is the query to create the table with it's columns***/
         private static final String CREATE_TABLE_QUERY =
                 "CREATE TABLE " + TABLE_NAME + " (" +
                         COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         COLUMN_USERNAME + " TEXT, "+
                         COLUMN_EMAIL + " TEXT, " +
                         COLUMN_PASSWORD + " TEXT " +
                         ")";

    //modified constructor
    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);     // <--this is to tell the app to create the DB once it is created..

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {  // <-- i= old version DB ,, i1 = newVersion of the DB
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);       // <-- tells the app to destroy DB and start over..to create new one
        onCreate(sqLiteDatabase);
    }

    public void addUser(User user) {
//        SQLiteOpenHelper openHelper = new SQLiteDBHelper(this);
        SQLiteDatabase  db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_USERNAME,user.getName());
        values.put(SQLiteDBHelper.COLUMN_EMAIL,user.getEmail());
        values.put(SQLiteDBHelper.COLUMN_PASSWORD,user.getPassword());
//        long id = db.insert(SQLiteDBHelper.TABLE_NAME,null,values);

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
//////////////////////////////////check user with email only////////////////////////////////////////////
    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
//////////////////////////////////Check user with email and password/////////////////////////////////////////////////////////
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

//////////////////////////////////////get all users////////////////////////////////////////////////////
    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ID,
                COLUMN_EMAIL,
                COLUMN_USERNAME,
                COLUMN_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USERNAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

//public String getUser(String email, String password){
//
//}


}






