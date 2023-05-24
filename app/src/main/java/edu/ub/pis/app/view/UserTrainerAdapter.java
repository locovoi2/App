package edu.ub.pis.app.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.User;

public class UserTrainerAdapter extends RecyclerView.Adapter<UserTrainerAdapter.ViewHolder> {

    private ArrayList<User> mUserList;


    private Context mContext;

    public UserTrainerAdapter(ArrayList<User> itemList,Context context) {
        this.mUserList = itemList;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUserList.get(position);
        String documentSnapshotKey = user.getId();

        String[] parts = documentSnapshotKey.split("/"); // Dividir la cadena en dos partes
        String email_aux = parts[1];
        String[] parts2 = email_aux.split(","); // Dividir la cadena en dos partes
        String email = parts2[0];
        holder.mailTextView.setText(email);
        holder.nameTextView.setText(user.getName());
        holder.surnameTextView.setText(user.getSurname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Acción a realizar al hacer clic en el elemento
                // Por ejemplo, iniciar una nueva actividad y pasar datos
                Intent intent = new Intent(view.getContext(), TrainerHomeActivity.class);
                intent.putExtra("user", user);
                view.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.inflate(R.menu.menu_popup_user); // El archivo XML del menú emergente
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Maneja los clics en los elementos del menú emergente
                        switch (item.getItemId()) {
                            case R.id.menu_delete_option:
                                ((UsersActivity)mContext).deleteUser(user);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView mailTextView;
        TextView nameTextView;
        TextView surnameTextView;

        ImageView trashIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            mailTextView = itemView.findViewById(R.id.mailTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            surnameTextView = itemView.findViewById(R.id.surnameTextView);
        }
    }
    public void updateUsers(ArrayList<User> users) {
        mUserList.clear();
        mUserList.addAll(users);
        notifyDataSetChanged();
    }

}