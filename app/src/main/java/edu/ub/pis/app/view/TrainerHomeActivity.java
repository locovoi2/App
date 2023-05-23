package edu.ub.pis.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.ActivityTrainerHomeBinding;
import edu.ub.pis.app.model.User;

public class TrainerHomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTrainerHomeBinding binding;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                user= null;
            } else {
                user= (User)extras.get("user");
            }
        } else {
            user= (User) savedInstanceState.getSerializable("user");
        }

        binding = ActivityTrainerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarTrainerHome.toolbar);

        DrawerLayout drawer = binding.drawerTrainerLayout;
        NavigationView navigationView = binding.navTrainerView;

        View headerView = navigationView.getHeaderView(0);
        Intent intent = getIntent();
        if (intent != null) {
            String mUserMail = (String) intent.getStringExtra("USER_MAIL");
            String mUserName = (String) intent.getStringExtra("USER_NAME");
            TextView userMailTextView = headerView.findViewById(R.id.uMail);
            TextView userNameTextView = headerView.findViewById(R.id.uName_surname);
            userMailTextView.setText(mUserMail);
            userNameTextView.setText(mUserName);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_trainer_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_trainer_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trainer_home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_trainer_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}