package com.example.eduar.brexpress.view.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.model.User;
import com.example.eduar.brexpress.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 03/04/2018.
 */

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ViewHolder> {

    private ClientListFragment mFragment;
    private List<User> mUsers;
    private List<Integer> mSelectedUsers;
    private boolean isAdmin;

    public ClientListAdapter(ClientListFragment fragment, List<User> users) {
        this.mFragment = fragment;
        this.mUsers = users;
        this.mSelectedUsers = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_user, parent, false);

        isAdmin = Utils.getUserType(mFragment.getContext());
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final User p = mUsers.get(position);

        holder.mNameTextView.setText(p.getName());
        holder.mEmailTextView.setText(p.getEmail());
        holder.mCpfTextView.setText(p.getCpf());
        holder.mFirstLetterTextView.setText(String.valueOf(p.getName().charAt(0)).toUpperCase());

        containerStyle(holder, mSelectedUsers.contains(p.getId()));
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

    public void notifyDataChanged(List<User> users) {
        mUsers = users;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mNameTextView;
        private TextView mEmailTextView;
        private TextView mCpfTextView;
        private TextView mFirstLetterTextView;
        private LinearLayout mContainer;

        private ViewHolder(View card) {
            super(card);
            mContainer = card.findViewById(R.id.container);
            mNameTextView = card.findViewById(R.id.user_name_text);
            mEmailTextView = card.findViewById(R.id.user_email_text);
            mCpfTextView = card.findViewById(R.id.user_cpf_text);
            mFirstLetterTextView = card.findViewById(R.id.first_letter_text);
        }
    }
}
