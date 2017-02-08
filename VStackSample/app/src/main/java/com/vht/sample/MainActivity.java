package com.vht.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText edtUserId, edtName;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUserId = (EditText) findViewById(R.id.edtUserId);
        edtName = (EditText) findViewById(R.id.edtUserName);
        btnNext = (Button) findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = edtUserId.getText().toString();
                String userName = edtName.getText().toString();
                if (!userId.equals("")) {
                    Config.my_name = userName;
                    Config.my_vstack_userid =userId;
                    startActivity(new Intent(MainActivity.this, SampleActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Input userid ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
