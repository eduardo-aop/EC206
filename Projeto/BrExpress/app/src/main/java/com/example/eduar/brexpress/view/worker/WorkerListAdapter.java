package com.example.eduar.brexpress.view.worker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.model.Worker;
import com.example.eduar.brexpress.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 03/04/2018.
 */

public class WorkerListAdapter extends RecyclerView.Adapter<WorkerListAdapter.ViewHolder> {

    private WorkerListFragment mFragment;
    private List<Worker> mWorkers;
    private List<Integer> mSelectedWorkers;
    private boolean isAdmin;

    public WorkerListAdapter(WorkerListFragment fragment, List<Worker> workers) {
        this.mFragment = fragment;
        this.mWorkers = workers;
        this.mSelectedWorkers = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_worker, parent, false);

        isAdmin = Utils.getUserType(mFragment.getContext());
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Worker p = mWorkers.get(position);

        holder.mNameTextView.setText(p.getName());
        holder.mFunctionTextView.setText(p.getFunction());
        holder.mSectorTextView.setText(p.getSector());
        holder.mFirstLetterTextView.setText(String.valueOf(p.getName().charAt(0)).toUpperCase());

        containerStyle(holder, mSelectedWorkers.contains(p.getId()));
    }

    private void containerStyle(ViewHolder holder, boolean selected) {
        if (selected) {
            holder.mContainer.setBackgroundColor(mFragment.getActivity().getResources()
                    .getColor(android.R.color.holo_blue_dark));
            holder.mContainer.setAlpha(0.5f);
        } else {
            holder.mContainer.setBackgroundColor(mFragment.getActivity().getResources()
                    .getColor(android.R.color.transparent));
            holder.mContainer.setAlpha(1.0f);
        }
    }

    public void notifyDataChanged(List<Worker> users) {
        mWorkers = users;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mWorkers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mNameTextView;
        private TextView mFunctionTextView;
        private TextView mSectorTextView;
        private TextView mFirstLetterTextView;
        private LinearLayout mContainer;

        private ViewHolder(View card) {
            super(card);
            mContainer = card.findViewById(R.id.container);
            mNameTextView = card.findViewById(R.id.worker_name_text);
            mFunctionTextView = card.findViewById(R.id.worker_function_text);
            mSectorTextView = card.findViewById(R.id.worker_sector_text);
            mFirstLetterTextView = card.findViewById(R.id.first_letter_text);
        }
    }
}