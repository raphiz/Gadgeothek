package ch.hsr.gadgeothek.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.constant.Tab;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.domain.Loan;
import ch.hsr.gadgeothek.domain.Reservation;
import ch.hsr.gadgeothek.service.Callback;
import ch.hsr.gadgeothek.service.LibraryService;
import ch.hsr.gadgeothek.ui.fragment.GadgetListFragment;

public class MainActivity extends AppCompatActivity implements GadgetListCallback {


    private boolean failed = false;
    private List<Loan> loans;
    private List<Reservation> reservations;
    private List<Gadget> gadgets;
    private GadgetItemAdapter gadgetAdapter;
    private GadgetItemAdapter reservationsAdapter;
    private GadgetItemAdapter loansAdapter;
    private Snackbar snackbar;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the toolbar to the same level as the action bar
        // looks ugly otherwise
        getSupportActionBar().setElevation(0f);

        // Setup ListView Adapters
        this.gadgetAdapter = new GadgetItemAdapter(this);
        this.reservationsAdapter = new GadgetItemAdapter(this);
        this.loansAdapter = new GadgetItemAdapter(this);

        loadData();

        // Setup Tabs
        TabLayout tabs = (TabLayout) findViewById(R.id.main_tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.main_pager);
        TabAdapter adapter = new TabAdapter(getResources(), getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshMenu:
                loadData();
                return true;
            case R.id.logoutMenu:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadData() {
        failed = false;
        loadingDialog = ProgressDialog.show(this, "Loading Gadgets", "Wait a sec...");
        LibraryService.getLoansForCustomer(new Callback<List<Loan>>() {
            @Override
            public void onCompletion(List<Loan> input) {
                loans = input;
                // TODO: Filter and update data
                if (reservations != null && gadgets != null){
                    loadingDialog.hide();
                }
            }

            @Override
            public void onError(String message) {
                handleDataLoadingError(message);
            }


        });
        LibraryService.getReservationsForCustomer(new Callback<List<Reservation>>() {
            @Override
            public void onCompletion(List<Reservation> input) {
                reservations = input;
                // TODO: Filter and update data
                if (loans != null && gadgets != null){
                    loadingDialog.hide();
                }
            }

            @Override
            public void onError(String message) {
                handleDataLoadingError(message);
            }
        });
        LibraryService.getGadgets(new Callback<List<Gadget>>() {
            @Override
            public void onCompletion(List<Gadget> input) {
                gadgets = input;
                updateAdapterData(gadgetAdapter, gadgets);
                if (reservations != null && loans != null){
                    loadingDialog.hide();
                }
            }

            @Override
            public void onError(String message) {
                handleDataLoadingError(message);
            }
        });
        // TODO: Update all Tabs - when done!
    }

    private void handleDataLoadingError(String message) {
        Log.d("LOG", message);
        if(failed) {
            return;
        }
        failed = true;
        loadingDialog.hide();
        snackbar = Snackbar
                .make(findViewById(R.id.activity_main), "Failed to load data..", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        loadData();
                    }
                });

        snackbar.show();
    }

    private void updateAdapterData(GadgetItemAdapter adapter, List<Gadget> gadgetList) {
        adapter.setGadgetList(gadgetList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGadgetClicked(Gadget gadget) {
        // TODO: Distinguish between phone and tablet
        Log.d("LOG", "You clicked on: " + gadget.getName());
        Intent fragmentIntent = new Intent(this, GadgetDetailActivity.class);
        Reservation reservation = null;
        Loan load = null;
        fragmentIntent.putExtra(Constant.GADGET, gadget);
        fragmentIntent.putExtra(Constant.RESERVATION, reservation);
        fragmentIntent.putExtra(Constant.LOAN, load);

        startActivity(fragmentIntent);
    }

    @Override
    public ArrayAdapter<Gadget> getAdapter(Tab tab) {
         switch (tab) {
            case GADGETS:
                return gadgetAdapter;
            case RESERVATIONS:
                return reservationsAdapter;
            case LOANS:
                return loansAdapter;
            default:
                return null;
        }
    }

    @Override
    public void onGadgetListRefresh(GadgetListFragment fragment) {
        //loadData(); // raises exception "not logged in"
        // TODO: Call onDataRefreshed() on fragment once loading is completed
        fragment.onDataRefreshed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!LibraryService.keepMeLoggedIn()) {
            LibraryService.logout(new Callback<Boolean>() {
                @Override
                public void onCompletion(Boolean input) {
                    Log.d("logout", "logout completed");
                }

                @Override
                public void onError(String message) {
                    Log.d("logout", "error during logout: " + message);
                }
            });
        }
    }

    public static class TabAdapter extends FragmentPagerAdapter {

        private final Resources resources;

        public TabAdapter(Resources resources, FragmentManager manager){
            super(manager);
            this.resources = resources;
        }

        @Override
        public Fragment getItem(int position) {
            // TODO: Pass List of Gadgets to fragment
            return GadgetListFragment.getInstance(Tab.values()[position]);
        }

        @Override
        public int getCount() {
            return Tab.values().length;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch(Tab.values()[position]){
                case GADGETS:
                    return resources.getString(R.string.gadgets);
                case RESERVATIONS:
                    return resources.getString(R.string.reservations);
                case LOANS:
                    return resources.getString(R.string.loans);
                default:
                    return "?";
            }
        }
    }

}
