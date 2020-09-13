package com.example.androidsocialnet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateStoryActivity extends AppCompatActivity {

    AwesomeValidation awesomeValidation;
    EditText editTitle;
    EditText editBody;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        editTitle = findViewById(R.id.edit_title);
        editBody = findViewById(R.id.edit_body);
        fab = findViewById(R.id.fabCreateStory);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.edit_title, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.edit_body, RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()){

                    Intent intent = new Intent(CreateStoryActivity.this, SelectGroupActivity.class);

                    intent.putExtra("storyTitle", editTitle.getText().toString());
                    intent.putExtra("storyBody", editBody.getText().toString());

                    startActivity(intent);

                }
            }
        });



    }
}
