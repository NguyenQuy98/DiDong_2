package com.example.dd2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.dd2.Fragment.ListMusicFragment;
import com.example.dd2.Fragment.PlayMusicFragment;
import com.example.dd2.adapters.MusicAdapter;
import com.example.dd2.adapters.ViewPagerAdapter;
import com.example.dd2.commons.Common;
import com.example.dd2.models.Music;
import com.example.dd2.services.PlayService;

public class HomeMusic extends AppCompatActivity {
    ViewPager viewpager;

    private ImageButton btnNext, btnPlay, btnPrev, btnLike;

    FrameLayout btnRepeat;

    PlayMusicFragment playMusicFragment = new PlayMusicFragment();
    ListMusicFragment listMusicFragment = new ListMusicFragment();
    private String keySearch = "";
    AlertDialog filterDialog, clockDialog;
    boolean isSortASC = true;
    OptionLike optionLike = OptionLike.NOT;
    Menu menu;
    MenuItem itemClock;
    int selectPage = 1;
    public enum OptionLike{
        LIKE, DISLIKE, NOT;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TEST","isChangeSong " + Common.isChangeSong);
        startService(new Intent(HomeMusic.this, PlayService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_music);
        getSupportFragmentManager().popBackStack();
        viewpager = findViewById(R.id.viewpager);
        btnNext = findViewById(R.id.btnNext);
        btnPlay = findViewById(R.id.btnPlay);
        btnPrev = findViewById(R.id.btnPrev);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnLike = findViewById(R.id.btnLike);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Common.listMusic.size() > 0)
            getSupportActionBar().setTitle(Common.listMusic.get(Common.selectId).getTitle());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(listMusicFragment);
        adapter.addFragment(playMusicFragment);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(2);
        viewpager.setCurrentItem(1);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                selectPage = i;
                onPrepareOptionsMenu(menu);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        btnPlay.setTag(true);

        btnLike.setTag(Common.selectId);
        if (Common.listMusic.get(Common.selectId).isLike()){
            btnLike.setBackground(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        }else{
            btnLike.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

        playMusicFragment.setListener(new PlayMusicFragment.Listener() {
            @Override
            public void onPause() {
                btnPlay.setImageDrawable(getResources().getDrawable(R.drawable.ico_play));
                btnPlay.setTag(false);
            }

            @Override
            public void onPlaying() {
                if (Common.listMusic.size() > 0) {
                    getSupportActionBar().setTitle(Common.listMusic.get(Common.selectId).getTitle());
                    btnLike.setTag(Common.selectId);
                    if (Common.listMusic.get(Common.selectId).isLike()){
                        btnLike.setBackground(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    }else{
                        btnLike.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    }

                }
                btnPlay.setImageDrawable(getResources().getDrawable(R.drawable.ico_pause));
                btnPlay.setTag(true);
                if (PlayService.time != 0 && System.currentTimeMillis() >= PlayService.time){
                    HomeMusic.this.stopService(new Intent(HomeMusic.this, PlayService.class));
                    btnPlay.setTag(false);
                    PlayService.time = 0;
                    itemClock.setIcon(getResources().getDrawable(R.drawable.ic_alarm_black_24dp));
                    createClockDialog();
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(boolean)btnPlay.getTag()) {
                    //btnPlay.setImageDrawable(getResources().getDrawable(R.drawable.ico_pause));
                    PlayService.isStart = true;
                    startService(new Intent(HomeMusic.this, PlayService.class));
                    playMusicFragment.onResume();
                } else {
                    PlayService.isStart = false;
                    //btnPlay.setImageDrawable(getResources().getDrawable(R.drawable.ico_play));
                    if (PlayService.mediaPlayer != null)
                        PlayService.mediaPlayer.pause();
                    stopService(new Intent(HomeMusic.this, PlayService.class));
                }
                btnPlay.setTag(!(boolean)btnPlay.getTag());
            }
        });


        // Sự kiện nút next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.selectId++;
                if (Common.selectId > Common.listMusic.size() - 1){
                    Common.selectId = 0;
                }
               changeSong(Common.selectId);
            }
        });


        // Sự kiện nút back
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.selectId--;
                if (Common.selectId < 0){
                    Common.selectId = Common.listMusic.size() - 1;
                }
                changeSong(Common.selectId);
            }
        });
        btnRepeat.setTag(false);
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PlayService.isRepeat){
                    btnRepeat.findViewById(R.id.tv).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_purple)));
                }else{
                    btnRepeat.findViewById(R.id.tv).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.defaultControl)));
                }
                PlayService.isRepeat = !PlayService.isRepeat;
            }
        });

        //lưu sharePreferences
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.listMusic.get(Common.selectId).isLike()){
                    btnLike.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    Common.listMusic.get(Common.selectId).setLike(false);
                }else{
                    btnLike.setBackground(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    Common.listMusic.get(Common.selectId).setLike(true);
                }
                Music.likeOrDislike(HomeMusic.this, Common.listMusic.get(Common.selectId));
                listMusicFragment.notifiChangeItem(Common.selectId);
            }
        });

        listMusicFragment.setListener(new MusicAdapter.Listener() {
            @Override
            public void onItemLike() {
                if (Common.selectId == Integer.parseInt(btnLike.getTag()+"")){
                    if (Common.listMusic.get(Common.selectId).isLike()){
                        btnLike.setBackground(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    }else{
                        btnLike.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    }
                }
            }

            @Override
            public void onItemClick() {
                Intent intent = new Intent(HomeMusic.this, PlayService.class);
                stopService(intent);
                startService(intent);
                btnLike.setTag(Common.selectId);
                if (Common.listMusic.get(Common.selectId).isLike()){
                    btnLike.setBackground(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                }else{
                    btnLike.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }
            }
        });

        createFilterDialog();
        createClockDialog();
    }

    private void createClockDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_clock_dialog, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        clockDialog = builder.create();
        final RadioGroup group = view.findViewById(R.id.group);
        final SwitchCompat sw = view.findViewById(R.id.sw);
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw.isChecked()){
                    PlayService.time = System.currentTimeMillis() + Long.parseLong(group.getTag()+"");
                    itemClock.setIcon(getResources().getDrawable(R.drawable.ic_alarm_on_24dp));
                }else{
                    itemClock.setIcon(getResources().getDrawable(R.drawable.ic_alarm_black_24dp));
                }
            }
        });
        group.setTag(10000);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rd30:
                        group.setTag(10000);
                        //group.setTag(30*60000);
                        break;
                    case R.id.rd60:
                        group.setTag(60*60000);
                        break;
                    case R.id.rd90:
                        group.setTag(90*60000);
                        break;
                    case R.id.rd120:
                        group.setTag(120*60000);
                        break;
                }
            }
        });

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
                if (optionLike == OptionLike.LIKE){
                    rdLiked.setChecked(false);
                    optionLike = OptionLike.NOT;
                }else{
                    rdLiked.setChecked(true);
                    optionLike = OptionLike.LIKE;
                }
            }
        });

        rdDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionLike == OptionLike.DISLIKE){
                    rdDisLike.setChecked(false);
                    optionLike = OptionLike.NOT;
                }else{
                    rdDisLike.setChecked(true);
                    optionLike = OptionLike.DISLIKE;
                }
            }
        });
    }

    void changeSong(int id){
        startService(new Intent(HomeMusic.this, PlayService.class));
        playMusicFragment.setNameSong(Common.listMusic.get(id).getTitle());
        //btnPlay.setBackgroundResource(R.drawable.ico_pause);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_music, menu);
        this.menu = menu;
        itemClock = menu.findItem(R.id.clock);
        itemClock.setVisible(true);
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
        return super.onCreateOptionsMenu(menu);
    }

    private void loadData() {
        listMusicFragment.filterData(keySearch, isSortASC, optionLike);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }else if (item.getItemId() == R.id.filter){
            filterDialog.show();
        }else if (item.getItemId() == R.id.search){
            viewpager.setCurrentItem(0);
        }else if (item.getItemId() == R.id.clock){
            clockDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null){
            menu.findItem(R.id.filter).setVisible(selectPage != 1);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
