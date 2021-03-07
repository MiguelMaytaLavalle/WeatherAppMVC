package view;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface MVCView {
    public View getRootView();
    public void initComponents();
    public RecyclerView.Adapter getForecastListAdapter();
}
