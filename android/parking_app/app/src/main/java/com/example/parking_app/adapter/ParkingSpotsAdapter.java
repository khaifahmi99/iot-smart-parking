package com.example.parking_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.example.parking_app.R;

import java.util.List;

public class ParkingSpotsAdapter extends RecyclerView.Adapter<ParkingSpotsAdapter.ViewHolder> {

    private List<Document> parkingSpots;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public ViewHolder(View aView) {
            super(aView);
            textView =  aView.findViewById(R.id.text_parking_spot);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ParkingSpotsAdapter(Context aContext, List<Document> aParkingSpots) {
        context = aContext;
        parkingSpots = aParkingSpots;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ParkingSpotsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.layout_parking_spot, parent, false);


        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String text = "Parking id: " + parkingSpots.get(position).get("parking_spot_id").convertToAttributeValue();
        holder.textView.setText(text);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return parkingSpots.size();
    }
}
