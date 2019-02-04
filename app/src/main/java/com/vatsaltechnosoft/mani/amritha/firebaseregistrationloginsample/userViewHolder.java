package com.vatsaltechnosoft.mani.amritha.firebaseregistrationloginsample;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Amritha on 7/30/18.
 */
public class userViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ItemClickListener itemClickListener;
    AppCompatTextView textViewFullName;
    AppCompatTextView textViewRadioButton;
    AppCompatTextView textViewCheckBox;
    AppCompatTextView textViewPhoneNumber;

    public userViewHolder(View view) {
        super(view);

        textViewFullName = view.findViewById(R.id.textViewName);
        textViewRadioButton = view.findViewById(R.id.textViewGender);
        textViewCheckBox = view.findViewById(R.id.textViewHobbies);
        textViewPhoneNumber = view.findViewById(R.id.textViewPhoneNumber);

        view.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}