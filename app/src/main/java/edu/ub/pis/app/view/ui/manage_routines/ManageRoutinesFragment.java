package edu.ub.pis.app.view.ui.manage_routines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis.app.databinding.FragmentManageRoutinesBinding;

public class ManageRoutinesFragment extends Fragment {

    private FragmentManageRoutinesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ManageRoutinesViewModel manageRoutinesViewModel =
                new ViewModelProvider(this).get(ManageRoutinesViewModel.class);

        binding = FragmentManageRoutinesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        manageRoutinesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}