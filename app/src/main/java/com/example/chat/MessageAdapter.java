package com.example.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Context context;
    private List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        }

        Message currentMessage = messages.get(position);

        TextView senderTextView = listItemView.findViewById(R.id.senderTextView);
        TextView contentTextView = listItemView.findViewById(R.id.contentTextView);

        senderTextView.setText(currentMessage.getSenderName());
        contentTextView.setText(currentMessage.getContent());

        return listItemView;
    }
}
