package edu.ub.pis.app.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.ActivityHomeBinding;
import edu.ub.pis.app.viewmodel.HomeActivityViewModel;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private HomeActivityViewModel mHomeActivityViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private Button mButtonVip;
    private Button mButtonLogout;
    private TextView mTextViewVip;
    private boolean doubleBackToExitPressedOnce = false;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicialitzem ViewModel
        mHomeActivityViewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);

        //Carreguem l'usuari
        mHomeActivityViewModel.loadUserFromRepository();

        mAuth = FirebaseAuth.getInstance();

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mButtonVip = navigationView.findViewById(R.id.buttonVip);
        mButtonLogout = navigationView.findViewById(R.id.buttonLogout);

        //Seteamos el nombre y mail y si es vip del usuario
        View headerView = navigationView.getHeaderView(0);
        Intent intent = getIntent();
        if (intent != null) {
            String mUserMail = (String) intent.getStringExtra("USER_MAIL");
            String mUserName = (String) intent.getStringExtra("USER_NAME");
            boolean mUserPremium = (boolean) intent.getBooleanExtra("USER_PREMIUM", false);
            int mUsCode = (int) intent.getIntExtra("USER_CODE", 0);
            String UsCode = String.valueOf(mUsCode);
            System.out.println(mUsCode);
            System.out.println(UsCode);
            TextView userMailTextView = headerView.findViewById(R.id.uMail);
            TextView userNameTextView = headerView.findViewById(R.id.uName_surname);
            mTextViewVip = headerView.findViewById(R.id.textViewVip);
            TextView userTokenTv = headerView.findViewById(R.id.tvToken);
            userMailTextView.setText(mUserMail);
            userNameTextView.setText(mUserName);
            userTokenTv.setText(UsCode);
            if(mUserPremium) { mTextViewVip.setText("VIP"); }
            else { mTextViewVip.setText(""); }
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_reports, R.id.nav_daily_routines,R.id.nav_manage_routines,R.id.nav_trainers)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if(id == R.id.nav_trainers) {
                if(!mHomeActivityViewModel.getPremium()) {
                    Toast.makeText(HomeActivity.this, "Hazte premium", Toast.LENGTH_SHORT).show();
                } else {
                    navController.navigate(id);
                    drawer.closeDrawer(GravityCompat.START);
                }
            } else {
                navController.navigate(id);
                drawer.closeDrawer(GravityCompat.START);
            }

            return true;
        });

        mButtonLogout.setOnClickListener(view -> {
            mAuth.signOut();
            finish();
        });

        mButtonVip.setOnClickListener(view -> {
            setPremium();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        //Posem de color negre el settings
        MenuItem settingsMenuItem = menu.findItem(R.id.action_settings);
        SpannableString spannableString = new SpannableString(settingsMenuItem.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        settingsMenuItem.setTitle(spannableString);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity(); //Cierra la aplicacion
            return;
        }

        //Primera vez que se pulsa back y se da el aviso
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pulsa de nuevo para salir", Toast.LENGTH_SHORT).show();

        //Tiempo limite para dar segundo click y cerrar la app, si no vuelve a estado anterior
        mHandler.postDelayed(mRunnable, 2000);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    private void setPremium() {
        if(mHomeActivityViewModel.getPremium()) {
            mHomeActivityViewModel.updatePremium(false);
            mTextViewVip.setText("");
            mButtonVip.setText("PREMIUM");
        } else {
            mHomeActivityViewModel.updatePremium(true);
            mTextViewVip.setText("VIP");
            mButtonVip.setText("CANCEL PREMIUM");
        }
    }
}