package com.example.androidsocialnet.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsocialnet.Interface.FetchInterface;
import com.example.androidsocialnet.Model.User;
import com.example.androidsocialnet.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class SlideshowFragment extends Fragment implements FetchInterface {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseUser fireBaseUser;
    FirebaseRecyclerAdapter<User, SlideshowFragment.ViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        recyclerView = root.findViewById(R.id.recycler_profile);

        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        fetch();


        return root;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFirstName;
        public TextView txtSecondName;
        public TextView txtPhone;
        public TextView txtEmail;
        public ImageView imgProfile;


        public ViewHolder(View itemView) {
            super(itemView);
            txtFirstName = itemView.findViewById(R.id.textProfileFirstName);
            txtSecondName = itemView.findViewById(R.id.textProfileSecondName);
            txtPhone = itemView.findViewById(R.id.textProfilePhone);
            txtEmail = itemView.findViewById(R.id.textProfileEmail);
            imgProfile = itemView.findViewById(R.id.imageProfilePictureBig);
        }

        public void setProfileFirstName(String string) {
            txtFirstName.setText(string);
        }


        public void setProfileSecondName(String string) {
            txtSecondName.setText(string);
        }

        public void setProfilePhone(String string) {
            txtPhone.setText(string);
        }

        public void setProfileEmail(String string) {
            txtEmail.setText(string);
        }

        public void setProfileImage(String url) {
            Picasso.with(getContext()).load(url).into(imgProfile);
        }
    }
    @Override
    public void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(fireBaseUser.getUid());

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, new SnapshotParser<User>() {
                            @NonNull
                            @Override
                            public User parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new User(
                                        snapshot.child("userFirstName").getValue().toString(),
                                        snapshot.child("userSecondName").getValue().toString(),
                                        snapshot.child("userPhone").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<User, SlideshowFragment.ViewHolder>(options) {

            @Override
            public SlideshowFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.txt_profile, parent, false);

                return new SlideshowFragment.ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(SlideshowFragment.ViewHolder holder, final int position, User user) {
                holder.setProfileFirstName(user.getUserFirstName());
                holder.setProfileSecondName(user.getUserSecondName());
                holder.setProfilePhone(user.getPhoneNumber());
                holder.setProfileEmail(fireBaseUser.getEmail());
                holder.setProfileImage("https://firebasestorage.googleapis.com/v0/b/socialnet-6920f.appspot.com/o/userphoto.jpg?alt=media&token=e9ac2236-d579-45ba-bd2e-022455b47f53");
            }

        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void fillDatabase() {

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}