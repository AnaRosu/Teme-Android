package com.example.laborator2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final EditText your_subject     = (EditText) findViewById(R.id.your_subject);
        final EditText your_message     = (EditText) findViewById(R.id.your_message);

        Button send_email = (Button) findViewById(R.id.post_message);
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject   = your_subject.getText().toString();
                String message   = your_message.getText().toString();

                if (TextUtils.isEmpty(subject)){
                    your_subject.setError("Enter Your Subject");
                    your_subject.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(message)){
                    your_message.setError("Enter Your Message");
                    your_message.requestFocus();
                    return;
                }

                //implicit intent
                Intent sendEmail  = new Intent(Intent.ACTION_SEND);

                sendEmail.setType("message/rfc822");
                sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ana_maria_rosu@yahoo.com"});
                sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                sendEmail.putExtra(android.content.Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(sendEmail, "Send mail..."));

            }
        });
    }

}
