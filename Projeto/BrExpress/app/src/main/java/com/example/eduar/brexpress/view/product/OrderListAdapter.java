package com.example.eduar.brexpress.view.product;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.model.Order;
import com.example.eduar.brexpress.utils.Utils;

import java.util.List;

/**
 * Created by eduar on 03/04/2018.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private OrderListFragment mFragment;
    private List<Order> mOrders;
    private boolean isAdmin;

    public OrderListAdapter(OrderListFragment fragment, List<Order> orders) {
        this.mFragment = fragment;
        this.mOrders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_order, parent, false);

        isAdmin = Utils.getUserType(mFragment.getContext());
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Order order = mOrders.get(position);

        holder.mNameTextView.setText(order.getName());
        holder.mStateTextView.setText(Utils.getStatusText(mFragment.getContext(), order.getPurchaseStatus()));

        holder.mPurchaseDateTextView.setText(String.format(mFragment.getResources().getString(R.string.purchase_date),
                Utils.setFormatDate(order.getPurchaseDate())));
        holder.mArrivalDateTextView.setText(String.format(mFragment.getResources().getString(R.string.arrival_date),
                Utils.setFormatDate(order.getArrivalDate())));

        holder.mImageView.setImageBitmap(null);
        if (order.getImage() != null) {
            holder.mImageView.setImageBitmap(order.getImage());
        } else {
            holder.mImageView.setImageDrawable(mFragment.getResources().getDrawable(R.drawable.no_image_available));
        }
    }

    public void notifyDataChanged(List<Order> order) {
        mOrders = order;
        this.notifyDataSetChanged();
    }

    public void notifySpecificItemChanged(final int pos) {
        Handler mainHandler = new Handler(mFragment.getActivity().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                OrderListAdapter.this.notifyItemChanged(pos);
            }
        };
        mainHandler.post(myRunnable);
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mNameTextView;
        private TextView mStateTextView;
        private TextView mArrivalDateTextView;
        private TextView mPurchaseDateTextView;
        private ImageView mImageView;

        private ViewHolder(View card) {
            super(card);
            mNameTextView = card.findViewById(R.id.product_name_text);
            mStateTextView = card.findViewById(R.id.product_state_text);
            mArrivalDateTextView = card.findViewById(R.id.arrival_date_text);
            mPurchaseDateTextView = card.findViewById(R.id.purchase_date_text);
            mImageView = card.findViewById(R.id.product_image);
        }
    }
}
