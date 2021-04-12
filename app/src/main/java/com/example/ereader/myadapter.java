package com.example.ereader;

import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder>
{
    DatabaseReference likereference;
    Boolean testclick;

    public myadapter(@NonNull FirebaseRecyclerOptions<model> options, DatabaseReference likereference, Boolean testclick) {
        super(options);
        this.likereference=likereference;
        this.testclick=testclick;
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final model model) {

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String userID=firebaseUser.getUid();
        String postkey=getRef(position).getKey();

        holder.getlikebuttonstatus(postkey, userID);

        holder.likebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testclick=true;
                likereference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(testclick==true){
                            if(snapshot.child(postkey).hasChild(userID)){
                                likereference.child(postkey).removeValue();
                                testclick=false;
                            }
                            else{
                                likereference.child(postkey).child(userID).setValue(true);
                                testclick=false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.header.setText(model.getFilename());

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.img1.getContext(),viewpdf.class);
                intent.putExtra("filename",model.getFilename());
                intent.putExtra("fileurl",model.getFileurl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.img1.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singelrowdesign,parent,false);
        return  new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        TextView header;
        ImageView likebook;
        TextView textlike;
        DatabaseReference likereference;

        public myviewholder(@NonNull View itemView)
        {
            super(itemView);

            img1=itemView.findViewById(R.id.img1);
            header=itemView.findViewById(R.id.header);

            likebook=itemView.findViewById(R.id.like_btn);

            textlike=itemView.findViewById(R.id.textlike);
        }

        public void getlikebuttonstatus(final String postkey, final String userID){
            likereference= FirebaseDatabase.getInstance().getReference("likes");
            likereference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(postkey).hasChild(userID)){
                        int likecount=(int)snapshot.child(postkey).getChildrenCount();
                        textlike.setText(likecount+" likes");
                        likebook.setImageResource(R.drawable.ic_baseline_favorite_24);
                    }
                    else{
                        int likecount=(int)snapshot.child(postkey).getChildrenCount();
                        textlike.setText(likecount+" likes");
                        likebook.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}