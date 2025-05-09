// ChatActivity.java
package com.example.myapplication;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import android.Manifest;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private String receiverId;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ImageButton btnSendLocation;
    private ChatAdapter chatAdapter;
    private List<Message> messageList = new ArrayList<>();
    FusedLocationProviderClient fusedLocationClient ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btnSendLocation = findViewById(R.id.btnSendLocation);

        receiverId = getIntent().getStringExtra("receiverId");

        recyclerView = findViewById(R.id.recyclerViewMessages);
        messageEditText = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.sendButton);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(this,messageList);
        recyclerView.setAdapter(chatAdapter);

        loadMessages();


        sendButton.setOnClickListener(v -> {
            String msg = messageEditText.getText().toString().trim();
            if (!msg.isEmpty()) {
                sendMessage(msg);
                messageEditText.setText("");
            }
        });
        btnSendLocation.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                return;
            }

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    sendLocationMessage(lat, lng);
                } else {
                    Toast.makeText(this, "Couldn't get location", Toast.LENGTH_SHORT).show();
     }
});
        });
    }
    private void sendLocationMessage(double lat, double lng) {
        String senderId = mAuth.getCurrentUser().getUid();
        String chatId = getChatId(senderId, receiverId);

        Map<String, Object> message = new HashMap<>();
        message.put("senderId", senderId);
        message.put("receiverId", receiverId);
        message.put("type", "location");
        message.put("lat", lat);
        message.put("lng", lng);
        message.put("timestamp", FieldValue.serverTimestamp());

        db.collection("chats").document(chatId)
                .collection("messages")
                .add(message)
                .addOnSuccessListener(doc -> Log.d("Chat", "Location sent"));
    }

    private void loadMessages() {
        String senderId = mAuth.getCurrentUser().getUid();
        String chatId = getChatId(senderId, receiverId);

        db.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp") // Make sure you're using server timestamps
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) {
                        return;
                    }

                    messageList.clear();
                    for (var doc : value.getDocuments()) {
                        Message message = doc.toObject(Message.class);
                        if (message != null) {
                            messageList.add(message);
                        }
                    }

                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messageList.size() - 1);
                });
    }


    private void sendMessage(String text) {
        String senderId = mAuth.getCurrentUser().getUid();
        String chatId = getChatId(senderId, receiverId);

        Message message = new Message(senderId, receiverId, text,null);

        db.collection("chats")
                .document(chatId)
                .collection("messages")
                .add(message)
                .addOnSuccessListener(doc -> {
                    // No need to add to messageList manually here — listener handles it
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
                });
    }
    private String getChatId(String user1, String user2) {
        return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
    }


}
