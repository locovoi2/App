package edu.ub.pis.app.view.ui.manage_routines.pages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.Routine;

public class RoutineCardAdapter extends RecyclerView.Adapter<RoutineCardAdapter.ViewHolder> {
    private ArrayList<Routine> mRoutines; //Referencia a la llista de rutines
    private OnClickInfoListener mOnClickInfoListener; // Qui hagi d'obrir popup amb info exercicis

    /** Definició de listener (interficie)
     *  per a quan algú vulgui escoltar un event de OnClickInfo, és a dir,
     *  quan l'usuari faci click en l'imatge info de la page AddRoutinesPage
     */
    public interface OnClickInfoListener {
        void OnClickInfo(int position);
    }

    public RoutineCardAdapter(ArrayList<Routine> routineList) {
        this.mRoutines = routineList;
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
        holder.bind(mRoutines.get(position), this.mOnClickInfoListener);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mRoutineName;
        private final ImageView mRoutinePicture;
        private final ImageView mInfoPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRoutineName = itemView.findViewById(R.id.routine_name);
            mRoutinePicture = itemView.findViewById(R.id.foto_descriptiva);
            mInfoPicture = itemView.findViewById(R.id.info_icon);
        }

        public void bind(final Routine routine, OnClickInfoListener Infolistener) {
            mRoutineName.setText(routine.getName());
            // Seteja el listener onClick del botó d'amagar (hide), que alhora
            // cridi el mètode OnClickHide que implementen els nostres propis
            // listeners de tipus OnClickHideListener.
            mInfoPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Infolistener.OnClickInfo(getAdapterPosition());
                }
            });
        }
    }
}
