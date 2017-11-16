package uyg1.mehmetonar.com.objectdetection.fragments;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uyg1.mehmetonar.com.objectdetection.R;
import uyg1.mehmetonar.com.objectdetection.cameratools.CameraPreview;


public class ListFragments extends Fragment{

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Button btnCapture;
    private Context mContext;
    private Camera mCamera;
    private CameraPreview mPreview;


    public ListFragments(Context context) {
        this.mContext=context;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);



        return view;
    }




}
