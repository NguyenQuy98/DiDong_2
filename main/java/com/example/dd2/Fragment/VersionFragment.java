package com.example.dd2.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.dd2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VersionFragment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_version);
        getSupportFragmentManager().popBackStack();

    }

//    public VersionFragment() {
//        // Required empty public constructor
//    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_version, container, false);
//    }

}
