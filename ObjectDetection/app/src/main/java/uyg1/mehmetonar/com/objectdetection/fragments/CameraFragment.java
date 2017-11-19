package uyg1.mehmetonar.com.objectdetection.fragments;

import uyg1.mehmetonar.com.objectdetection.R;
import uyg1.mehmetonar.com.objectdetection.cameratools.CameraHelper;
import uyg1.mehmetonar.com.objectdetection.cameratools.CameraPreview;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraFragment extends Fragment implements View.OnClickListener{

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Button btnCapture;
    private TextView tvInfoOutput;
    private Context mContext;
    private Camera mCamera;
    private CameraPreview mPreview;
    private View view;


    public CameraFragment(Context context) {
        this.mContext=context;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("CAMERA_KONTROL","CAMERA STATE CHECK :"+checkCameraHardware(mContext));
        Log.d("CAMERA AÇIK :",""+getCameraInstance());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_camera, container, false);

        mCamera=getCameraInstance();
        mPreview =new CameraPreview(mContext,mCamera);
        FrameLayout preview =(FrameLayout) view.findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        Log.d("CAMERA","GÖRÜNTÜ ALINIYOR");

        tvInfoOutput = view.findViewById(R.id.tv_info_output);
        btnCapture = view.findViewById(R.id.btn_capture);
        btnCapture.setOnClickListener(this);

        return view;
    }


    /**
     * this function check camere features and return camera features
     * @param context
     * @return
     */
    private boolean checkCameraHardware(Context context){
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * getting camera instance @Camera
     * @return
     */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c= Camera.open();
            Log.d("CAMERA AÇIK :",c.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }


    @Override
    public void onClick(View view) {
        Log.d("TAKE_CAPTURE","görüntü alma düğmesi");
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                camera.startPreview();
            }
        });
        mCamera.takePicture(null,null,mPicture);
        tvInfoOutput.setText("image reading...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        mCamera.startPreview();



    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
           File picFile = CameraHelper.getOutputMediaFile(MEDIA_TYPE_IMAGE);//camera helper yazılacak

            if (picFile==null){}

            try {

                FileOutputStream fos=new FileOutputStream(picFile);
                fos.write(bytes);
                Log.d("tag","tag"+picFile.toString());
                Log.d("tag","tag"+fos.toString());
                Toast.makeText(mContext, ""+picFile.toString(), Toast.LENGTH_SHORT).show();
                fos.close();
            }catch (FileNotFoundException e){
                Log.d("FİLE NOT FOUND","file not found"+e.getMessage());
            }catch (IOException e){
                Log.d("File error","error accesing file"+e.getMessage());
            }
        }
    };
}
