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

public class SignUpActivity extends AppCompatActivity {
    private final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private CheckBox checkBoxTrainer;
    private Button buttonContinue;
    private UserRepository mRepository;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mRepository = UserRepository.getInstance();

        // Inicializar vistas
        editTextName = findViewById(R.id.editTextFirstName);
        editTextSurname = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        checkBoxTrainer = findViewById(R.id.checkBoxTrainer);
        TextView textViewSignIn = findViewById(R.id.textViewSignIn);

        SpannableString spannableString = new SpannableString("Have an account? Sign In");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // Dirigir al usuario a otra actividad
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        };

        // Agregar el ClickableSpan a la palabra "Sign In"
        spannableString.setSpan(clickableSpan, 17, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// Establecer el texto con el ClickableSpan en el TextView
        textViewSignIn.setText(spannableString);
        textViewSignIn.setMovementMethod(LinkMovementMethod.getInstance()); // Para hacer clickeable el texto

        buttonContinue = (Button) findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(view -> {
            signUp(editTextName.getText().toString(), editTextSurname.getText().toString(), editTextEmail.getText().toString(), editTextPassword.getText().toString(),
                    editTextConfirmPassword.getText().toString(), checkBoxTrainer.isChecked());
        });
    }

    protected void signUp(String name, String surname, String email, String password, String confirmPassword, boolean trainer) {
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            // Mostrar un diálogo de alerta con el mensaje de error
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setTitle("Error")
                    .setMessage("Passwords don't match")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            mAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.isSuccessful()) {
                                SignInMethodQueryResult result = task.getResult();
                                List<String> signInMethods = result.getSignInMethods();

                                if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                    // El usuario ya está registrado
                                    Toast.makeText(getApplicationContext(), "Este correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
                                } else {
                                    // El usuario no está registrado
                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        mRepository.addUser(
                                                                name,
                                                                surname,
                                                                email,
                                                                checkBoxTrainer.isChecked()
                                                        );
                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error al registrar al usuario", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error al comprobar si el usuario está registrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}



