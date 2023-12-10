package com.example.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {
    private EditText textNome, editEmail, editSenha2;
    private Button butCadastrar;
    String UsuarioID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar().hide();
        iniciarComponentes();

        butCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = textNome.getText().toString();
                String email = editEmail.getText().toString();
                String senha = editSenha2.getText().toString();

                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    cadastrarUsuario(v);
                }
            }
        }
        );
    }

    private void cadastrarUsuario(View v) {
        String email = editEmail.getText().toString();
        String senha = editSenha2.getText().toString();
        // Verifica se o email é válido
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar snackbar = Snackbar.make(v, "Email inválido", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            return;
        }
        // Verifica se a senha tem pelo menos 6 caracteres
        if (senha.length() < 6) {
            Snackbar snackbar = Snackbar.make(v, "A senha deve ter pelo menos 6 caracteres", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            return;
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                SalvardadosUsuario();

                if (task.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(v, "Cadastro realizado com sucesso", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Snackbar snackbar = Snackbar.make(v, "Já existe uma conta com este email", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(v, "Erro ao cadastrar usuário", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                }

            }
        });
    }
    private void SalvardadosUsuario(){

        String nome = textNome.getText().toString();
        String email = editEmail.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuario = new HashMap<>();

        usuario.put("nome", nome);
        usuario.put("email", email);

        UsuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("usuarios").document();
        documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar usuario");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error", "Erro ao salvar o usuario" + e.toString());
            }
        });
        Map<String, Object> data = new HashMap<>();
        data.put("capital", true);

        db.collection("usuarios").document("user")
                .set(data, SetOptions.merge());
    };

    private void iniciarComponentes() {

        textNome = findViewById(R.id.text_nome);
        editEmail = findViewById(R.id.edit_email);
        editSenha2 = findViewById(R.id.edit_senha2);
        butCadastrar = findViewById(R.id.but_cadastrar);
        ImageButton btnToggleSenha = findViewById(R.id.btn_toggle_senha);
        btnToggleSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editSenha2.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Se a senha está visível, oculta a senha
                    editSenha2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnToggleSenha.setImageResource(R.drawable.ic_senha_invisible);
                } else {
                    // Se a senha está oculta, mostra a senha
                    editSenha2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    btnToggleSenha.setImageResource(R.drawable.ic_senha_visible);
                }
                // Move o cursor para o final da senha
                editSenha2.setSelection(editSenha2.getText().length());
            }
        });
    }
}
//cadastro de usuario - CONCLUIDO
//Salvando usuarios no authentication e firestore database - CONCLUIDO
//Icone de exibir senha ou esconder implementado - CONCLUIDO