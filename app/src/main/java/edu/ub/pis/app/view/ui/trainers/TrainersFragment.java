package edu.ub.pis.app.view.ui.trainers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis.app.databinding.FragmentHomeBinding;
import edu.ub.pis.app.databinding.FragmentTrainersBinding;

public class TrainersFragment extends Fragment {

    private FragmentTrainersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TrainersViewModel trainersViewModel =
                new ViewModelProvider(this).get(TrainersViewModel.class);

        binding = FragmentTrainersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        trainersViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}