package com.dpilaloa.upsipteeo.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dpilaloa.upsipteeo.R;

public class ArraySpinnerAdapter extends ArrayAdapter<CharSequence> {

    private final String rol;
    private final int pos;

    public ArraySpinnerAdapter(@NonNull Context context, int resource, @NonNull CharSequence[] objects, String rol, int pos) {
        super(context, resource, objects);
        this.rol = rol;
        this.pos = pos;
    }

    @Override
    public boolean isEnabled(int position) {
        return rol.equals(getContext().getString(R.string.admin_one)) || position != pos;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setTextColor((rol.equals(getContext().getString(R.string.admin_one)) || position != pos) ? Color.BLACK : Color.GRAY);
        return view;
    }

}
