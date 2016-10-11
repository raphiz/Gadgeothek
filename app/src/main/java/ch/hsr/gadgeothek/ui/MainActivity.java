package ch.hsr.gadgeothek.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.ui.fragment.GadgetListFragment;

public class MainActivity extends AppCompatActivity implements GadgetListCallback {


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

        // Setup Tabs
        TabLayout tabs = (TabLayout)findViewById(R.id.main_tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.main_pager);
        CustomAdapter adapter = new CustomAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onGadgetClicked(Gadget gadget) {
        // TODO: Distinguish between phone and tablet
        Log.d("LOG", "You clicked on: " + gadget.getName());
        Intent fragmentIntent = new Intent(this, GadgetDetailActivity.class);
        fragmentIntent.putExtra(Constant.GADGET, gadget);
        startActivity(fragmentIntent);
    }

    public static class CustomAdapter extends FragmentPagerAdapter {

        public CustomAdapter (FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            // TODO: Pass List of Gadgets to fragment
            return GadgetListFragment.getInstance(Constant.pageTitles[position], new ArrayList<Gadget>());
        }

        @Override
        public int getCount() {
            return Constant.pageTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position){
            // TODO: Localize!
            return Constant.pageTitles[position];
        }
    }}
