package uyg1.mehmetonar.com.objectdetection.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uyg1.mehmetonar.com.objectdetection.R;


public class ListFragments extends Fragment {

    Context mContext;

    public ListFragments(Context context) {
        this.mContext = context;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_fragments, container, false);
    }


}
