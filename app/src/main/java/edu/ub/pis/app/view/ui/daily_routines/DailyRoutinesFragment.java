package edu.ub.pis.app.view.ui.daily_routines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import edu.ub.pis.app.databinding.FragmentDailyRoutinesBinding;
import edu.ub.pis.app.databinding.FragmentManageRoutinesBinding;
import edu.ub.pis.app.view.ViewPagerAdapter;
import edu.ub.pis.app.view.ui.daily_routines.pages.FridayPage;
import edu.ub.pis.app.view.ui.daily_routines.pages.MondayPage;
import edu.ub.pis.app.view.ui.daily_routines.pages.SaturdayPage;
import edu.ub.pis.app.view.ui.daily_routines.pages.SundayPage;
import edu.ub.pis.app.view.ui.daily_routines.pages.ThursdayPage;
import edu.ub.pis.app.view.ui.daily_routines.pages.TuesdayPage;
import edu.ub.pis.app.view.ui.daily_routines.pages.WednesdayPage;
import edu.ub.pis.app.view.ui.manage_routines.pages.AddRoutinesPage;
import edu.ub.pis.app.view.ui.manage_routines.pages.SeeRoutinesPage;
import me.relex.circleindicator.CircleIndicator;

public class DailyRoutinesFragment extends Fragment {

    private FragmentDailyRoutinesBinding binding;
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    private CircleIndicator indicator;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDailyRoutinesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MondayPage());
        fragments.add(new TuesdayPage());
        fragments.add(new WednesdayPage());
        fragments.add(new ThursdayPage());
        fragments.add(new FridayPage());
        fragments.add(new SaturdayPage());
        fragments.add(new SundayPage());

        pager = binding.viewPagerDaily;
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);

        indicator = binding.ciDaily;
        indicator.setViewPager(pager);

        //Metodo para hace clickables los indicadores
        for (int i = 0; i < indicator.getChildCount(); i++) {
            final int page = i;
            indicator.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(page);
                }
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}