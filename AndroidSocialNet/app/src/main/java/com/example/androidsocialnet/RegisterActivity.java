package com.example.androidsocialnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.androidsocialnet.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    AwesomeValidation awesomeValidation;

    private FirebaseAuth mAuth;

   private EditText editEmail;
   private EditText editPhone;
   private EditText editFirstName;
   private EditText editSecondName;
   private EditText editPassword;

    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister);

        editEmail = findViewById(R.id.editRegEmail);
        editPhone = findViewById(R.id.editRegPhoneNumber);
        editFirstName = findViewById(R.id.editRegFirstName);
        editSecondName = findViewById(R.id.editRegSecondName);
        editPassword = findViewById(R.id.editRegPassword);

        buttonRegister = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.editRegFirstName, RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        awesomeValidation.addValidation(this, R.id.editRegSecondName, RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        awesomeValidation.addValidation(this, R.id.editRegPhoneNumber, "[5-9]{1}[0-9]{10}$", R.string.invalid_mobile);

        awesomeValidation.addValidation(this, R.id.editRegEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.editRegPassword, ".{6,}", R.string.invalid_password);

        awesomeValidation.addValidation(this, R.id.editRegSecondPassword, R.id.editRegPassword, R.string.password_equals);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()){

                    final String email = editEmail.getText().toString();
                    final String phone = editPhone.getText().toString();
                    final String firstName = editFirstName.getText().toString();
                    final String secondName = editSecondName.getText().toString();
                    final String password = editPassword.getText().toString();

                    mAuth.createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).push();
                                        Map<String, Object> map = new HashMap<>();

                                        map.put("userEmail", email);
                                        map.put("userPhone", phone);
                                        map.put("userFirstName", firstName);
                                        map.put("userSecondName", secondName);
                                        map.put("userPassword", password);

                                        databaseReference.setValue(map);

                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("tag", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}