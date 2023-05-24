package edu.ub.pis.app.view.ui.trainer_manage_routines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import edu.ub.pis.app.databinding.FragmentManageRoutinesBinding;
import edu.ub.pis.app.view.ViewPagerAdapter;
import edu.ub.pis.app.view.ui.trainer_manage_routines.pages.AddRoutinesPage;
import edu.ub.pis.app.view.ui.trainer_manage_routines.pages.SeeRoutinesPage;
import me.relex.circleindicator.CircleIndicator;

public class TrainerManageRoutinesFragment extends Fragment {
    private FragmentManageRoutinesBinding binding;
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    private CircleIndicator indicator;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageRoutinesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new AddRoutinesPage());
        fragments.add(new SeeRoutinesPage());

        pager = binding.viewPagerManage;
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);

        indicator = binding.ciManage;
        indicator.setViewPager(pager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}