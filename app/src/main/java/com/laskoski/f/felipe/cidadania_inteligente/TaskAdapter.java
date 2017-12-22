package com.laskoski.f.felipe.cidadania_inteligente;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.laskoski.f.felipe.cidadania_inteligente.model.QuestionTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.Task;

import java.util.List;

/**
 * Created by Felipe on 11/25/2017.
 */

public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(@NonNull Context context, @NonNull List<Task> tasks) {
        super(context, 0, tasks);
    }

    /**
     * Connect MissionItem object with the listView
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);

        Task currentItem = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.title);
        title.setText(currentItem.getType() + " - " +  currentItem.getTitle());


        //ImageView icon = (ImageView) listItemView.findViewById(R.id.missionIcon);
        //if item has associated image
//        if(currentItem.hasImage()) {
//            missionIcon.setImageResource(currentItem.getMissionIconId());
//            missionIcon.setVisibility(View.VISIBLE);
//        }
//        else{
//            missionIcon.setVisibility(View.INVISIBLE);
//        }

        return listItemView;
    }

}
