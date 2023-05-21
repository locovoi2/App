package edu.ub.pis.app.view.ui.daily_routines;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ub.pis.app.databinding.FragmentDailyRoutinesBinding;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;
import edu.ub.pis.app.view.ViewPagerAdapter;
import edu.ub.pis.app.view.ui.daily_routines.pages.DailyPage;
import me.relex.circleindicator.CircleIndicator;

public class DailyRoutinesFragment extends Fragment {

    private FragmentDailyRoutinesBinding binding;
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    private CircleIndicator indicator;
    private TabLayout tabLayout;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private QuerySnapshot totalRoutines;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDailyRoutinesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        ArrayList<Routine> routines = new ArrayList<>();

        DailyPage mondayPage = new DailyPage();
        mondayPage.setDay(0);
        DailyPage tuesdayPage = new DailyPage();
        tuesdayPage.setDay(1);
        DailyPage wednesdayPage = new DailyPage();
        wednesdayPage.setDay(2);
        DailyPage thursdayPage = new DailyPage();
        thursdayPage.setDay(3);
        DailyPage fridayPage = new DailyPage();
        fridayPage.setDay(4);
        DailyPage saturdayPage = new DailyPage();
        saturdayPage.setDay(5);
        DailyPage sundayPage = new DailyPage();
        sundayPage.setDay(6);

        if(currentUser != null) {
            String userEmail = currentUser.getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersRef = db.collection("users");
            DocumentReference userDocRef = usersRef.document(userEmail);
            CollectionReference routinesRef = userDocRef.collection("routines");

            routinesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        totalRoutines = task.getResult();

                        for (QueryDocumentSnapshot document : totalRoutines) {

                            String routineName = document.getId(); // Get the document name
                            List<Map<String, Object>> exercisesList = (List<Map<String, Object>>) document.get("exercises");
                            ArrayList<Boolean> days = (ArrayList<Boolean>) document.get("days");

                            ArrayList<Exercise> exercisesArray = new ArrayList<>();

                            for (Map<String, Object> exercise : exercisesList) {
                                String name = (String) exercise.get("name");
                                String weight = (String) exercise.get("weight");
                                String series = (String) exercise.get("series");
                                String reps = (String) exercise.get("reps");
                                ArrayList<Boolean> completed = (ArrayList<Boolean>) exercise.get("completed");
                                //Log.println(Log.ASSERT, name,""+completed.size());
                                /*
                                Toast toast = Toast.makeText(getApplicationContext(), reps, Toast.LENGTH_SHORT);
                                toast.show();*/

                                exercisesArray.add(new Exercise(name, series, reps, weight, completed));
                            }

                            routines.add(new Routine(routineName, exercisesArray, days));
                        }

                        mondayPage.updateRoutines(routines);
                        tuesdayPage.updateRoutines(routines);
                        wednesdayPage.updateRoutines(routines);
                        thursdayPage.updateRoutines(routines);
                        fridayPage.updateRoutines(routines);
                        saturdayPage.updateRoutines(routines);
                        sundayPage.updateRoutines(routines);

                    }
                }
            });
        }


        // Fins aqui

        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(mondayPage);
        fragments.add(tuesdayPage);
        fragments.add(wednesdayPage);
        fragments.add(thursdayPage);
        fragments.add(fridayPage);
        fragments.add(saturdayPage);
        fragments.add(sundayPage);

        pager = binding.viewPagerDaily;
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);

        tabLayout = binding.tabDaily;
        tabLayout.setupWithViewPager(pager);

        LocalDate currentDate = LocalDate.now();
        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
        int currentDayOfWeekValue = currentDayOfWeek.getValue() - 1;

        pager.setCurrentItem(currentDayOfWeekValue);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}