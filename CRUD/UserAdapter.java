package com.jaydip.crud2;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    ArrayList<UserItem> userItemArrayList;
    DatabaseReference databaseReference;

    public UserAdapter(Context context, ArrayList<UserItem> userItemArrayList) {
        this.context = context;
        this.userItemArrayList = userItemArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

        UserItem userItem = userItemArrayList.get(position);

        holder.name.setText("Name : "+userItem.getName());
        holder.email.setText("Email : "+userItem.getEmail());
        holder.country.setText("Country : "+userItem.getCountry());

        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
                viewDialogAdd.showDialog(context, userItem.getId(), userItem.getName(), userItem.getEmail(), userItem.getCountry());
            }
        });
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialogConfirmDelete viewDialogConfirmDelete = new ViewDialogConfirmDelete();
                viewDialogConfirmDelete.showDialog(context, userItem.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return userItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,email,country;
        Button buttonDelete,buttonUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.userName);
            email = itemView.findViewById(R.id.userEmail);
            country = itemView.findViewById(R.id.userCountry);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);

        }
    }

    public class ViewDialogAdd{
        public void showDialog(Context context,String id,String name,String email,String country){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_add_new_user);

            EditText textName =  dialog.findViewById(R.id.editName);
            EditText textEmail =  dialog.findViewById(R.id.editEmail);
            EditText textCountry =  dialog.findViewById(R.id.editCountry);

            textName.setText(name);
            textEmail.setText(email);
            textCountry.setText(country);

            Button buttonUpdate = dialog.findViewById(R.id.buttonAddUser);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonUpdate.setText("Update");
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newName = textName.getText().toString();
                    String newEmail = textEmail.getText().toString();
                    String newCountry = textCountry.getText().toString();
                    if (name.isEmpty() || email.isEmpty() || country.isEmpty()){
                        Toast.makeText(context, "please enter all data..", Toast.LENGTH_SHORT).show();
                    }else{
                        if (newName.equals(name) && newEmail.equals(email) && newCountry.equals(country)){
                            Toast.makeText(context, "You don't change anything..", Toast.LENGTH_SHORT).show();
                        }else{
                            databaseReference.child("USERS").child(id).setValue(new UserItem(id,newName,newEmail,newCountry));
                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }

    public class ViewDialogConfirmDelete{
        public void showDialog(Context context,String id){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.view_dialog_confirm_delete);

            Button buttonDelete = dialog.findViewById(R.id.buttonDelete);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        databaseReference.child("USERS").child(id).removeValue();
                        Toast.makeText(context, "User Delete Successfully..", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }
}
