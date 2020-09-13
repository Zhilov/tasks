package com.example.androidsocialnet.ui.home;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsocialnet.Interface.FetchInterface;
import com.example.androidsocialnet.Model.Story;
import com.example.androidsocialnet.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements FetchInterface {

    RecyclerView recyclerView;
    TextView textLoadingFeed;
    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Story, ViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_feed);
        textLoadingFeed = root.findViewById(R.id.textLoadingFeed);

        textLoadingFeed.setVisibility(View.VISIBLE);

        AnimationRotating(textLoadingFeed, "rotationY");

        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        fetch();

        return root;
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
            txtUser= itemView.findViewById(R.id.txt_story_user);
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

    @Override
    public void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts");

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

        adapter = new FirebaseRecyclerAdapter<Story, ViewHolder>(options) {

            @Override
            public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.txt_feed, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, Story story) {
                holder.setTxtTitle(story.getStoryTitle());
                holder.setTxtDesc(story.getStoryBody());
                holder.setTxtGroup(story.getStoryGroup());
                holder.setTxtUser(story.getStoryUser());
                textLoadingFeed.setVisibility(View.GONE);
            }

        };

        recyclerView.setAdapter(adapter);
    }

    private void AnimationRotating (Object target, String anim){
        ObjectAnimator animation = ObjectAnimator.ofFloat(target, anim, 0.0f, 360f);
        animation.setDuration(2000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.setRepeatMode(ValueAnimator.RESTART);
        animation.start();
    }

    public void fillDatabase(){
        //Fill database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts").push();
        Map<String, Object> map = new HashMap<>();
        map.put("storyTitle", "Title");
        map.put("storyBody", "Body");
        map.put("storyGroup", "Group");
        map.put("storyUser", "User");

        databaseReference.setValue(map);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

}