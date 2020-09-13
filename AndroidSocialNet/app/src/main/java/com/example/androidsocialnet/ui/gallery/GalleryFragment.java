package com.example.androidsocialnet.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsocialnet.Interface.FetchInterface;
import com.example.androidsocialnet.Model.Group;
import com.example.androidsocialnet.R;
import com.example.androidsocialnet.WatchGroupActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment implements FetchInterface {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    View.OnClickListener mOnClickListener;

    FirebaseRecyclerAdapter<Group, GalleryFragment.ViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView = root.findViewById(R.id.recycler_groups);

        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        //Adding data void

        linearLayoutManager = new LinearLayoutManager(getContext());
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

                Intent intent = new Intent(getContext(), WatchGroupActivity.class);
                intent.putExtra("groupName", groupString);
                startActivity(intent);
            }
        };

        return root;
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

    public void fetch() {
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

        adapter = new FirebaseRecyclerAdapter<Group, GalleryFragment.ViewHolder>(options) {

            @Override
            public GalleryFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.txt_groups, parent, false);

                    view.setOnClickListener(mOnClickListener);

                return new GalleryFragment.ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(GalleryFragment.ViewHolder holder, final int position, Group Group) {
                holder.setTxtGroupName(Group.getGroupName());
            }

        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void fillDatabase() {
        List<String> list = new ArrayList();
        list.add("IT industry");
        list.add("Author stories");
        list.add("Aviation");
        list.add("Cars");
        list.add("Management");
        list.add("HR");
        list.add("Cosmetics");
        list.add("Pharmaceuticals");
        list.add("Electronics");
        list.add("Art");
        list.add("Clothing");
        list.add("Surviving");
        list.add("Embroidery");
        list.add("Manufacturing");
        list.add("Gaming");
        list.add("Stupidity");
        list.add("Life stories");
        list.add("Fighting");
        list.add("Other");

        for (int x = 0; x < list.size(); x++){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("groups").push();
            Map<String, Object> map = new HashMap<>();
            map.put("groupName", list.get(x));
            databaseReference.setValue(map);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }



}