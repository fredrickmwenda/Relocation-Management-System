package com.example.lucy.first;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;






public class MainActivity extends AppCompatActivity {
   private Button mDriver,mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         mDriver =(Button)findViewById(R.id.driver);
         mClient =(Button)findViewById(R.id.client);

         mDriver.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(MainActivity.this,DriverLoginActivity.class);
                 startActivity(intent);
                 finish();
                 return;
             }
         });
        mClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ClientLoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

    }
}


