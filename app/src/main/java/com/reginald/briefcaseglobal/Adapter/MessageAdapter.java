package com.reginald.briefcaseglobal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.Model.MessageModel;
import com.reginald.briefcaseglobal.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    private Context context;
    private List<MessageModel> list;
    public MessageAdapter (Context context,List<MessageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_message_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModel model = list.get(position);
        holder.subject.setText(model.getStrSubject());
        holder.message.setText(model.getMessagestofollow());
        holder.fromUserName.setText(model.getFromUser());
        holder.date.setText(model.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView subject,fromUserName,message,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.subject);
            fromUserName = itemView.findViewById(R.id.fromUserName);
            message = itemView.findViewById(R.id.messageHere);
            date = itemView.findViewById(R.id.date);
        }
    }
}
