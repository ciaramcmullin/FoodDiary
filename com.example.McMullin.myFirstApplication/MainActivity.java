/**
 *
 * Created by Ciara McMullin
 * A class used for first launching the app
 *
 */
package com.example.donghyukshin.myfirstapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enterApp(View view){
        Intent startNewActivity = new Intent(this, DisplayEnterActivity.class);
        startActivity(startNewActivity);

    }
}
