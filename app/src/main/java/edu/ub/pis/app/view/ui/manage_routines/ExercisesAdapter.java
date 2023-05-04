package edu.ub.pis.app.view.ui.manage_routines;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.Exercise;

public class ExercisesAdapter  extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {

    /**
     * Definició de listener (interficie)
     * per a quan algú vulgui escoltar un event de OnClickHide, és a dir,
     * quan l'usuari faci clic en la creu (amagar) algún dels items de la RecyclerView
     */
    public interface OnClickHideListener {
        void OnClickHide(int position);
    }

    private ArrayList<Exercise> mExercises; // Referència a la llista d'exercicis
    private OnClickHideListener mOnClickHideListener; // Qui hagi de repintar la ReciclerView

    // quan s'amagui
    // Constructor
    public ExercisesAdapter(ArrayList<Exercise> exerciseList) {
        this.mExercises = exerciseList; // no fa new (La llista la manté el ViewModel)

    }

    public void setOnClickHideListener(OnClickHideListener listener) {
        this.mOnClickHideListener = listener;
    }

    @NonNull
    @Override
    public ExercisesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate crea una view genèrica definida pel layout que l'hi passem (add new routine layout)
        View view = inflater.inflate(R.layout.list_exercise_element, parent, false);

        // La classe ViewHolder farà de pont entre la classe Exercise del model i la view.
        return new ExercisesAdapter.ViewHolder(view);
    }

    /* Mètode cridat per cada ViewHolder de la RecyclerView */
    @Override
    public void onBindViewHolder(@NonNull ExercisesAdapter.ViewHolder holder, int position) {
        // El ViewHolder té el mètode que s'encarrega de llegir els atributs dels Exercise (1r parametre),
        // i assignar-los a les variables del ViewHolder.
        // Qualsevol listener que volguem posar a un item, ha d'entrar com a paràmetre extra (2n).
        holder.bind(mExercises.get(position), this.mOnClickHideListener);
    }

    /**
     * Retorna el número d'elements a la llista.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mExercises.size();
    }

    /**
     * Mètode que seteja de nou la llista d'eercises si s'hi han fet canvis de manera externa.
     *
     * @param exercises
     */
    public void setExercises(ArrayList<Exercise> exercises) {
        this.mExercises = exercises; // no recicla/repinta res
    }

    /**
     * Mètode que repinta la RecyclerView sencera.
     */
    public void updateExercises() {
        notifyDataSetChanged();
    }

    /**
     * Mètode que repinta només posició indicada
     *
     * @param position
     */
    public void hideExercise(int position) {
        notifyItemRemoved(position);
    }

    /**
     * Classe ViewHolder. No és més que un placeholder de la vista (user_card_list.xml)
     * dels items de la RecyclerView. Podem implementar-ho fora de RecyclerViewAdapter,
     * però es pot fer dins.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mName;
        private final TextView mSeries;
        private final TextView mReps;
        private final TextView mWeight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.listElementExerciseName);
            mSeries = itemView.findViewById(R.id.listElementSeries);
            mReps = itemView.findViewById(R.id.listElementRepes);
            mWeight = itemView.findViewById(R.id.listElementWeight);
        }

        public void bind(final Exercise exercise, OnClickHideListener listener) {
            mName.setText(exercise.getName());
            mSeries.setText(exercise.getSeries());
            mReps.setText(exercise.getReps());
            mWeight.setText(exercise.getWeight());
            // Seteja el listener onClick del botó d'amagar (hide), que alhora
            // cridi el mètode OnClickHide que implementen els nostres propis
            // listeners de tipus OnClickHideListener.
            /*mHideButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnClickHide(getAdapterPosition());
                }
            });*/
        }
    }

}
