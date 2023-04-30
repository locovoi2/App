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

import edu.ub.pis.app.databinding.FragmentDailyRoutinesBinding;

public class DailyRoutinesFragment extends Fragment {

    private FragmentDailyRoutinesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DailyRoutinesViewModel dailyRoutinesViewModel = new ViewModelProvider(this).get(DailyRoutinesViewModel.class);

        binding = FragmentDailyRoutinesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}