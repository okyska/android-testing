package io.mattcarroll.androidtesting.signup;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

/**
 * List Adapter that offers a list of personal interests that can be selected.
 */
class InterestsListsAdapter extends BaseAdapter {

    private final String[] interests = new String[] {
            "Snowboarding",
            "Chess",
            "Programming",
            "Graphic Design",
            "Football",
            "Basketball",
            "Soccer",
            "Espresso Testing",
            "Alcohol",
            "Coffee",
            "Long walks on the beach",
            "Astronomy"
    };
    private final boolean[] isChecked;

    InterestsListsAdapter() {
        isChecked = new boolean[interests.length];
    }

    public boolean isChecked(int position) {
        return isChecked[position];
    }

    public void setChecked(int position, boolean isChecked) {
        this.isChecked[position] = isChecked;
        notifyDataSetChanged();
    }

    @NonNull
    public Set<String> getCheckedItems() {
        Set<String> checkedItems = new HashSet<>();

        for (int i = 0; i < interests.length; ++i) {
            if (this.isChecked[i]) {
                checkedItems.add(interests[i]);
            }
        }

        return checkedItems;
    }

    @Override
    public int getCount() {
        return interests.length;
    }

    @Override
    public String getItem(int position) {
        return interests[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        }

        ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position));
        ((CheckedTextView) convertView.findViewById(android.R.id.text1)).setChecked(isChecked(position));

        return convertView;
    }
}
