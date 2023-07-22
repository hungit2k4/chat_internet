package com.example.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton , btnDelete;
    private ListView messageListView;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo DatabaseReference để tham chiếu tới "messages"
        databaseReference = FirebaseDatabase.getInstance().getReference().child("messages");

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        btnDelete = findViewById(R.id.btnDelete);
        messageListView = findViewById(R.id.messageListView);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        messageListView.setAdapter(messageAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    sendMessage(messageText);
                    messageEditText.setText("");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa toàn bộ dữ liệu trên Firebase Realtime Database
                databaseReference.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Xóa thành công, cập nhật lại danh sách tin nhắn
                                messageList.clear();
                                messageAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Xóa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xóa thất bại, thông báo lỗi
                                Toast.makeText(MainActivity.this, "Xóa dữ liệu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Lắng nghe sự kiện thay đổi dữ liệu từ Firebase Realtime Database
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messageList.add(message);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                // Không cần xử lý
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Không cần xử lý
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                // Không cần xử lý
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Không cần xử lý
            }
        });
    }

    private void sendMessage(String messageText) {
        // Tạo một ID ngẫu nhiên cho tin nhắn
        String messageId = databaseReference.push().getKey();
        // Lấy tên người gửi (có thể lấy từ Firebase Authentication nếu có)
        String senderName = "User"; // Đây chỉ là tên mặc định, bạn có thể thay đổi tùy ý.
        // Tạo đối tượng tin nhắn
        Message message = new Message(messageText, senderName);
        // Ghi tin nhắn vào Firebase Realtime Database
        databaseReference.child(messageId).setValue(message);
    }
}
