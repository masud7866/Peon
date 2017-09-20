package com.ieitlabs.peon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SideBar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DatabaseAdapter d = new DatabaseAdapter(SideBar.this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView txtLBLUsername = (TextView)hView.findViewById(R.id.lbl_uname);
        TextView txtLBLUserEmail = (TextView)hView.findViewById(R.id.lbl_uemail);
        if(d.getAppMeta("ac_type").equals("org"))
        {
            navigationView.getMenu().setGroupVisible(R.id.nav_group_user,false);
            txtLBLUsername.setText(d.getAppMeta("org_title"));
            txtLBLUserEmail.setText(d.getAppMeta("email"));
        }
        else
        {
            txtLBLUsername.setText(d.getAppMeta("fname") + " " + d.getAppMeta("lname"));
            navigationView.getMenu().setGroupVisible(R.id.nav_group_admin,false);
            txtLBLUserEmail.setText(d.getAppMeta("email"));
            if(d.getAppMeta("group_role").equals("1"))
            {
                navigationView.getMenu().findItem(R.id.nav_manage_user).setVisible(true);
            }
        }





        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_dashboard);
        Fragment fragmentDashboard = new FragmentDashboard();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame, fragmentDashboard);
        transaction.commit();

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

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switchFragment(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchFragment(int id)
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();
        MenuItem item = menuNav.findItem(id);
        SideBar.this.setTitle(item.getTitle());
        FragmentManager fm;
        FragmentTransaction transaction;
        switch (id)
        {
            case R.id.nav_dashboard:
                Fragment fragmentDashboard = new FragmentDashboard();
                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, fragmentDashboard);
                transaction.commit();
                break;
            case R.id.nav_cgroups:
                Fragment fragmentCreateGroup = new FragmentCreateGroup();
                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, fragmentCreateGroup);
                transaction.commit();
                break;
            case R.id.nav_vgroups:
                Fragment fragmentViewGroups = new FragmentViewGroups();
                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, fragmentViewGroups);
                transaction.commit();
                break;
            case R.id.nav_about:
                Fragment fragmentAbout = new FragmentAbout();
                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, fragmentAbout);
                transaction.commit();
                break;
            case R.id.nav_help:
                Fragment fragmentHelp = new FragmentHelp();
                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, fragmentHelp);
                transaction.commit();
                break;
            case R.id.nav_logout:
                case R.id.nav_logout1:
                Toast.makeText(SideBar.this,"Logging out", Toast.LENGTH_SHORT).show();
                DatabaseAdapter d = new DatabaseAdapter(SideBar.this);
                String url="http://peon.ml/api/logout?uid="+ d.getAppMeta("uid") +"&skey=" + d.getAppMeta("session");
                (new ServerTasker(SideBar.this,SideBar.this,2,url)).execute((Void)null);
                break;
            case R.id.nav_manage_user:
                Fragment fragmentManageUser = new FragmentManageUser();
                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, fragmentManageUser);
                transaction.commit();
                break;
        }
    }

    public void switchFragmentExtended(int layout_id)
    {
        FragmentManager fm;
        FragmentTransaction transaction;
        switch (layout_id)
        {
            case R.layout.fragment_fragment_dashboard:
                Fragment fragmentDashboard = new FragmentDashboard();
                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, fragmentDashboard);
                transaction.commit();
                break;
            case R.layout.fragment_fragment_create_group:
                Fragment fragmentCreateGroup = new FragmentCreateGroup();
                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, fragmentCreateGroup);
                transaction.commit();
                break;
            case R.layout.fragment_fragment_view_groups:
                Fragment fragmentViewGroups = new FragmentViewGroups();
                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, fragmentViewGroups);
                transaction.commit();
                break;
            case R.layout.fragment_fragment_about:
                Fragment fragmentAbout = new FragmentAbout();
                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, fragmentAbout);
                transaction.commit();
                break;
            case R.layout.fragment_fragment_help:
                Fragment fragmentHelp = new FragmentHelp();
                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.frame, fragmentHelp);
                transaction.commit();
                break;
        }
    }
}
