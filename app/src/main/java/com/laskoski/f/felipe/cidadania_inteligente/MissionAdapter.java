package com.laskoski.f.felipe.cidadania_inteligente;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.laskoski.f.felipe.cidadania_inteligente.fileManagement.ImageDownloader;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
//import com.laskoski.f.felipe.cidadania_inteligente.fileManagement.ImageDownloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
            Bitmap iconFromDB = getImageFromDB(ImageDownloader.SERVER_ROOT_URL+currentItem.get_id());
            if(iconFromDB != null) {
                missionIcon.setImageBitmap(iconFromDB);
                missionIcon.setVisibility(View.VISIBLE);
            }
            else
                missionIcon.setVisibility(View.INVISIBLE);
        }


        return listItemView;
    }

    public Bitmap getImageFromDB(String url) {
        ImageDownloader imageDownloader = new ImageDownloader();
        try {
            return imageDownloader.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
