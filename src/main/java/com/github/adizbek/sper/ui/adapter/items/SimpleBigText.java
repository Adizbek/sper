package com.github.adizbek.sper.ui.adapter.items;

import android.support.annotation.NonNull;
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
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.simple_big_text;
    }

    public SimpleBigText setText(String text) {
        this.text = text;

        return this;
    }

    public class VH extends FastAdapter.ViewHolder<SimpleBigText> {
        TextView text;

        public VH(View v) {
            super(v);

            text = (TextView) v;
        }

        @Override
        public void bindView(SimpleBigText item, List<Object> payloads) {
            text.setText(item.text);
        }

        @Override
        public void unbindView(SimpleBigText item) {
            text.setText(null);
        }
    }
}
