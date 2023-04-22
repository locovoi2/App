package edu.ub.pis.app.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.UserRepository;

public class SignInActivity extends AppCompatActivity {
    private final String TAG = "SignInActivity";
    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private CheckBox checkBoxTrainer;
    private Button buttonContinue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        // Inicializar vistas
        editTextEmail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxTrainer = findViewById(R.id.checkBoxTrainer);
        TextView textViewSignUp = findViewById(R.id.textViewSignUp);

        SpannableString spannableString = new SpannableString("Don't have an account? Sign Up");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // Dirigir al usuario a otra actividad
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        };

        // Agregar el ClickableSpan a la palabra "Sign In"
        spannableString.setSpan(clickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// Establecer el texto con el ClickableSpan en el TextView
        textViewSignUp.setText(spannableString);
        textViewSignUp.setMovementMethod(LinkMovementMethod.getInstance()); // Para hacer clickeable el texto

        buttonContinue = findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(view -> {
            signIn(editTextEmail.getText().toString(), editTextPassword.getText().toString(),checkBoxTrainer.isChecked());
        });
    }

    protected void signIn(String email, String password, boolean trainer) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignInActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Codi per fer sign in
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Exception exception = task.getException();
                                if (exception != null) {
                                    Toast.makeText(SignInActivity.this, "Correo o contraseña invalidos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

        }
    }

}



