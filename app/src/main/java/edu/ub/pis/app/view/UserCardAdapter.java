package edu.ub.pis.app.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;
import androidx.core.content.ContextCompat;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.ViewHolder> {

    /** Definició de listener (interficie)
     *  per a quan algú vulgui escoltar un event de OnClickHide, és a dir,
     *  quan l'usuari faci clic en la creu (amagar) algún dels items de la RecyclerView
     */
    public interface OnClickHideListener {
        void OnClickHide(int position);
    }

    public interface  OnClickPictureListener {

        void onClickPicture(String name);

    }

    private ArrayList<Routine> mRoutines; // Referència a la llista d'usuaris
    private OnClickHideListener mOnClickHideListener; // Qui hagi de repintar la ReciclerView
                                                      // quan s'amagui
    private  OnClickPictureListener mOnClickPictureListener;

    private static Context context;

    // Constructor
    public UserCardAdapter(ArrayList<Routine> userList, Context context) {
        this.mRoutines = userList; // no fa new (La llista la manté el ViewModel)
        this.context = context;
    }

    public void setOnClickHideListener(OnClickHideListener listener) {
        this.mOnClickHideListener = listener;
    }

    public void setOnClickPictureListener(OnClickPictureListener listener) {
        this.mOnClickPictureListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate crea una view genèrica definida pel layout que l'hi passem (l'user_card_layout)
        View view = inflater.inflate(R.layout.user_card_layout, parent, false);

        // La classe ViewHolder farà de pont entre la classe User del model i la view (UserCard).
        return new ViewHolder(view);
    }

    /* Mètode cridat per cada ViewHolder de la RecyclerView */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // El ViewHolder té el mètode que s'encarrega de llegir els atributs del User (1r parametre),
        // i assignar-los a les variables del ViewHolder.
        // Qualsevol listener que volguem posar a un item, ha d'entrar com a paràmetre extra (2n).
        holder.bind(mRoutines.get(position), this.mOnClickHideListener, this.mOnClickPictureListener);
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
     * @param users
     */
    public void setUsers(ArrayList<Routine> users) {
        this.mRoutines = users; // no recicla/repinta res
    }

    /**
     * Mètode que repinta la RecyclerView sencera.
     */
    public void updateUsers() {
        notifyDataSetChanged();
    }

    /**
     * Mètode que repinta només posició indicada
     * @param position
     */
    public void hideUser(int position) {
        notifyItemRemoved(position);
    }

    /**
     * Classe ViewHolder. No és més que un placeholder de la vista (user_card_list.xml)
     * dels items de la RecyclerView. Podem implementar-ho fora de RecyclerViewAdapter,
     * però es pot fer dins.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mCardImageView;
        private final TextView mCardRoutineName;
        private final TextView mCardExercises;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardImageView = itemView.findViewById(R.id.avatar);
            mCardRoutineName = itemView.findViewById(R.id.routinename);
            mCardExercises = itemView.findViewById(R.id.completedexercises);
        }

        public void bind(final Routine routine, OnClickHideListener listener, OnClickPictureListener listener2) {
            mCardRoutineName.setText(routine.getName());
            int nComplete = 0;
            int nTotal = 0;
            for(Exercise ex : routine.getExercises()) {
                if(ex.getCompleted()) {
                    nComplete++;
                }
                nTotal++;
            }
            mCardExercises.setText("Completed exercices: " + nComplete + "/" + nTotal);
            // Carrega foto de l'usuari de la llista directament des d'una Url
            // d'Internet.
            mCardImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icono_mancuerna));
            // Seteja el listener onClick del botó d'amagar (hide), que alhora
            // cridi el mètode OnClickHide que implementen els nostres propis
            // listeners de tipus OnClickHideListener.

            mCardImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener2.onClickPicture(mCardRoutineName.getText().toString());
                }
            });
        }
    }

}