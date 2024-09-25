package com.dpilaloa.upsipteeo.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dpilaloa.upsipteeo.DetUserView;
import com.dpilaloa.upsipteeo.Holders.UserHolder;
import com.dpilaloa.upsipteeo.Objects.User;
import com.dpilaloa.upsipteeo.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {

    public List<User> userList = new ArrayList<>();
    final Context context;

    public UserAdapter(Context context) {
        this.context = context;
    }

    public void add (User user ){
        userList.add(user);
        notifyItemInserted(userList.size());
    }

    public void clear (){
        userList.clear();
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_user,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

        String ced = " C.I: " + userList.get(position).ced;
        holder.cardCed.setText(ced);
        holder.cardName.setText(userList.get(position).name);
        holder.cardCanton.setText(userList.get(position).canton);
        holder.cardPhone.setText(userList.get(position).phone);
        holder.cardRol.setText(userList.get(position).rol);

        if(!TextUtils.isEmpty(userList.get(position).photo)) {
            Glide.with(context).load(userList.get(position).photo).centerCrop().into(holder.cardPhoto);
        }else{
            Glide.with(context).load(R.drawable.profile).fitCenter().into(holder.cardPhoto);
        }

        holder.cardViewUser.setOnClickListener(view -> {
            Intent i = new Intent(context, DetUserView.class).putExtra("uid",userList.get(position).uid);
            context.startActivity(i);
        });

        holder.cardViewUser.setOnLongClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            String text = userList.get(position).phone;
            ClipData clip = ClipData.newPlainText("celular",  text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context,"Celular copiado al Portapapeles",Toast.LENGTH_SHORT).show();
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
