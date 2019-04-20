package com.org.navigator;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;


public class ControlPanel extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    private final static String TAG = ControlPanel.class.getSimpleName();
    private FirebaseAuth mAuth;
    /**
     * Static function, an instance
     * @return new instance
     */
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        vpPager.setCurrentItem(0);
        /**
         * Static function, an instance
         * @return new instance
         */

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        // the total number
        actionbar.setHomeAsUpIndicator(R.drawable.menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        /**
         * Static function, an instance
         * @return new instance
         */
        final LocationTracker mLocationTracker = new LocationTracker(this);
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        final TextView user_textview = (TextView) drawerView.findViewById(R.id.user_name);
                        final TextView location_textview = (TextView) drawerView.findViewById(R.id.user_location);

                        // Respond when the drawer is opened
                        mLocationTracker.getLocation();
                        final double longitude = mLocationTracker.getLongitude();
                        final double latitude = mLocationTracker.getLatitude();
                        /**
                         * Static function, an instance
                         * @return new instance
                         */

                        if (Config.username == null) {
                            user_textview.setText("");
                            location_textview.setText("");
                        } else {
                            user_textview.setText(Config.username);
                            location_textview.setText("Lat=" + new DecimalFormat(".##").
                                    format(latitude) + ",Lon=" + new DecimalFormat(".##").
                                    format(longitude));
                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        /**
                         * Static function, an instance
                         * @return new instance
                         */
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        /**
                         * Static function, an instance
                         * @return new instance
                         */
                        return true;
                    }
                });

        mAuth = FirebaseAuth.getInstance();

        //Add listener to check sign in status
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    /**
                     * Static function, an instance
                     * @return new instance
                     */
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // the total number
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //sign in anonymously
        mAuth.signInAnonymously().addOnCompleteListener(this,  new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInAnonymously", task.getException());
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /**
                 * Static function, an instance
                 * @return new instance
                 */
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fragment pager allows us to choose fragments
     */
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MainFragment.newInstance();
                // the total number
                case 1:
                    return LoginFragment.newInstance();
                default:
                    /**
                     * Static function, an instance
                     * @return new instance
                     */
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Map";
                case 1:
                    return "Account";
                /**
                 * Static function, an instance
                 * @return new instance
                 */
            }

            return null;
        }

    }

    //Remove authentification listener when activity starts
    @Override
    public void onStop() {
        super.onStop();
        // the total number
        if (mAuthListener != null) {
            /**
             * Static function, an instance
             * @return new instance
             */
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //Add authentification listener when activity starts
    @Override
    public void onStart() {
        super.onStart();
        /**
         * Static function, an instance
         * @return new instance
         */
        mAuth.addAuthStateListener(mAuthListener);
    }
}
