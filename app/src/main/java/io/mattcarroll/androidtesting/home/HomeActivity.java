package io.mattcarroll.androidtesting.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.accounts.ManageAccountsActivity;
import io.mattcarroll.androidtesting.creditcardanalysis.CreditCardAnalysisFragment;
import io.mattcarroll.androidtesting.overview.AccountsOverviewFragment;
import io.mattcarroll.androidtesting.spending.MonthlySpendingFragment;
import io.mattcarroll.androidtesting.login.LoginActivity;
import io.mattcarroll.androidtesting.usersession.UserSession;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    private static final int REQUEST_CODE_LOGIN = 1000;

    private final SparseArray<Fragment> navItemToFragmentMap = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initNavItemMap();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_manage_accounts);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchManageAccountScreen();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        @IdRes int defaultNavItem = getDefaultNavItem();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(defaultNavItem);
        navigationView.setNavigationItemSelectedListener(this);

        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.framelayout_container, navItemToFragmentMap.get(defaultNavItem))
                    .commit();
        }
    }

    private void launchManageAccountScreen() {
        Intent manageAccountIntent = new Intent(this, ManageAccountsActivity.class);
        startActivity(manageAccountIntent);
    }

    private void initNavItemMap() {
        navItemToFragmentMap.put(R.id.nav_overview, AccountsOverviewFragment.newInstance());
        navItemToFragmentMap.put(R.id.nav_credit_card_analysis, CreditCardAnalysisFragment.newInstance());
        navItemToFragmentMap.put(R.id.nav_monthly_spending, MonthlySpendingFragment.newInstance());
    }

    @IdRes
    private int getDefaultNavItem() {
        return R.id.nav_overview;
    }

    private void switchToFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout_container, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!UserSession.getInstance().isLoggedIn()) {
            Log.d(TAG, "User is not logged in. Launching LoginActivity.");
            launchLoginScreen();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (RESULT_CANCELED == resultCode) {
                    // User chose not to login.
                    finish();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void launchLoginScreen() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, REQUEST_CODE_LOGIN);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment selectedFragment = navItemToFragmentMap.get(id);
        if (selectedFragment != null) {
            switchToFragment(selectedFragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
