package com.mood.loginsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    SQLiteOpenHelper dbhelper;
    SQLiteDBHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //To hide AppBar for fullscreen.
        ActionBar ab = getSupportActionBar();
        ab.hide();

        //Referencing UserEmail, Password EditText and TextView for SignUp Now
        final EditText _txtemail = (EditText) findViewById(R.id.txtemail);
        final EditText _txtpass = (EditText) findViewById(R.id.txtpass);
        Button btnlogin = (Button) findViewById(R.id.btnsignin);
        TextView btnreg = (TextView) findViewById(R.id.btnreg);

        dbhelper = new SQLiteDBHelper(this);
        db = dbhelper.getReadableDatabase();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = _txtemail.getText().toString().trim();
                String pass = _txtpass.getText().toString().trim();

//                if (databaseHelper.checkUser(email, pass)) {

//                    if(cursor.getCount() > 0) {
                    // here we want to retrieve a sigle user
                    cursor = db.rawQuery("SELECT *FROM " + SQLiteDBHelper.TABLE_NAME + " WHERE " + SQLiteDBHelper.COLUMN_EMAIL + "=? AND " + SQLiteDBHelper.COLUMN_PASSWORD + "=?", new String[]{email, pass});
                    if (cursor != null) {
                        if (cursor.getCount() > 0) {

                            cursor.moveToFirst();
                            //Retrieving User FullName and Email after successfull login and passing to LoginSucessActivity
                            String _fname = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_USERNAME));
                            String _email = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_EMAIL));
                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, LoginSuccessActivity.class);
                            intent.putExtra("username", _fname);
                            intent.putExtra("email", _email);
                            startActivity(intent);

                            //Removing MainActivity[Login Screen] from the stack for preventing back button press.
                            finish();

                        } else {
                            // Toast to show success message that record is wrong
//                    Toast.makeText(getApplicationContext(),"User not registered",Toast.LENGTH_LONG).show();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Alert");
                            builder.setMessage("Username or Password is wrong.");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();

                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    }
                }
//            }
        });

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterAccountActivity.class);
                startActivity(intent);
            }
        });



    }
}
