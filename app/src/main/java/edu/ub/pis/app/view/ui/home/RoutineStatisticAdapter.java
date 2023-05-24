package edu.ub.pis.app.view.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;

public class RoutineStatisticAdapter extends RecyclerView.Adapter<RoutineStatisticAdapter.ViewHolder> {
    private ArrayList<Routine> routines;
    private Context context;
    private int day;

    public RoutineStatisticAdapter(ArrayList<Routine> routines, Context context, int day) {
        this.routines = routines;
        this.context = context;
        this.day = day;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_statistic_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Routine r = routines.get(position);
        String name = r.getName();

        // Set your data to the views in the layout
        holder.routineNameTextView.setText(name);
        int nCompleted = 0;
        int nToComplete = 0;

        for(Exercise e : r.getExercises()) {
            if(e.getCompleted().get(day)) {
                nCompleted++;
            }
            else {
                nToComplete++;
            }
        }

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(nToComplete, "To do"));
        entries.add(new PieEntry(nCompleted, "Completed"));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(new int[]{Color.LTGRAY, Color.DKGRAY}); // Customize colors if needed
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setValueTextColor(Color.parseColor("#5F9E64"));

        PieData pieData = new PieData(pieDataSet);

        holder.pieChart.setData(pieData);
        holder.pieChart.invalidate(); // Refresh the chart
        holder.pieChart.setDrawHoleEnabled(true);
        holder.pieChart.setHoleColor(Color.TRANSPARENT);

        holder.pieChart.getDescription().setEnabled(false);

        Legend l = holder.pieChart.getLegend();
        l.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView routineNameTextView;
        private PieChart pieChart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            routineNameTextView = itemView.findViewById(R.id.routine_name);
            pieChart = itemView.findViewById(R.id.pieChart2);
        }
    }
}
