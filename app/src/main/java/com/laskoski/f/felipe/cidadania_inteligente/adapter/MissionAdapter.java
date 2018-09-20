package com.laskoski.f.felipe.cidadania_inteligente.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.laskoski.f.felipe.cidadania_inteligente.R;
import com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks.ImageDownloader;
import com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks.MissionAsyncTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
//import com.laskoski.f.felipe.cidadania_inteligente.fileManagement.ImageDownloader;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Felipe on 11/25/2017.
 */

public class MissionAdapter extends ArrayAdapter<MissionItem> implements Filterable{
    List<MissionItem> mOriginalValues;
    List<MissionItem> arrayList;
    private RequestQueue requestQueue;
    private MissionAsyncTask missionAsyncTask;

    public void setRequestQueue(RequestQueue requestQueue, MissionAsyncTask missionAsyncTask) {
        this.requestQueue = requestQueue;
        this.missionAsyncTask = missionAsyncTask;
    }

    public MissionAdapter(@NonNull Context context, @NonNull List<MissionItem> missionItems) {
        super(context, 0, missionItems);
        arrayList = missionItems;
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
        final ImageView missionIcon = (ImageView) listItemView.findViewById(R.id.missionIcon);

        missionTitle.setText(currentItem.getMissionName());

        ImageDownloader imageDownloader = new ImageDownloader(requestQueue);
        Response.ErrorListener responseError = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                missionIcon.setImageResource(R.mipmap.image_not_found);
            }
        };
        try {
            missionAsyncTask.requestImageFromDB(MissionAsyncTask.IMAGE_TYPE_ICON , requestQueue, currentItem.get_id(), missionIcon, responseError);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //missionIcon.setVisibility(View.VISIBLE);


        return listItemView;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                arrayList = (List<MissionItem>) results.values; // has the filtered values
                clear();
                addAll(arrayList);

                if (arrayList.size() > 0)
                    notifyDataSetChanged();
                else
                    notifyDataSetInvalidated();
                //notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<MissionItem> FilteredArrList = new ArrayList<MissionItem>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<MissionItem>(arrayList); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        MissionItem data = mOriginalValues.get(i);
                        if (Integer.valueOf(constraint.toString()) == data.getStatus()) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
