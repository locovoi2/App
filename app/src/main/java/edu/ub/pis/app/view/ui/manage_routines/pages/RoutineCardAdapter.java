package edu.ub.pis.app.view.ui.manage_routines.pages;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.Routine;

public class RoutineCardAdapter extends RecyclerView.Adapter<RoutineCardAdapter.ViewHolder> {
    private ArrayList<Routine> mRoutines; //Referencia a la llista de rutines
    private OnClickInfoListener mOnClickInfoListener; // Qui hagi d'obrir popup amb info exercicis
    private static RoutineCardAdapter instance;
    private Context context;

    /** Definició de listener (interficie)
     *  per a quan algú vulgui escoltar un event de OnClickInfo, és a dir,
     *  quan l'usuari faci click en l'imatge info de la page AddRoutinesPage
     */
    public interface OnClickInfoListener {
        void OnClickInfo(int position);
    }

    public RoutineCardAdapter(Context context, ArrayList<Routine> routineList) {
        this.mRoutines = routineList;
        this.context = context;
    }

    public static RoutineCardAdapter getInstance(Context context, ArrayList<Routine> routineList) {
        if (instance == null) {
            instance = new RoutineCardAdapter(context, routineList);
        }
        return instance;
    }

    public void setOnClickInfoListener(OnClickInfoListener listener) {
        this.mOnClickInfoListener = listener;
    }

    @NonNull
    @Override
    public RoutineCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate crea una view genèrica definida pel layout que l'hi passem (l'user_card_layout)
        View view = inflater.inflate(R.layout.routine_card_layout, parent, false);

        // La classe ViewHolder farà de pont entre la classe User del model i la view (UserCard).
        return new RoutineCardAdapter.ViewHolder(view);
    }

    /* Mètode cridat per cada ViewHolder de la RecyclerView */
    @Override
    public void onBindViewHolder(@NonNull RoutineCardAdapter.ViewHolder holder, int position) {
        // El ViewHolder té el mètode que s'encarrega de llegir els atributs del User (1r parametre),
        // i assignar-los a les variables del ViewHolder.
        // Qualsevol listener que volguem posar a un item, ha d'entrar com a paràmetre extra (2n).
        holder.bind(mRoutines.get(position), this.mOnClickInfoListener, context);

    }

    /**
     * Retorna el número d'elements a la llista.
     * @return
     */
    @Override
    public int getItemCount() {
        return mRoutines.size();
    }

    /**
     * Mètode que seteja de nou la llista d'usuaris si s'hi han fet canvis de manera externa.
     * @param routines
     */
    public void setRoutines(ArrayList<Routine> routines) {
        this.mRoutines = routines; // no recicla/repinta res
    }

    /**
     * Mètode que repinta la RecyclerView sencera.
     */
    public void updateRoutines() {
        notifyDataSetChanged();
    }

    public void hideRoutine(int position) {
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) { notifyItemInserted(position); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mRoutineName;
        private final ImageView mRoutinePicture;
        private final ImageView mInfoPicture;
        public ConstraintLayout mlayoutABorrar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRoutineName = itemView.findViewById(R.id.routine_name);
            mRoutinePicture = itemView.findViewById(R.id.foto_descriptiva);
            mInfoPicture = itemView.findViewById(R.id.info_icon);
            mlayoutABorrar = itemView.findViewById(R.id.layoutAborrar);
        }

        public void bind(final Routine routine, OnClickInfoListener Infolistener, Context context) {
            mRoutineName.setText(routine.getName());
            // Seteja el listener onClick del botó d'info (info), que alhora
            // cridi el mètode OnClickInfo que implementen els nostres propis
            // listeners de tipus OnClickInfoListener.
            mInfoPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Crear la vista del Pop-up y la RecyclerView
                    View popupView = LayoutInflater.from(context).inflate(R.layout.popup_routine_exercises_layout, null);
                    RecyclerView recyclerView = popupView.findViewById(R.id.routine_exercises_recycler);

                    // Crear un objeto PopupWindow
                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                    // Configurar el adaptador y la lista de ejercicios en la RecyclerView
                    ExercisesAdapter exercisesAdapter = new ExercisesAdapter(routine.getExercises());
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(exercisesAdapter);

                    // Mostrar el Pop-up en la posición del botón de información
                    int[] location = new int[2];
                    mInfoPicture.getLocationOnScreen(location);
                    popupWindow.showAtLocation(mInfoPicture, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());

                }
            });
        }
    }
}
