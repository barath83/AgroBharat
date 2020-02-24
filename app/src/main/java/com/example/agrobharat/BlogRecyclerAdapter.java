package com.example.agrobharat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BlogRecyclerAdapter extends FirestoreRecyclerAdapter<BlogPost,BlogRecyclerAdapter.BlogHolder>{

    private OnItemClickListener listener;

    public BlogRecyclerAdapter(@NonNull FirestoreRecyclerOptions<BlogPost> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BlogHolder holder, int position, @NonNull BlogPost model) {

        holder.usernameView.setText(model.getUsername());
        Picasso.get().load(model.getImageURL()).into(holder.postImageView);
        holder.descView.setText(model.getDescription());

    }

    @NonNull
    @Override
    public BlogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,
                parent,false);


        return new BlogHolder(v);
    }

    class BlogHolder extends RecyclerView.ViewHolder{

        private TextView descView;
        private ImageView postImageView;
        private TextView usernameView;


        public BlogHolder(@NonNull View itemView) {
            super(itemView);

            descView = itemView.findViewById(R.id.post_desc);
            postImageView = itemView.findViewById(R.id.post_image);
            usernameView = itemView.findViewById(R.id.post_user);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });


        }
    }

    public interface OnItemClickListener {

        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


}