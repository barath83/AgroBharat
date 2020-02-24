package com.example.agrobharat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForumActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activitymain;
    FloatingActionButton fab;
    String  username;
    DatabaseReference databaseReference;
    String langcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        Firebase.setAndroidContext(this);
        username = LoginActivity.username_handle;
        TextInputLayout layoutUser;
        layoutUser = (TextInputLayout) findViewById(R.id.textinputlayout);
        langcheck = MainActivity.language;
        fab = (FloatingActionButton) findViewById(R.id.fab);

        switch (langcheck){

            case "tamil":
                layoutUser.setHint(getResources().getString(R.string.forum_tamil_hint));
                break;

            case "hindi":
                layoutUser.setHint(getResources().getString(R.string.forum_hindi_hint));
                break;

            case "english":
                layoutUser.setHint(getResources().getString(R.string.forum_english_hint));
                break;

        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().child(NewsWall.postid)
                        .push().setValue(new ChatMessage(input.getText().toString(),
                        username));
                input.setText("");

            }
        });
        displayChatMessage();


    }

    private void displayChatMessage(){

        ListView listofMessage = (ListView) findViewById(R.id.listofmsgs);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(NewsWall.postid);
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setLayout(R.layout.list_item)
                .setQuery(databaseReference, ChatMessage.class)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options) {

            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {


                TextView messageText,messageUser,messageTime;
                messageText = (TextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));

                System.out.print(model.getMessageText());
                System.out.print(model.getMessageUser());

            }
        };

        listofMessage.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
