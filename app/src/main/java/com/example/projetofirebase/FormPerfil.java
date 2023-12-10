package com.example.projetofirebase;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FormPerfil extends AppCompatActivity {

    private TextView editNome, editEmail;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_perfil);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editNome = findViewById(R.id.text_userlAC);
        editEmail = findViewById(R.id.text_email_user);
        logoutButton = findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                Intent intent = new Intent(FormPerfil.this, FormLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("usuarios").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String nome = document.getString("nome");
                                String email = document.getString("email");

                                editNome.setText(nome);
                                editEmail.setText(email);
                            } else {
                                editNome.setText("Nome não encontrado");
                                editEmail.setText("Email não encontrado");
                            }
                        } else {
                            editNome.setText("Erro ao carregar nome");
                            editEmail.setText("Erro ao carregar email");
                        }
                    });
        } else {
        }
    }
}
//terminar depois isso...
//seguir a documentação para terminar o get dos dados do usuario para exibir na tela
