package com.ayush_ca.ca_mod2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
static String teacher_contact;
    static String student_contact;
    Button calling_button;
    Boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Firebase.setAndroidContext(this);
        calling_button=(Button)findViewById(R.id.button);
        calling_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getno();
            }
        });

    }
    public void getno()
    {
        try
    {
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        final String mPhoneNumber = tMgr.getLine1Number();
        student_contact=mPhoneNumber;
        Log.e("Value", student_contact + "/");
        if(student_contact .equals("") || student_contact.equals(null))
        {
            alertBox();

        }
        else
        {
            if (isNetworkAvailable())
                new MyTask().execute();
            else {
                alertError();
            }
        }
        System.out.println("PHONE NO IS ++++" + student_contact);
    }
    catch(Exception e)
    {
        Log.e("Error","Error");
        alertBox();

        System.out.println("PHONE NO IS" + student_contact);
    }
        teacher_contact="8826947172";
    }

   public void alertBox()
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.custom_phone_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setTitle("Enter Phone Number");
        dialog.setCanceledOnTouchOutside(false);
        SharedPreferences sp=getSharedPreferences("MM",0);

        // set the custom dialog components - text, image and button
//				TextView text = (TextView) dialog.findViewById(R.id.text);
//				text.setText("Android custom dialog example!");
        final EditText editText_cap=(EditText)dialog.findViewById(R.id.editText1);
        editText_cap.setText(sp.getString("KK", ""));


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student_contact=editText_cap.getText().toString();
                SharedPreferences sp1=getSharedPreferences("MM",0);
                SharedPreferences.Editor ed1=sp1.edit();
                ed1.putString("KK", student_contact);
                ed1.commit();
                flag=true;
                if (isNetworkAvailable())
                    new MyTask().execute();
                else {
                    alertError();
                }
                dialog.dismiss();


            }
        });

        dialog.show();

    }
    private void alertError()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Internet Connection Not Available")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    ProgressDialog mProgressDialog;
    private class MyTask extends AsyncTask<Void, Void, String> {

        String title ="";
        String sessionId="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Sending Request");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            //	Toast.makeText(getApplicationContext(),capcha,3000).show();
            //	Toast.makeText()
//			mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            String num1=teacher_contact;
            String num2=student_contact;



            //Document doc;
            try
            {

                String urlLogin = "http://vapi.unicel.in/voiceapi?request=callpatch&uname=HEVOICE&pass=x5d2e5R7&cpdid=35&custno=91"+num1+"+&agentno=91"+num2;
                URL oracle = new URL(urlLogin);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return title;

        }

        @Override
        protected void onPostExecute(String result){//
            mProgressDialog.dismiss();
//Toast.makeText(getApplicationContext(),title_correct,3000).show();
            //calling_button.setVisibility(View.GONE);

        }
    }

}
