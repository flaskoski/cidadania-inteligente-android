package com.laskoski.f.felipe.cidadania_inteligente.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.laskoski.f.felipe.cidadania_inteligente.R;
import com.laskoski.f.felipe.cidadania_inteligente.model.AbstractTask;

import java.util.List;

/**
 * Created by Felipe on 11/25/2017.
 */

public class TaskAdapter extends ArrayAdapter<AbstractTask> {

    public TaskAdapter(@NonNull Context context, @NonNull List<AbstractTask> tasks) {
        super(context, 0, tasks);
    }

    /**
     * Connect MissionItem object with the listView
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //setup
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        AbstractTask currentItem = getItem(position);
        //set title
        TextView title = (TextView) listItemView.findViewById(R.id.title);
        title.setText(currentItem.getType() + " - " +  currentItem.getTitle(    ));

        //set checked image
        ImageView checked = (ImageView) listItemView.findViewById(R.id.taskStatus);
        if(currentItem.isFinished()) {
            if(currentItem.isCompleted())
                checked.setImageResource(R.drawable.ic_check_box_black_24dp);
            else checked.setImageResource(R.drawable.baseline_clear_24);
            listItemView.setClickable(false);
            checked.setClickable(false);
            title.setClickable(false);
        }
        else checked.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
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
