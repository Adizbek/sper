package com.github.adizbek.sper.ui.adapter.items;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.github.adizbek.sper.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class SimpleBigText extends AbstractItem<SimpleBigText, SimpleBigText.VH> {
    public String text;
    public long id;

    @NonNull
    @Override
    public SimpleBigText.VH getViewHolder(View v) {
        return new VH(v);
    }

    public SimpleBigText(String text, long id) {
        this.text = text;
        this.id = id;
    }

    @Override
    public long getIdentifier() {
        return id;
    }

    @Override
    public int getType() {
        return R.id.text1;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.simple_big_text;
    }

    public SimpleBigText setText(String text) {
        this.text = text;

        return this;
    }

    @Override
    public void bindView(VH holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.text.setText(text);
    }

    @Override
    public void unbindView(VH holder) {
        super.unbindView(holder);

        holder.text.setText(null);
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView text;

        public VH(View v) {
            super(v);

            text = (TextView) v;
        }

    }
}
