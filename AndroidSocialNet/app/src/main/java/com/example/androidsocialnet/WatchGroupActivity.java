package com.example.androidsocialnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidsocialnet.Model.Story;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class WatchGroupActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Story, WatchGroupActivity.ViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_group);

        recyclerView = findViewById(R.id.recycler_watch_group);

        linearLayoutManager = new LinearLayoutManager(WatchGroupActivity.this);

        recyclerView.setLayoutManager(linearLayoutManager);

        linearLayoutManager = new LinearLayoutManager(WatchGroupActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        fetch();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtDesc;
        public TextView txtGroup;
        public TextView txtUser;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_story_title);
            txtDesc = itemView.findViewById(R.id.txt_story_body);
            txtGroup = itemView.findViewById(R.id.txt_story_group);
            txtUser = itemView.findViewById(R.id.txt_story_user);
        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }


        public void setTxtDesc(String string) {
            txtDesc.setText(string);
        }

        public void setTxtGroup(String string) {
            txtGroup.setText(string);
        }

        public void setTxtUser(String string) {
            txtUser.setText(string);
        }
    }

    private void fetch() {

        Intent intent = getIntent();
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(intent.getStringExtra("groupName"));

        FirebaseRecyclerOptions<Story> options =
                new FirebaseRecyclerOptions.Builder<Story>()
                        .setQuery(query, new SnapshotParser<Story>() {
                            @NonNull
                            @Override
                            public Story parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Story(
                                        snapshot.child("storyTitle").getValue().toString(),
                                        snapshot.child("storyBody").getValue().toString(),
                                        snapshot.child("storyGroup").getValue().toString(),
                                        snapshot.child("storyUser").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Story, WatchGroupActivity.ViewHolder>(options) {

            @Override
            public WatchGroupActivity.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.txt_feed, parent, false);

                return new WatchGroupActivity.ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(WatchGroupActivity.ViewHolder holder, final int position, Story story) {
                holder.setTxtTitle(story.getStoryTitle());
                holder.setTxtDesc(story.getStoryBody());
                holder.setTxtGroup(story.getStoryGroup());
                holder.setTxtUser(story.getStoryUser());
            }

        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
