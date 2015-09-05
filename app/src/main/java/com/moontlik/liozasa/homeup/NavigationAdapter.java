package com.moontlik.liozasa.homeup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moontlik.liozasa.homeup.Activities.CustomizedActivity;

import java.util.Collections;
import java.util.List;

/**
 * Created by liozasa on 7/29/15.
 */
public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    List<NavigationMenu> menu = Collections.emptyList();
    Context context;

    public NavigationAdapter(Context context, List<NavigationMenu> menu){
        inflater = LayoutInflater.from(context);
        this.menu = menu;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavigationMenu current = menu.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
        }

        @Override
        public void onClick(View v) {
          //  Toast.makeText(context, title.getText(), Toast.LENGTH_SHORT).show();

            //if the first title is equal to create_an_even then ->
            if (title.getText().toString().equals(inflater.getContext().getString(R.string.create_an_event))){
                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                createAnEventDialog();
            }
        }

        private void createAnEventDialog(){
            new AlertDialog.Builder(inflater.getContext())
                    .setTitle("create an event")
                    .setMessage("What kind of event would you like to create ? ")
                    .setPositiveButton("customized", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Goto customized activity
                            Intent intent = new Intent(inflater.getContext(), CustomizedActivity.class);
                            inflater.getContext().startActivity(intent);
                        }
                    })

                    .setNeutralButton("Coacher template", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
             .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                     // do nothing
                 }
             })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
    }
}
