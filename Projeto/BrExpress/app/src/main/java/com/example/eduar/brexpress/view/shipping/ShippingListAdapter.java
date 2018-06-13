package com.example.eduar.brexpress.view.shipping;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.model.Shipping;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 03/04/2018.
 */

public class ShippingListAdapter extends RecyclerView.Adapter<ShippingListAdapter.ViewHolder> {

    private ShippingListFragment mFragment;
    private List<Shipping> mShippingList;
    private List<Integer> mSelectedShipping;
    private boolean mIsAdmin;
    private boolean mIsEditing;

    public ShippingListAdapter(ShippingListFragment fragment, List<Shipping> shippingList) {
        this.mFragment = fragment;
        this.mShippingList = shippingList;
        this.mSelectedShipping = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_shipping, parent, false);

        mIsAdmin = Utils.getUserType(mFragment.getContext());
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Shipping s = mShippingList.get(position);

        holder.mNameTextView.setText(s.getName());
        holder.mCompanyTextView.setText(s.getCompany());

        String price = String.format(mFragment.getResources().getString(R.string.price), s.getPayment());
        holder.mPaymentTextView.setText(price);
        holder.mFirstLetterTextView.setText(String.valueOf(s.getName().charAt(0)).toUpperCase());

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsEditing) {
                    containerStyle(holder, selectProduct(s.getId()));
                } else {
                    Intent i = new Intent(mFragment.getActivity(), RegisterShippingActivity.class);
                    i.putExtra(Constants.IS_EDITING, true);
                    i.putExtra(Constants.SHIPPING_ID, s.getId());
                    mFragment.getActivity().startActivity(i);
                }
            }
        });

        if (mIsAdmin) {
            holder.mContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    containerStyle(holder, selectProduct(s.getId()));
                    return true;
                }
            });
        }

        containerStyle(holder, mSelectedShipping.contains(s.getId()));
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

    private boolean selectProduct(Integer id) {
        boolean selected;
        if (mSelectedShipping.contains(id)) {
            mSelectedShipping.remove(id);
            selected = false;
        } else {
            mSelectedShipping.add(id);
            selected = true;
        }

        mIsEditing = !mSelectedShipping.isEmpty();

        mFragment.getActivity().invalidateOptionsMenu();
        return selected;
    }

    public boolean isEditing() {
        return mIsEditing;
    }

    public void notifyDataChanged(List<Shipping> shippingList) {
        mShippingList = shippingList;
        this.notifyDataSetChanged();
    }

    public List<Integer> getSelectedShipping() {
        return this.mSelectedShipping;
    }

    public void setSelectedWorkers(List<Integer> list) {
        this.mSelectedShipping = list;
        if (mSelectedShipping.isEmpty()) {
            this.mIsEditing = false;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mShippingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mNameTextView;
        private TextView mCompanyTextView;
        private TextView mPaymentTextView;
        private TextView mFirstLetterTextView;
        private LinearLayout mContainer;

        private ViewHolder(View card) {
            super(card);
            mContainer = card.findViewById(R.id.container);
            mNameTextView = card.findViewById(R.id.shipping_name_text);
            mCompanyTextView = card.findViewById(R.id.shipping_company_text);
            mPaymentTextView = card.findViewById(R.id.shipping_payment_text);
            mFirstLetterTextView = card.findViewById(R.id.first_letter_text);
        }
    }
}
