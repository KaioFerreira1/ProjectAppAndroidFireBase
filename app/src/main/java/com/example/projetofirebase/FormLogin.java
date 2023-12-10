package com.example.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class FormLogin extends AppCompatActivity {
    private TextView text_tela_de_cadastro;
    private EditText editemail, editsenha;
    private Button butlogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        getSupportActionBar().hide();
        iniciarComponentes();

        text_tela_de_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });


        butlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editemail.getText().toString();
                String senha = editsenha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    AutenticarUsuario(view);
                }
            }
        });
    }

    private void AutenticarUsuario(View view) {
        String email = editemail.getText().toString();
        String senha = editsenha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();
                        }
                    }, 3000);
                } else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (Exception e){
                        erro = "Erro, login não efetuado";
                        Snackbar snackbar = Snackbar.make(view, erro,  Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioatual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioatual != null){
            TelaPrincipal();
        }
    }

    private void TelaPrincipal(){
        Intent intent = new Intent(FormLogin.this, FormPerfil.class);
        startActivity(intent);
        finish();
    }

    private void iniciarComponentes() {
        text_tela_de_cadastro = findViewById(R.id.text_tela_cadastro);
        editemail = findViewById(R.id.edit_email);
        editsenha = findViewById(R.id.edit_senha);
        butlogin = findViewById(R.id.but_entrar);
        progressBar = findViewById(R.id.progress_bar);

        if (text_tela_de_cadastro != null) {
            text_tela_de_cadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                    startActivity(intent);
                }
            });
        }
    }
}
//Login de usuarios cadastrados implementado - CONCLUIDO
//Só falta terminar os "gets" dos dados dos usuarios cadastrados para exibir na FormPerfil, após o login