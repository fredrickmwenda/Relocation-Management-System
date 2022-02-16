package com.example.lucy.first;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
//import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.lucy.first.Common.CommonClient;
import com.example.lucy.first.Model.Client;
import com.example.lucy.first.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
//import android.app.AlertDialog;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class ClientLoginActivity extends AppCompatActivity {
    Button btnSignIn,btnRegister;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference clients;
    RelativeLayout majorLayout;

    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        //  super.onCreate();
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        setContentView(R.layout.activity_client_login);
        //initializing firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        clients = db.getReference(CommonClient.user_client_tbl);



        //initializing view
        btnRegister=(Button)findViewById(R.id.btnRegister);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        majorLayout = (RelativeLayout) findViewById(R.id.majorLayout);
        //Events
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });




    }

    private void showRegisterDialog() {

        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please register with email and relevant phone");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout =inflater.inflate(R.layout.register,null);

        final MaterialEditText editName =register_layout.findViewById(R.id.editName);
        final MaterialEditText editEmail =register_layout.findViewById(R.id.editEmail);
        final MaterialEditText editPassword =register_layout.findViewById(R.id.editPassword);
        final MaterialEditText editPhone =register_layout.findViewById(R.id.editPhone);

        dialog.setView(register_layout);

        //set button
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //check validation
                if (TextUtils.isEmpty(editEmail.getText().toString()))
                {
                    Snackbar.make(majorLayout,"Please enter email address",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editPhone.getText().toString()))
                {
                    Snackbar.make(majorLayout,"Please enter phone number",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editName.getText().toString()))
                {
                    Snackbar.make(majorLayout,"Please enter your name",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editPassword.getText().toString()))
                {
                    Snackbar.make(majorLayout,"Please enter password",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (editPassword.getText().toString().length() < 6)
                {
                    Snackbar.make(majorLayout,"Password is too short!!!",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //registration of a new user
                auth.createUserWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //saving user to the db
                                Client client = new Client();
                                //User user = new User();
                                client.setEmail(editEmail.getText().toString());
                                client.setPassword(editPassword.getText().toString());
                                client.setName(editName.getText().toString());
                                client.setPhone(editPhone.getText().toString());

                                //Use the email as the key
                                clients.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(client)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(majorLayout,"Registration successful",Snackbar.LENGTH_SHORT).show();
                                                return;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(majorLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                                                return;
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(majorLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                        });

            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    private void showLoginDialog() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle("LOGIN");
        dialog.setMessage("Please use email to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View signin_layout =inflater.inflate(R.layout.signin,null);

        final MaterialEditText editEmail =signin_layout.findViewById(R.id.editEmail);
        final MaterialEditText editPassword =signin_layout.findViewById(R.id.editPassword);

        dialog.setView(signin_layout);

        //set button
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //setting disable button signin if it is processing
                btnSignIn.setEnabled(false);


                //check validation
                if (TextUtils.isEmpty(editEmail.getText().toString())) {
                    Snackbar.make(majorLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    Snackbar.make(majorLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (editPassword.getText().toString().length() < 6) {
                    Snackbar.make(majorLayout, "Password is too short!!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                final android.app.AlertDialog waitingDialog = new SpotsDialog(ClientLoginActivity.this);
                waitingDialog.show();

                //Login to the system
                auth.signInWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                waitingDialog.dismiss();
                                startActivity(new Intent(ClientLoginActivity.this ,ClientNavigationDrawer.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(majorLayout, "Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();

                        //Active Button
                        btnSignIn.setEnabled(true);
                    }
                });

            }

        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }
}

