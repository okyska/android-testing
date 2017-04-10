package io.mattcarroll.androidtesting.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;

/**
 * Collect info on user's interests.
 */
public class CollectInterestsFragment extends Fragment {

    @NonNull
    public static CollectInterestsFragment newInstance() {
        return new CollectInterestsFragment();
    }

    private InterestsListsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect_interests, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new InterestsListsAdapter();
        ListView listView = (ListView) view.findViewById(R.id.listview_interests);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setChecked(position, !adapter.isChecked(position));
            }
        });

        view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bus.getBus().post(new NextScreenRequestedEvent());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update ActionBar title for this screen.
        getActivity().setTitle("Sign Up - Interests");
    }
}
