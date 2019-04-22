package com.example.dd2.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dd2.HomeMusic;
import com.example.dd2.R;
import com.example.dd2.adapters.MusicAdapter;
import com.example.dd2.commons.Common;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    boolean isLoad = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_music, container, false);
//        runTimePermission();
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        musicAdapter = new MusicAdapter(false, getContext(),Common.listMusic);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(musicAdapter);
        musicAdapter.setListener(new MusicAdapter.Listener() {
            @Override
            public void onItemLike() {

            }

            @Override
            public void onItemClick() {

            }
        });
        return v;
    }

    public void filterData(String keySearch, boolean isSortASC, HomeMusic.OptionLike optionLike) {
        musicAdapter.setConditionFilter(isSortASC, optionLike);
        musicAdapter.getFilter().filter(keySearch);
    }
}
