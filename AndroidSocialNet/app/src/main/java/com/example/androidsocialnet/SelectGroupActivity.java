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

import com.example.androidsocialnet.Model.Group;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SelectGroupActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<Group, SelectGroupActivity.ViewHolder> adapter;
    View.OnClickListener mOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);

        recyclerView = findViewById(R.id.recycler_select_group);

        linearLayoutManager = new LinearLayoutManager(SelectGroupActivity.this);

        recyclerView.setLayoutManager(linearLayoutManager);

        linearLayoutManager = new LinearLayoutManager(SelectGroupActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        fetch();

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int itemPosition = recyclerView.getChildLayoutPosition(view);
                Group group = adapter.getItem(itemPosition);
                String groupString = group.getGroupName();

                Intent intent = getIntent();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts").push();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Map<String, Object> map = new HashMap<>();
                    map.put("storyTitle", intent.getStringExtra("storyTitle"));
                    map.put("storyBody", intent.getStringExtra("storyBody"));
                    map.put("storyGroup", groupString);
                    map.put("storyUser", user.getEmail());
                    databaseReference.setValue(map);


                DatabaseReference databaseReferenceGroup = FirebaseDatabase.getInstance().getReference().child(groupString).push();
                    Map<String, Object> mapGroup = new HashMap<>();
                    mapGroup.put("storyTitle", intent.getStringExtra("storyTitle"));
                    mapGroup.put("storyBody", intent.getStringExtra("storyBody"));
                    mapGroup.put("storyGroup", groupString);
                    mapGroup.put("storyUser", user.getEmail());
                    databaseReferenceGroup.setValue(mapGroup);

                    Intent intentHome = new Intent(SelectGroupActivity.this, MainActivity.class);
                    startActivity(intentHome);
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtGroupName;

        public ViewHolder(View itemView) {
            super(itemView);
            txtGroupName = itemView.findViewById(R.id.txt_group_name);
        }

        public void setTxtGroupName(String string) {
            txtGroupName.setText(string);
        }


    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("groups");

        FirebaseRecyclerOptions<Group> options =
                new FirebaseRecyclerOptions.Builder<Group>()
                        .setQuery(query, new SnapshotParser<Group>() {
                            @NonNull
                            @Override
                            public Group parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Group(
                                        snapshot.child("groupName").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Group, SelectGroupActivity.ViewHolder>(options) {

            @NotNull
            @Override
            public SelectGroupActivity.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.txt_groups, parent, false);

                view.setOnClickListener(mOnClickListener);

                return new SelectGroupActivity.ViewHolder(view);

            }


            @Override
            protected void onBindViewHolder(@NotNull SelectGroupActivity.ViewHolder holder, final int position, @NotNull Group Group) {
                holder.setTxtGroupName(Group.getGroupName());
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
