package edu.neu.stickemup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    //TextView register;
    EditText username;
    Button loginButton;
    String user;
    String clientRegToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(SavedSharedPreferences.getUserName(Login.this).length() != 0)
        {

            UserDetails.username = user;
            startActivity(new Intent(Login.this, Users.class));
        }
        else {
            //register = (TextView) findViewById(R.id.register);
            username = (EditText) findViewById(R.id.username);
            //password = (EditText) findViewById(R.id.password);
            loginButton = (Button) findViewById(R.id.loginButton);

/*        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });*/

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user = username.getText().toString();
                    //pass = password.getText().toString();

                    if(user.equals("")){
                        username.setError("Can't be blank");
                    }
/*                else if(pass.equals("")){
                    password.setError("Can't be blank");
                }*/
                    else{
                        String url = "https://stick-em-up.firebaseio.com/users.json";
                        final ProgressDialog pd = new ProgressDialog(Login.this);
                        pd.setMessage("Loading...");
                        pd.show();

                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                            @Override
                            public void onResponse(String s) {
                                Firebase.setAndroidContext(Login.this);
                                Firebase reference = new Firebase("https://stick-em-up.firebaseio.com/users");
                                //Firebase sessionReference = new Firebase("https://stick-em-up.firebaseio.com/sessions");
/*                            if(s.equals("null")){
                                Toast.makeText(Login.this, "User not found", Toast.LENGTH_LONG).show();
                            }
                            else{*/
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(Login.this, "New user created successfully", Toast.LENGTH_LONG).show();
                                        UserDetails.username = user;
                                        reference.child(user).setValue(user);
                                    }
/*                                    else if(obj.getJSONObject(user).getString("password").equals(pass)){
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        startActivity(new Intent(Login.this, Users.class));
                                    }*/
                                    else {
                                        //Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_LONG).show();
                                        UserDetails.username = user;
                                        //UserDetails.password = pass;
                                    }

                                    // Creating/Updating Token
                                    //sessionReference.child(clientRegToken).setValue(user);
                                    SavedSharedPreferences.setUserName(Login.this, user);
                                    startActivity(new Intent(Login.this, Users.class));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //}

                                pd.dismiss();
                            }
                        },new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                System.out.println("" + volleyError);
                                pd.dismiss();
                            }
                        });

                        RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                        rQueue.add(request);
                    }

                }
            });
        }

        /*
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Login.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                final String token = instanceIdResult.getToken();
                clientRegToken = token;
                System.out.println("TOKEN :: " + token);
                Toast.makeText(Login.this, token, Toast.LENGTH_SHORT).show();
                String url = "https://stick-em-up.firebaseio.com/sessions.json";
                final ProgressDialog pd = new ProgressDialog(Login.this);
                pd.setMessage("Loading...");
                pd.show();
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        Firebase.setAndroidContext(Login.this);

                        try {
                            JSONObject obj = new JSONObject(s);

                            if(obj.has(token)){
                                String tmpUser = obj.getString(token);
                                System.out.println("Token present for user :: " + tmpUser);
                                UserDetails.username = user;
                                startActivity(new Intent(Login.this, Users.class));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pd.dismiss();
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError);
                        pd.dismiss();
                    }
                });
                RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                rQueue.add(request);
            }
        });
        */

    }
}