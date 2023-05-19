package edu.ub.pis.app.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.User;
import edu.ub.pis.app.viewmodel.users.UsersViewModel;

public class UsersActivity extends AppCompatActivity implements LifecycleOwner {
    private RecyclerView recyclerView;
    private PopupWindow popupWindow;
    private View popupView;

    private boolean click = false;
    private UsersViewModel mUsersViewModel;

    private UserTrainerAdapter mUserTrainerAdapter;
    private ArrayList<User> userList;

    private User lastAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mUsersViewModel = new ViewModelProvider(this)
                .get(UsersViewModel.class);

        getSupportActionBar().hide();

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.add_user_popup_layout, null);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE)); // Fondo blanco
        popupWindow.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Tooltip);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(40);



        EditText mail = popupView.findViewById(R.id.editTextUserMail);
        EditText number = popupView.findViewById(R.id.editTextUserNumber);



        Button addUserButton = popupView.findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser(mail.getText().toString(),number.getText().toString());
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);// Muestra la ventana emergente en el centro de la pantalla

            }
        });

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Crear la lista de elementos para el RecyclerView
        userList = new ArrayList();
        for(User user : mUsersViewModel.getUsers().getValue()){
            if(user.getTrainer()){
                userList.add(user);
            }
        }

        // Crear y establecer el adaptador para el RecyclerView
        mUserTrainerAdapter = new UserTrainerAdapter(userList);
        recyclerView.setAdapter(mUserTrainerAdapter);

        /*mUsersViewModel.getUsers().observe(this,new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                // Triem els usuaris que son Trainers
                ArrayList<User> users1 = new ArrayList<>();
                for(User user : users){
                    if(!user.getTrainer()){
                        users1.add(user);
                    }
                }

                // Actualitzem la llista d'entrenadors en el RecyclerViewAdapter
                mUserTrainerAdapter.updateUsers(users1);
            }
        });*/
        mUsersViewModel.loadUsersFromRepository();

    }

    private void addUser(String mail, String number){
        int userNum = -1;
        boolean encontrado = false;
        if (mail.isEmpty() || number.isEmpty()){
            Toast.makeText(UsersActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            try {
                userNum = Integer.parseInt(number);
            } catch (Exception i){
                Toast.makeText(UsersActivity.this, "El código de usuario debe ser un número entero", Toast.LENGTH_SHORT).show();
            }
            for (User user : mUsersViewModel.getUsers().getValue()){

                String documentSnapshotKey = user.getId();
                String[] parts = documentSnapshotKey.split("/"); // Dividir la cadena en dos partes
                String email_aux = parts[1];
                String[] parts2 = email_aux.split(","); // Dividir la cadena en dos partes
                String email = parts2[0];
                if(!user.getTrainer() && email.equals(mail) && (user.getUserCode() == userNum)){
                   if (userList.contains(user)){
                        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.users_coordinator_layout), "Este usuario ya ha sido añadido.", 3000);
                        mySnackbar.show();
                    } else {
                        userList.add(user);
                        lastAdded = user;
                        Snackbar mySnackbar1 = Snackbar.make(findViewById(R.id.users_coordinator_layout), "Se ha añadido el usuario.", 3000);
                        mySnackbar1.setAction("Undo", new MyUndoListener());
                        mySnackbar1.show();
                        TextView text = findViewById(R.id.users_textview);
                        text.setText("");
                        mUserTrainerAdapter.notifyDataSetChanged();
                    }
                    encontrado = true;
                }
            }
            if (!encontrado) {
                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.users_coordinator_layout), "El email o el código de usuarios no son correctos.", 3000);
                mySnackbar.show();
            }
        }
    }

    public class MyUndoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            userList.remove(lastAdded);
            mUserTrainerAdapter.notifyDataSetChanged();
        }
    }

}