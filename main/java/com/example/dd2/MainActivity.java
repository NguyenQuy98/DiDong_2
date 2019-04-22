package com.example.dd2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.dd2.Fragment.GroupFragment;
import com.example.dd2.Fragment.HomeFragment;
import com.example.dd2.Fragment.MusicFragment;
import com.example.dd2.Fragment.VersionFragment;
import com.example.dd2.Fragment.VideoFragment;
import com.example.dd2.adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    MusicFragment musicFragment;
    HomeFragment homeFragment;
    VideoFragment videoFragment;
    GroupFragment groupFragment;
    MenuItem prevMenuItem;
    Toolbar toolbar;
    private String keySearch = "";
    private Menu menu;
    private int selectPage = 0;
    private AlertDialog filterDialog;
    private boolean isSortASC = true;
    private HomeMusic.OptionLike optionLike = HomeMusic.OptionLike.NOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        toolbar.setTitle("Home");
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                viewPager.setCurrentItem(0);
                                toolbar.setTitle("Home");
                                break;
                            case R.id.action_music:
                                viewPager.setCurrentItem(1);
                                toolbar.setTitle("Music");
                                break;
                            case R.id.action_video:
                                viewPager.setCurrentItem(2);
                                toolbar.setTitle("Video");
                                break;
                            case R.id.action_group:
                                viewPager.setCurrentItem(3);
                                toolbar.setTitle("Group");
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPage = position;
                onPrepareOptionsMenu(menu);
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
                switch (position){
                    case 0:
                        toolbar.setTitle("Home");
                        break;
                    case 1:
                        toolbar.setTitle("Music");
                        break;
                    case 2:
                        toolbar.setTitle("Video");
                        break;
                    case 3:
                        toolbar.setTitle("Group");
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

        homeFragment.setListener(new HomeFragment.Listener() {
            @Override
            public void onItemVideoClick(int position) {
                viewPager.setCurrentItem(2);
                videoFragment.playVideo(position);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        createFilterDialog();
    }

    private void createFilterDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_filter_dialog, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadData();
                filterDialog.dismiss();
            }
        });
        builder.setTitle("Bộ lọc");
        builder.setView(view);
        filterDialog = builder.create();
        RadioGroup rdgSort = view.findViewById(R.id.rdgSort);
        final RadioButton rdLiked = view.findViewById(R.id.rdLiked);
        final RadioButton rdDisLike = view.findViewById(R.id.rdDisLike);

        rdgSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isSortASC = checkedId == R.id.rdASC;
            }
        });

        rdLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionLike == HomeMusic.OptionLike.LIKE){
                    rdLiked.setChecked(false);
                    optionLike = HomeMusic.OptionLike.NOT;
                }else{
                    rdLiked.setChecked(true);
                    rdDisLike.setChecked(false);
                    optionLike = HomeMusic.OptionLike.LIKE;
                }
                Log.d("TEST", String.valueOf(optionLike));
            }
        });

        rdDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionLike == HomeMusic.OptionLike.DISLIKE){
                    rdDisLike.setChecked(false);
                    optionLike = HomeMusic.OptionLike.NOT;
                }else{
                    rdDisLike.setChecked(true);
                    rdLiked.setChecked(false);
                    optionLike = HomeMusic.OptionLike.DISLIKE;
                }
                Log.d("TEST", String.valueOf(optionLike));
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_home_music, menu);
        this.menu = menu;
        MenuItem searchView = menu.findItem(R.id.search);
        final SearchView searchViewActionBar = (SearchView) MenuItemCompat.getActionView(searchView);
//        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchViewActionBar.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchAutoComplete.setHintTextColor(Color.GRAY);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        };
        searchViewActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                keySearch = newText;
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 500);
                return true;
            }
        });
        searchViewActionBar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                keySearch = "";
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 500);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void loadData() {
        musicFragment.filterData(keySearch, isSortASC, optionLike);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }else if (item.getItemId() == R.id.filter){
            filterDialog.show();
        }else if (item.getItemId() == R.id.search){
            viewPager.setCurrentItem(1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null){
            menu.findItem(R.id.filter).setVisible(selectPage == 1);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.db) {
////            Toast.makeText(getApplicationContext(), "Ok db", Toast.LENGTH_SHORT).show();
//            viewPager.setCurrentItem(1);
//        } else
        if (id == R.id.home) {
            viewPager.setCurrentItem(0);
            toolbar.setTitle("Home");
        } else if (id == R.id.music) {
            viewPager.setCurrentItem(1);
            toolbar.setTitle("Music");
        } else if (id == R.id.video) {
            viewPager.setCurrentItem(2);
            toolbar.setTitle("Video");
        } else if (id == R.id.group) {
            viewPager.setCurrentItem(3);
            toolbar.setTitle("Group");
        }
        else if (id == R.id.version) {
            Intent intent = new Intent(MainActivity.this, VersionFragment.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        musicFragment = new MusicFragment();
        videoFragment = new VideoFragment();
        groupFragment = new GroupFragment();
//        themeFragment = new ThemeFragment();
        adapter.addFragment(homeFragment);
//        adapter.addFragment(themeFragment);
        adapter.addFragment(musicFragment);
        adapter.addFragment(videoFragment);
        adapter.addFragment(groupFragment);

        viewPager.setAdapter(adapter);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setOffscreenPageLimit(4);
            }
        });
    }
}
