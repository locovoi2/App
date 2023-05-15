package edu.ub.pis.app.view.ui.trainers;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.User;

public class TrainerCardAdapter extends RecyclerView.Adapter<edu.ub.pis.app.view.ui.trainers.TrainerCardAdapter.ViewHolder> {
    private ArrayList<User> mTrainers; //Referencia a la llista de trainers
    private edu.ub.pis.app.view.ui.trainers.TrainerCardAdapter.OnClickInfoListener mOnClickInfoListener; // Qui hagi d'obrir popup amb info exercicis

    private static edu.ub.pis.app.view.ui.trainers.TrainerCardAdapter instance;
    private Context context;
    /** Definició de listener (interficie)
     *  per a quan algú vulgui escoltar un event de OnClickInfo, és a dir,
     *  quan l'usuari faci click en l'imatge info de la page AddRoutinesPage
     */
    public interface OnClickInfoListener {
        void OnClickInfo(int position);
    }

    public TrainerCardAdapter(Context context, ArrayList<User> trainerList) {
        this.mTrainers = trainerList;
        this.context = context;
    }

    public static TrainerCardAdapter getInstance(Context context, ArrayList<User> trainerList) {
        if (instance == null) {
            instance = new edu.ub.pis.app.view.ui.trainers.TrainerCardAdapter(context, trainerList);
        }
        return instance;
    }

    public void setOnClickInfoListener(edu.ub.pis.app.view.ui.trainers.TrainerCardAdapter.OnClickInfoListener listener) {
        this.mOnClickInfoListener = listener;
    }

    @NonNull
    @Override
    public edu.ub.pis.app.view.ui.trainers.TrainerCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate crea una view genèrica definida pel layout que l'hi passem (l'user_card_layout)
        View view = inflater.inflate(R.layout.trainer_card_layout, parent, false);

        // La classe ViewHolder farà de pont entre la classe User del model i la view (UserCard).
        return new edu.ub.pis.app.view.ui.trainers.TrainerCardAdapter.ViewHolder(view);
    }

    /* Mètode cridat per cada ViewHolder de la RecyclerView */
    @Override
    public void onBindViewHolder(@NonNull edu.ub.pis.app.view.ui.trainers.TrainerCardAdapter.ViewHolder holder, int position) {
        // El ViewHolder té el mètode que s'encarrega de llegir els atributs del User (1r parametre),
        // i assignar-los a les variables del ViewHolder.
        // Qualsevol listener que volguem posar a un item, ha d'entrar com a paràmetre extra (2n).
        holder.bind(mTrainers.get(position), this.mOnClickInfoListener, context);

    }

    /**
     * Retorna el número d'elements a la llista.
     * @return
     */
    @Override
    public int getItemCount() {
        return mTrainers.size();
    }

    /**
     * Mètode que seteja de nou la llista d'usuaris si s'hi han fet canvis de manera externa.
     * @param trainers
     */
    public void setRoutines(ArrayList<User> trainers) {
        this.mTrainers = trainers; // no recicla/repinta res
    }

    /**
     * Mètode que repinta la RecyclerView sencera.
     */
    public void updateTrainers(ArrayList<User> trainers) {
        mTrainers.clear();
        mTrainers.addAll(trainers);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTrainerName;
        //private final ImageView mTrainerPicture;
        private final TextView mTrainerMail;
        private final ImageView mInfoPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTrainerName = itemView.findViewById(R.id.trainer_name);
            mTrainerMail = itemView.findViewById(R.id.trainer_mail);
            mInfoPicture = itemView.findViewById(R.id.info_icon);
        }

        public void bind(final User trainer, edu.ub.pis.app.view.ui.trainers.TrainerCardAdapter.OnClickInfoListener Infolistener, Context context) {
            mTrainerName.setText(trainer.getName());
            String documentSnapshotKey = trainer.getId();
            String[] parts = documentSnapshotKey.split("/"); // Dividir la cadena en dos partes
            String email_aux = parts[1];
            String[] parts2 = email_aux.split(","); // Dividir la cadena en dos partes
            String email = parts2[0];
            mTrainerMail.setText(email);

            // Seteja el listener onClick del botó d'amagar (hide), que alhora
            // cridi el mètode OnClickHide que implementen els nostres propis
            // listeners de tipus OnClickHideListener.
            mInfoPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Inflar el layout del popup
                    View popupView = LayoutInflater.from(context).inflate(R.layout.popup_info_trainers_layout, null);

                    // Obtener referencias a los elementos del layout del popup
                    TextView emailTextView = popupView.findViewById(R.id.popup_email_text);
                    TextView nameTextView = popupView.findViewById(R.id.popup_name_text);
                    TextView phoneTextView = popupView.findViewById(R.id.popup_phone_text);
                    TextView experienceTextView = popupView.findViewById(R.id.popup_experience_text);
                    TextView priceTextView = popupView.findViewById(R.id.popup_experience_text);
                    //Button closeButton = popupView.findViewById(R.id.popup_close_button);

                    // Mostrar la información del entrenador en los elementos del layout del popup
                    String documentSnapshotKey = trainer.getId();
                    String[] parts = documentSnapshotKey.split("/"); // Dividir la cadena en dos partes
                    String email_aux = parts[1];
                    String[] parts2 = email_aux.split(","); // Dividir la cadena en dos partes
                    String email = parts2[0];
                    mTrainerMail.setText(email);
                    emailTextView.setText(email);
                    nameTextView.setText(trainer.getName());
                    phoneTextView.setText(trainer.getContactPhoneNumber());
                    experienceTextView.setText(trainer.getDescription());
                    priceTextView.setText(trainer.getPrice());

                    // Crear un AlertDialog que muestre el layout inflado
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(popupView);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    // Cerrar el popup al hacer clic en el botón "Cerrar"
                    /*closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });*/
                    /*Log.d("Missatge3", "He escoltat el boto");
                    // Crear la vista del Pop-up y la RecyclerView
                    View popupView = LayoutInflater.from(context).inflate(R.layout.popup_info_trainers_layout, null);

                    // Crear un objeto PopupWindow
                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                    // Mostrar el Pop-up en la posición del botón de información
                    int[] location = new int[2];
                    mInfoPicture.getLocationOnScreen(location);
                    popupWindow.showAtLocation(mInfoPicture, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());*/

                }
            });
        }
    }
}