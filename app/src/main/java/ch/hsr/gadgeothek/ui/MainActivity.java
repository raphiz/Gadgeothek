package ch.hsr.gadgeothek.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.ui.fragment.GadgetListFragment;
import ch.hsr.gadgeothek.ui.fragment.PseudoGadgetListFragment;

public class MainActivity extends AppCompatActivity implements GadgetListCallback {


    private TabLayout tabs;
    private ViewPager pager;

    // TODO: delete when implementing properly!
    public static final String[] pageTitles = {"Gadgets", "Ausleihen", "Reservationen"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Your Mama!");

        tabs = (TabLayout)findViewById(R.id.main_tabs);
        pager = (ViewPager) findViewById(R.id.main_pager);

        // Set the toolbar to the same level as the action bar
        // looks ugly otherwise
        getSupportActionBar().setElevation(0f);

        // Coloring tabs
        tabs.setTabTextColors(ContextCompat.getColor(this, android.R.color.white),
                ContextCompat.getColor(this, R.color.colorAccent));
        tabs.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        CustomAdapter adapter = new CustomAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);


        tabs.setupWithViewPager(pager);
    }

    // Menu icons are inflated just as they were with actionbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onGadgetClicked(Gadget gadget) {
        // TODO: Open details view here
        Log.d("LOG", "You clicked on: " + gadget.getName());
    }

    public static class CustomAdapter extends FragmentPagerAdapter {

        public CustomAdapter (FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            // TODO: Pass List of Gadgets to fragment
            return GadgetListFragment.getInstance(pageTitles[position], new ArrayList<Gadget>());
        }

        @Override
        public int getCount() {
            return pageTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position){
            // TODO: Localize!
            return pageTitles[position];
        }
    }}
