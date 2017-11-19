package uyg1.mehmetonar.com.objectdetection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import uyg1.mehmetonar.com.objectdetection.fragments.AboutFragment;
import uyg1.mehmetonar.com.objectdetection.fragments.CameraFragment;
import uyg1.mehmetonar.com.objectdetection.fragments.ListFragments;

public class MainActivity extends AppCompatActivity {

    android.support.v4.app.FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


       /* BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,new CameraFragment(getApplicationContext())).commit();

    }


   /* private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.replace(R.id.container,new CameraFragment(getApplicationContext())).commit();

                    return true;
                case R.id.navigation_dashboard:
                    fragmentTransaction.replace(R.id.container,new ListFragments(getApplicationContext())).commit();
                    return true;
                case R.id.navigation_notifications:
                    fragmentTransaction.replace(R.id.container,new AboutFragment(getApplicationContext())).commit();
                    return true;
            }
            return false;
        }

    };
*/


}
