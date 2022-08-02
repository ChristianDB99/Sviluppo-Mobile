package com.example.e_culture_tool_a;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    EditText memail;
    EditText mpassword;
    Button mloginButton;
    Button mospiteButton;
    TextView mregisterText;
    ProgressBar mprogressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memail = findViewById(R.id.InputEmail);
        mpassword = findViewById(R.id.InputPassword);
        mloginButton = findViewById(R.id.ButtonRegister);
        mospiteButton = findViewById(R.id.ButtonOspite);
        mregisterText = findViewById(R.id.VaiRegistrazione);
        mprogressBar = findViewById(R.id.progressBarLogin);

        fAuth = FirebaseAuth.getInstance();


        mregisterText.setOnClickListener(view -> {

            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

        });

        mloginButton.setOnClickListener(view -> {
            String email = memail.getText().toString().trim();
            String password = mpassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){

                memail.setError("Inserisci l'email");
                return;

            }

            if(TextUtils.isEmpty(password)){

                mpassword.setError("Inserisci la Password");
                return;
            }
            mprogressBar.setVisibility(View.VISIBLE);

            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {



                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Login effettuato con successo", Toast.LENGTH_SHORT).show();
                        mprogressBar.setVisibility(View.GONE);
                        redirect();

                    }else{
                        Toast.makeText(MainActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mprogressBar.setVisibility(View.GONE);

                    }


                }

            });



        });


    }

    private void redirect() {
        fAuth = FirebaseAuth.getInstance();
        user_id = fAuth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();

        DocumentReference docReference = fStore.collection("utenti").document(user_id);
        docReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                String ruolo = value.getString("Curatore");
                Toast.makeText(MainActivity.this, ruolo , Toast.LENGTH_SHORT).show();

                boolean b1 = Boolean.parseBoolean(ruolo);

                if(b1){
                    startActivity(new Intent(getApplicationContext(), HomeCuratoreActivity.class));

                }else {

                    startActivity(new Intent(getApplicationContext(), HomeVisitatoreActivity.class));
                }


            }
        });

    }

}