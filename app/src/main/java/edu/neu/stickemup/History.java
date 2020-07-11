package edu.neu.stickemup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class History extends AppCompatActivity {

    LinearLayout layout;
    TextView noUsersText;
    TextView totalCount;
    ArrayList<String> al = new ArrayList<>();
    int totalStickers = 0;
    ProgressDialog pd;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        noUsersText = findViewById(R.id.noUsersText);
        layout = (LinearLayout) findViewById(R.id.layout1);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        totalCount = (TextView) findViewById(R.id.totalCount);

        String url = "https://stick-em-up.firebaseio.com/history/"+UserDetails.username+".json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("ERRRRRRRRRRRR--------------------------------" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(History.this);
        rQueue.add(request);
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                String a = obj.get(key).toString();
                JSONObject newObj = new JSONObject(a);
                addMessageBox(newObj.get("sentTo").toString(),newObj.get("sticker").toString(),1);
                totalStickers++;
            }

        } catch (JSONException e) {
            System.out.println("CATCH");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        if(totalStickers <1){
            noUsersText.setVisibility(View.VISIBLE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            System.out.println(totalStickers);
            totalCount.setVisibility(View.VISIBLE);
            String a = "You sent " + String.valueOf(totalStickers) + " stickers";
            totalCount.setText(a);
        }
    }

    public void addMessageBox(String sender, String message, int type){
        TextView textView = new TextView(History.this);
        textView.setText("Sent to " + sender.substring(0, 1).toUpperCase() + sender.substring(1));
        textView.setTextColor(Color.WHITE);
        ImageView image = new ImageView(History.this);
        int w = 100;
        int h = 100;
        //Loading image using Picasso
        Picasso.get().load(message).into(image);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);
        image.setLayoutParams(lp);

        textView.setBackgroundResource(R.drawable.rounded_corner1);
        image.setBackgroundResource(R.drawable.rounded_corner1);

        layout.addView(textView);
        layout.addView(image);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}