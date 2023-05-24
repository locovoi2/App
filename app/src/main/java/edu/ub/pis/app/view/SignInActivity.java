package edu.ub.pis.app.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.User;

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

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference usersCollection = db.collection("users");
                                DocumentReference userDoc = usersCollection.document(email);

                                userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                User User = document.toObject(User.class);
                                                // Obtén el valor del campo "nombre"
                                                String name = User.getName();
                                                String surname = User.getSurname();
                                                boolean premium = User.getPremium();
                                                String Name_surname = name + " " + surname;
                                                boolean train = User.getTrainer();
                                                if (train && trainer) {
                                                    Intent intent = new Intent(SignInActivity.this, UsersActivity.class);
                            /*                      intent.putExtra("USER_MAIL", email);
                                                    intent.putExtra("USER_NAME", Name_surname);
                                                    intent.putExtra("user", User); */
                                                    startActivity(intent);
                                                } else if (!train && !trainer) {
                                                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                                    intent.putExtra("USER_MAIL", email);
                                                    intent.putExtra("USER_NAME", Name_surname);
                                                    intent.putExtra("USER_PREMIUM", premium);
                                                    startActivity(intent);
                                                } else if (train && !trainer) {
                                                    Toast.makeText(SignInActivity.this, "Entrenador no registrado como usuario", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(SignInActivity.this, "Usuario no registrado como entrenador", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Log.d("MiActividad", "No se encontró el documento");
                                            }
                                        } else {
                                            Log.d("MiActividad", "Error al obtener el documento", task.getException());
                                        }
                                    }
                                });

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



