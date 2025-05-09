package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<Message> messageList;
    private String currentUserId;
    private Context context;

    public ChatAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.getSenderId().equals(currentUserId) ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder instanceof SentMessageHolder) {
            ((SentMessageHolder) holder).bind(message, context);
        } else {
            ((ReceivedMessageHolder) holder).bind(message, context);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessageSent;

        SentMessageHolder(View itemView) {
            super(itemView);
            tvMessageSent = itemView.findViewById(R.id.tvMessageSent);
        }

        void bind(Message message, Context context) {
            if ("location".equals(message.getType())) {
                tvMessageSent.setText("📍 Location");
                itemView.setOnClickListener(v -> {
                    String mapsUrl = "https://maps.google.com/?q=" + message.getLat() + "," + message.getLng();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl));
                    context.startActivity(intent);
                });
            } else {
                tvMessageSent.setText(message.getMessage());
                itemView.setOnClickListener(null);
            }
        }
    }

    static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessageReceived;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            tvMessageReceived = itemView.findViewById(R.id.tvMessageReceived);
        }

        void bind(Message message, Context context) {
            if ("location".equals(message.getType())) {
                tvMessageReceived.setText("📍 Location");
                itemView.setOnClickListener(v -> {
                    String mapsUrl = "https://maps.google.com/?q=" + message.getLat() + "," + message.getLng();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl));
                    context.startActivity(intent);
                });
            } else {
                tvMessageReceived.setText(message.getMessage());
                itemView.setOnClickListener(null);
            }
        }
    }
}
