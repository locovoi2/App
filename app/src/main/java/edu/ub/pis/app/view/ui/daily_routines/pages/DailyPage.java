package edu.ub.pis.app.view.ui.daily_routines.pages;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.FragmentDailyRoutinesBinding;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;
import edu.ub.pis.app.view.UserCardAdapter;
import edu.ub.pis.app.view.ui.daily_routines.DailyRoutineCardAdapter;
import edu.ub.pis.app.view.ui.daily_routines.ExerciceCompletedCardAdapter;

public class DailyPage extends Fragment {
    private FragmentDailyRoutinesBinding binding;
    private RecyclerView mUserCardsRV;
    private UserCardAdapter mUserCardRVAdapter;
    private ArrayList<Routine> routines = new ArrayList<>();
    private int day = -1;
    private String [] dayList = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private TextView dayTitle;
    private ArrayList<Routine> totalRoutines = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.page_monday,
                        container,
                        false);

        binding = FragmentDailyRoutinesBinding.inflate(inflater, container, false);

        Context currentContext = getContext();
        DailyPage thisPage = this;

        dayTitle = rootView.findViewById(R.id.dayTitle);
        if(day != -1)
            dayTitle.setText(dayList[day]);

        TextView addButton = rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DailyRoutineCardAdapter popupAdapter = new DailyRoutineCardAdapter(totalRoutines, day, mUserCardRVAdapter, thisPage);
                showPopupMenu(inflater, currentContext, rootView, popupAdapter);
            }
        });

        // Anem a buscar la RecyclerView i fem dues coses:
        mUserCardsRV = rootView.findViewById(R.id.userCardRv);

        // (1) Li assignem un layout manager.
        LinearLayoutManager manager = new LinearLayoutManager(
                currentContext, LinearLayoutManager.VERTICAL, false
        );
        mUserCardsRV.setLayoutManager(manager);

        mUserCardRVAdapter = new UserCardAdapter(routines, currentContext, day);

        mUserCardRVAdapter.setOnClickPictureListener(new UserCardAdapter.OnClickPictureListener() {
            @Override
            public void onClickPicture(String name) {
                ArrayList<Exercise> exercises = new ArrayList<>();
                String routineName = "";
                for(Routine r : totalRoutines) {
                    if (r.getName().equals(name)) {
                        exercises = r.getExercises();
                        routineName = r.getName();
                        break;
                    }
                }
                ExerciceCompletedCardAdapter popupAdapter = new ExerciceCompletedCardAdapter(exercises, currentContext, day, routineName, mUserCardRVAdapter, thisPage);
                showPopupMenu(inflater, currentContext, rootView, popupAdapter);
            }
        });

        mUserCardsRV.setAdapter(mUserCardRVAdapter);

        return rootView;
    }

    private void showPopupMenu(LayoutInflater inflater, Context currentContext, ViewGroup rootView, RecyclerView.Adapter popupAdapter) {
        View popupView = inflater.inflate(R.layout.popup_daily, null);
        RecyclerView popupRecyclerView = popupView.findViewById(R.id.popupRecyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(
                currentContext, LinearLayoutManager.VERTICAL, false
        );
        popupRecyclerView.setLayoutManager(manager);

        popupRecyclerView.setAdapter(popupAdapter);

        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int popupHeight = screenHeight * 2 / 3;
        int topThirdHeight = screenHeight / 3;

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, popupHeight);
        int cornerRadius = 20;
        int backgroundColor = Color.parseColor("#629C44");

        GradientDrawable roundedCornerDrawable = new GradientDrawable();
        GradientDrawable shadowDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#40000000"), Color.TRANSPARENT}
        );
        shadowDrawable.setBounds(0, 0, ((Activity) currentContext).getWindow().getDecorView().getWidth(), topThirdHeight);

        roundedCornerDrawable.setCornerRadius(cornerRadius);
        roundedCornerDrawable.setColor(backgroundColor);
        popupWindow.setBackgroundDrawable(roundedCornerDrawable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // Dismiss listener for the popup window
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rootView.getOverlay().remove(shadowDrawable); // Remove the shadow drawable from the background
            }
        });

        popupWindow.showAtLocation(((Activity) currentContext).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        rootView.getOverlay().add(shadowDrawable);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setDay(int day) {
        this.day = day;
        if(dayTitle != null)
            dayTitle.setText(dayList[day]);
    }

    public int getDay() {
        return this.day;
    }

    public void updateRoutines(ArrayList<Routine> routinesTotal) {
        ArrayList<Routine> dRoutines = new ArrayList<>();
        for(Routine routine : routinesTotal) {
            if(routine.getDay().get(day)) {
                dRoutines.add(routine);
            }
        }
        this.routines = dRoutines;
        this.totalRoutines = routinesTotal;
        if(this.mUserCardRVAdapter != null) {
            this.mUserCardRVAdapter.setUsers(dRoutines);
            this.mUserCardRVAdapter.notifyDataSetChanged();
        }
    }
}
