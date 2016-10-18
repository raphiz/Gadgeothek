package ch.hsr.gadgeothek.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.constant.Tab;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.domain.Loan;
import ch.hsr.gadgeothek.domain.Reservation;
import ch.hsr.gadgeothek.service.LibraryServiceCallback;
import ch.hsr.gadgeothek.service.LibraryService;
import ch.hsr.gadgeothek.service.SimpleLibraryServiceCallback;
import ch.hsr.gadgeothek.ui.fragment.GadgetListFragment;

public class MainActivity extends AppCompatActivity implements GadgetListCallback {


    private boolean failed = false;
    private List<Loan> loans;
    private List<Reservation> reservations;
    private List<Gadget> gadgets;

    private GadgetItemAdapter gadgetAdapter;
    private GadgetItemAdapter reservationsAdapter;
    private GadgetItemAdapter loansAdapter;

    private GadgetListFragment gadgetsFragment;
    private GadgetListFragment reservationsFragment;
    private GadgetListFragment loansFragment;

    private Snackbar snackbar;

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

        // Setup Tabs
        TabLayout tabs = (TabLayout) findViewById(R.id.main_tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.main_pager);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());

        // Setup & add fragments
        gadgetsFragment = GadgetListFragment.getInstance(Tab.GADGETS);
        loansFragment = GadgetListFragment.getInstance(Tab.LOANS);
        reservationsFragment = GadgetListFragment.getInstance(Tab.RESERVATIONS);

        adapter.addFragment(gadgetsFragment, getResources().getString(R.string.gadgets));
        adapter.addFragment(loansFragment, getResources().getString(R.string.loans));
        adapter.addFragment(reservationsFragment, getResources().getString(R.string.reservations));

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        loadData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu:
                LibraryService.logout(MainActivity.this, new SimpleLibraryServiceCallback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        MainActivity.this.startActivity(intent);
                    }

                    @Override
                    public void onError(String message) {
                        Snackbar.make(findViewById(R.id.activity_main), getResources().getString(R.string.logout_failed), Snackbar.LENGTH_LONG).show();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadData() {
        LibraryService.load(new LibraryServiceCallback() {


            @Override
            public void onCompletion(List<Gadget> loadedGadgets, List<Loan> loadedLoans, List<Reservation> loadedReservations) {
                gadgets = loadedGadgets;
                loans = loadedLoans;
                reservations = loadedReservations;

                gadgetAdapter.setGadgetList(gadgets);
                gadgetAdapter.notifyDataSetChanged();

                List<Gadget> loanedGadgets = new ArrayList<Gadget>();
                for (Loan loan : loans){loanedGadgets.add(loan.getGadget());}
                loansAdapter.setGadgetList(loanedGadgets);
                loansAdapter.notifyDataSetChanged();

                List<Gadget> reservedGadgets = new ArrayList<Gadget>();
                for (Loan loan : loans){reservedGadgets .add(loan.getGadget());}
                reservationsAdapter.setGadgetList(reservedGadgets );
                reservationsAdapter.notifyDataSetChanged();

                gadgetsFragment.onDataRefreshed();
                loansFragment.onDataRefreshed();
                reservationsFragment.onDataRefreshed();

                // Hide Snackbar if visible
                if(snackbar != null && snackbar.isShown()){
                    snackbar.dismiss();;
                }


            }

            @Override
            public void onError(String message) {

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
        });
    }

    @Override
    public void onGadgetClicked(Gadget gadget) {
        Intent fragmentIntent = new Intent(this, GadgetDetailActivity.class);

        // Filter for active reservations
        Reservation reservation = null;
        for(Reservation current : reservations){
            if(gadget.equals(current.getGadget()) && !current.getFinished()){
                reservation = current;
                break;
            }
        }

        // Filter for active loans
        Loan loan = null;
        for(Loan current: loans){
            if(gadget.equals(current.getGadget()) && !current.isLent()){
                loan = current;
                break;
            }
        }

        fragmentIntent.putExtra(Constant.GADGET, gadget);
        fragmentIntent.putExtra(Constant.RESERVATION, reservation);
        fragmentIntent.putExtra(Constant.LOAN, loan);

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
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!LibraryService.keepMeLoggedIn() && LibraryService.isLoggedIn()) {
            LibraryService.logout(MainActivity.this, new SimpleLibraryServiceCallback<Boolean>() {
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
}
