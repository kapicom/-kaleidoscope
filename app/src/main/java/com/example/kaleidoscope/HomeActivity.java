package com.example.kaleidoscope;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageButton imageButton=(ImageButton)findViewById(R.id.tap);
        imageButton.setOnClickListener(click);
    }
    View.OnClickListener click =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);
        }
    };
}
