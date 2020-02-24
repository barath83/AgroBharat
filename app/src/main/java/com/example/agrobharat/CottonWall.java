package com.example.agrobharat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CottonWall extends AppCompatActivity {

    String langcheck;

    RecyclerView blog_list_view;
    static  String postid;


    FirebaseFirestore firebaseFirestore;
    CollectionReference ref;
    private  BlogRecyclerAdapter blogRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotton_wall);
        langcheck = MainActivity.language;

        setupRecyclerView();


    }

    private void setupRecyclerView(){

        // blog_list = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        ref = firebaseFirestore.collection(NewsWall.crop+"_"+MainActivity.language);

        Query query = ref.orderBy("timeStamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<BlogPost> options = new FirestoreRecyclerOptions.Builder<BlogPost>()
                .setQuery(query,BlogPost.class)
                .build();

        blogRecyclerAdapter = new BlogRecyclerAdapter(options);

        blog_list_view = (RecyclerView)findViewById(R.id.recyclerView);
        blog_list_view.setHasFixedSize(true);
        blog_list_view.setLayoutManager(new LinearLayoutManager(this));
        blog_list_view.setAdapter(blogRecyclerAdapter);


        blogRecyclerAdapter.setOnItemClickListener(new BlogRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                postid = documentSnapshot.getId();
                Intent intent = new Intent(CottonWall.this,ForumActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        blogRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        blogRecyclerAdapter.stopListening();
    }
}
