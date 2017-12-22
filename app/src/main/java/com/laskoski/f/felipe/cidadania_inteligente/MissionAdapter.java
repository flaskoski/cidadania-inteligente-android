package com.laskoski.f.felipe.cidadania_inteligente;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;

import java.util.List;

/**
 * Created by Felipe on 11/25/2017.
 */

public class MissionAdapter extends ArrayAdapter<MissionItem>{
    public MissionAdapter(@NonNull Context context, @NonNull List<MissionItem> missionItems) {
        super(context, 0, missionItems);
    }

    /**
     * Connect MissionItem object with the listView
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.mission_item, parent, false);

        MissionItem currentItem = getItem(position);

        TextView missionTitle = (TextView) listItemView.findViewById(R.id.missionTitle);
        TextView missionDescription = (TextView) listItemView.findViewById(R.id.missionDescription);
        ImageView missionIcon = (ImageView) listItemView.findViewById(R.id.missionIcon);

        missionTitle.setText(currentItem.getMissionName());

        //if item has associated image
        if(currentItem.hasImage()) {
            missionIcon.setImageResource(currentItem.getMissionIconId());
            missionIcon.setVisibility(View.VISIBLE);
        }
        else{
            missionIcon.setVisibility(View.INVISIBLE);
        }


        return listItemView;
    }
}
