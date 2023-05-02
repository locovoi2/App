package edu.ub.pis.app.view.ui.daily_routines.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.FragmentManageRoutinesBinding;

public class SundayPage extends Fragment {
    private FragmentManageRoutinesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.page_monday,
                        container,
                        false);

        binding = FragmentManageRoutinesBinding.inflate(inflater, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
