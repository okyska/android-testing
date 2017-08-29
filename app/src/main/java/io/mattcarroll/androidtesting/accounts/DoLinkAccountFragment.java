package io.mattcarroll.androidtesting.accounts;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;

/**
 * Links to an account and displays a spinner while linking is in progress.
 */
public class DoLinkAccountFragment extends Fragment {

    private static final String ARG_ACCOUNT_CREDENTIALS = "link_account_form";
    private static final int LOADER_LINK_ACCOUNT = 1000;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            api = ((AccountsService.LocalBinder) service).getApi();
            initiateAccountLinking();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            api = null;
        }
    };

    private AccountsApi api;

    @NonNull
    public static DoLinkAccountFragment newInstance(@NonNull AccountCredentials accountCredentials) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACCOUNT_CREDENTIALS, accountCredentials);
        DoLinkAccountFragment frag = new DoLinkAccountFragment();
        frag.setArguments(args);
        return frag;
    }

    private AccountCredentials credentials;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bindAccountsService();
    }

    private void bindAccountsService() {
        Intent accountsServiceIntent = new Intent(getActivity(), AccountsService.class);
        getActivity().bindService(accountsServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        credentials = (AccountCredentials) args.getSerializable(ARG_ACCOUNT_CREDENTIALS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spinner, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update ActionBar title for this screen.
        getActivity().setTitle("Linking account...");
    }

    private void initiateAccountLinking() {
        getLoaderManager().initLoader(LOADER_LINK_ACCOUNT, null,
                new LoaderManager.LoaderCallbacks<LinkAccountLoader.Result>() {
                    @Override
                    public Loader<LinkAccountLoader.Result> onCreateLoader(int id, Bundle args) {
                        return new LinkAccountLoader(getContext(), api, credentials);
                    }

                    @Override
                    public void onLoadFinished(Loader<LinkAccountLoader.Result> loader,
                                               LinkAccountLoader.Result result) {
                        if (result.wasSuccessful()) {
                            onLinkAccountSucceeded();
                        } else {
                            onLinkAccountFailed();
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<LinkAccountLoader.Result> loader) {
                        // no op
                    }
                });
    }

    private void onLinkAccountSucceeded() {
        Bus.getBus().post(new LinkAccountSucceededEvent());
    }

    private void onLinkAccountFailed() {
        Bus.getBus().post(new LinkAccountFailedEvent());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unbindService(serviceConnection);
    }

}
