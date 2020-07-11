package edu.neu.stickemup;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {

    private static final String CHANNEL_ID = "MY CHANNEL";
    LinearLayout layout;
    ImageView sendButton;
    String messageArea = "";
    ScrollView scrollView;
    Firebase reference1, reference2, history;
    ImageView selectedImage;
    TextView chatWithName;
    String chatWith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        chatWith = getIntent().getStringExtra("chatWith");
        layout = (LinearLayout) findViewById(R.id.layout1);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        selectedImage = (ImageView) findViewById(R.id.selectedSticker);
        GridView gridview = (GridView) findViewById(R.id.stickers);
        gridview.setAdapter(new ImageAdapter(this));
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        chatWithName = findViewById(R.id.chatName);
        chatWithName.setText(chatWith.substring(0, 1).toUpperCase() + chatWith.substring(1));

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://stick-em-up.firebaseio.com/messages/" + UserDetails.username + "_" + chatWith);
        reference2 = new Firebase("https://stick-em-up.firebaseio.com/messages/" + chatWith + "_" + UserDetails.username);
        history = new Firebase("https://stick-em-up.firebaseio.com/history/" + UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageArea != "") {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageArea);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    selectedImage.setVisibility(View.GONE);

                    Map<String, String> historyMap = new HashMap<String, String>();
                    historyMap.put("sentTo", chatWith);
                    historyMap.put("sticker", messageArea);
                    history.push().setValue(historyMap);
                    messageArea = "";
                } else {
                    Toast.makeText(Chat.this, "Select an emoji to send",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                ImageAdapter imageAdapter = new ImageAdapter(Chat.this);

                messageArea = imageAdapter.mThumbIds.get(position);
                //Loading image using Picasso
                Picasso.get().load(messageArea).into(selectedImage);
                selectedImage.setVisibility(View.VISIBLE);
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                if (userName.equals(UserDetails.username)) {
                    addMessageBox("You:\n", message, 1);
                } else {
                    addMessageBox(chatWith.substring(0, 1).toUpperCase() + chatWith.substring(1) + ":\n", message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String sender, String message, int type) {
        TextView textView = new TextView(Chat.this);
        textView.setText(sender);

        ImageView image = new ImageView(Chat.this);
        int w = 100;
        int h = 100;
        //Loading image using Picasso
        Picasso.get().load(message).into(image);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        textView.setLayoutParams(lp);
        image.setLayoutParams(lp);

        if (type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
            textView.setTextColor(Color.BLACK);
            image.setBackgroundResource(R.drawable.rounded_corner1);
            lp.setMargins(0, 0, 60, 15);
        } else {
            textView.setBackgroundResource(R.drawable.rounded_corner2);
            textView.setTextColor(Color.WHITE);
            image.setBackgroundResource(R.drawable.rounded_corner2);
            lp.setMargins(60, 0, 0, 15);

        }

        layout.addView(textView);
        layout.addView(image);
        scrollView.fullScroll(View.FOCUS_DOWN);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 1000);
    }

}