package io.mattcarroll.androidtesting.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.mattcarroll.androidtesting.R;

/**
 * Collect personal info about the user.
 */
public class CollectPersonalInfoFragment extends Fragment {

    @NonNull
    public static CollectPersonalInfoFragment newInstance() {
        return new CollectPersonalInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect_personal_info, container, false);
    }
}
