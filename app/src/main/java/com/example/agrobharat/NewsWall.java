package com.example.agrobharat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewsWall extends AppCompatActivity  {

    static  String crop;
    String langcheck;

    RecyclerView blog_list_view;
    static  String postid;


    FirebaseFirestore firebaseFirestore;
    CollectionReference ref;
    private  BlogRecyclerAdapter blogRecyclerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

      langcheck = MainActivity.language;

      switch (langcheck){

          case "tamil":
              menuInflater.inflate(R.menu.main_menu_tamil,menu);
              break;

          case "hindi":
              menuInflater.inflate(R.menu.main_menu_hindi,menu);
              break;

          case "english":
              menuInflater.inflate(R.menu.main_menu_english,menu);
              break;

      }



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         switch(item.getItemId()){

             case R.id.newpost:
                 Intent intent = new Intent(NewsWall.this,NewPost.class);
                 startActivity(intent);
                 return  true;

             case R.id.cotton:
                 crop = "cotton";
                 Intent intent1 = new Intent(NewsWall.this,CottonWall.class);
                 startActivity(intent1);
                 return  true;

             case R.id.rice:
                 crop = "rice";
                 Intent intent2 = new Intent(NewsWall.this,RiceWall.class);
                 startActivity(intent2);
                 return  true;


              default:
                  return false;
         }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_wall);

        setupRecyclerView();

    }

    private void setupRecyclerView(){

       // blog_list = new ArrayList<>();
        System.out.print("Hello World");
        firebaseFirestore = FirebaseFirestore.getInstance();
        ref = firebaseFirestore.collection(MainActivity.language);

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
              Intent intent = new Intent(NewsWall.this,ForumActivity.class);
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
