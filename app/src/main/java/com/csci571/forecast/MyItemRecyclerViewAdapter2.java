package com.csci571.forecast;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csci571.forecast.ItemFragment2.OnListFragmentInteractionListener;
import com.csci571.forecast.dummy.Weekly;

import java.util.List;

public class MyItemRecyclerViewAdapter2 extends RecyclerView.Adapter<MyItemRecyclerViewAdapter2.ViewHolder> {

    private final List<Weekly> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter2(List<Weekly> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.s_date.setText(mValues.get(position).date);
        holder.s_icon.setImageResource(Sharable.getIconResourceId(mValues.get(position).icon));
        holder.smi_temp.setText(mValues.get(position).minimumTemperature);
        holder.smx_temp.setText(mValues.get(position).maximumTemperature);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView s_date;
        public final ImageView s_icon;
        public final TextView smi_temp;
        public final TextView smx_temp;
        public Weekly mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            s_date = (TextView) view.findViewById(R.id.s_date);
            smi_temp = (TextView) view.findViewById(R.id.smi_temp);
            smx_temp = (TextView) view.findViewById(R.id.smx_temp);
            s_icon = (ImageView) view.findViewById(R.id.s_icon);
        }
    }
}
