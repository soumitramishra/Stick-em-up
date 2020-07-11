package edu.neu.stickemup;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Users extends AppCompatActivity {

    private static final String CHANNEL_ID = "MY CHANNEL";
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersList = (ListView) findViewById(R.id.usersList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);

        pd = new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://stick-em-up.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                Intent i = new Intent(Users.this, Chat.class);
                i.putExtra("chatWith", al.get(position));
                startActivity(i);
            }
        });
    }

    public void openHistory(View v) {
        Intent i = new Intent(Users.this, History.class);
        startActivity(i);
    }

    public void doOnSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();

                if (!key.equals(UserDetails.username)) {
                    al.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (totalUsers <= 1) {
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        } else {
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }
        pd.dismiss();

        for (int i = 0; i < al.size(); i++) {
            final String chatName = al.get(i);
            Firebase.setAndroidContext(this);
            Firebase chatHistory = new Firebase("https://stick-em-up.firebaseio.com/messages/");
            chatHistory.child(UserDetails.username + "_" + chatName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Iterator it = snapshot.getChildren().iterator();
                        Object val = it.next();
                        while (it.hasNext()) {
                            val = it.next();
                        }
                        Map<String, String> a = (Map) ((DataSnapshot) val).getValue();
                        if (a.get("user").equals(chatName)) {
                            new sendNotification(getApplicationContext())
                                    .execute(a.get("message"), a.get("message"), a.get("user"));
                        }
                    } else {
                        System.out.println("ENTER ELSE");
                    }
                }

                @Override
                public void onCancelled(FirebaseError arg0) {
                }
            });
        }

    }

    private void notifyUser(String sender) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, Users.class);
        intent.putExtra("chatWith", sender);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(Users.this, sender.hashCode(), intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Users.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(bmp)
                .setContentTitle("New Sticker From: " + sender)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        mNotificationManager.notify(sender.hashCode(), builder.build());
    }

    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context context;
        String message;
        String userName;

        public sendNotification(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            userName = params[2];
            message = params[0] + params[1];
            try {
                URL url = new URL(message);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                return bmp;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            try {
                notifyUser(userName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}