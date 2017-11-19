package com.clarifai.android.starter.api.v2.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import com.clarifai.android.starter.api.v2.App;
import com.clarifai.android.starter.api.v2.ClarifaiUtil;
import com.clarifai.android.starter.api.v2.R;
import com.clarifai.android.starter.api.v2.adapter.RecognizeConceptsAdapter;
import com.clarifai.android.starter.api.v2.cameratools.CameraHelper;
import com.clarifai.android.starter.api.v2.cameratools.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public final class RecognizeConceptsActivity extends BaseActivity {

  public static final int PICK_IMAGE = 100;

  // the list of results that were returned from the API
  @BindView(R.id.resultsList) RecyclerView resultsList;

  // the view where the image the user selected is displayed
  @BindView(R.id.image) ImageView imageView;

  // switches between the text prompting the user to hit the FAB, and the loading spinner
  @BindView(R.id.switcher) ViewSwitcher switcher;

  // the FAB that the user clicks to select an image




   private RecognizeConceptsAdapter adapter;


  private Camera mCamera;
  CameraPreview mPreview;
  FrameLayout preview;
  FloatingActionButton fab;
  public static final int MEDIA_TYPE_IMAGE = 1;
  public static final int MEDIA_TYPE_VIDEO = 2;






  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActionBar actionBar = getSupportActionBar();
    //actionBar.setIcon(R.mipmap.logo);

    Log.d("CAMERA_KONTROL","CAMERA STATE CHECK :"+checkCameraHardware(this));
    Log.d("CAMERA AÇIK :",""+getCameraInstance());
    fab = (FloatingActionButton) findViewById(R.id.fab);
    mCamera=getCameraInstance();
    mPreview =new CameraPreview(this,mCamera);
    preview = (FrameLayout) findViewById(R.id.camera_preview);
    preview.addView(mPreview);
    Log.d("CAMERA","GÖRÜNTÜ ALINIYOR");
    imageView.setVisibility(GONE);
      imageView.setRotation(90);
    preview.setVisibility(VISIBLE);

    adapter = new RecognizeConceptsAdapter();

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          mCamera.autoFocus(new Camera.AutoFocusCallback() {
              @Override
              public void onAutoFocus(boolean b, Camera camera) {
                  camera.startPreview();
              }
          });
          mCamera.takePicture(null,null,mPicture);
      }
    });

  }

  @Override protected void onStart() {
    super.onStart();

    resultsList.setLayoutManager(new LinearLayoutManager(this));
    resultsList.setAdapter(adapter);
  }


  void takepicture() {
    Toast.makeText(this, "örüntü alma düğmesi", Toast.LENGTH_SHORT).show();
    Log.d("TAKE_CAPTURE","görüntü alma düğmesi");
    mCamera.autoFocus(new Camera.AutoFocusCallback() {
      @Override
      public void onAutoFocus(boolean b, Camera camera) {
        camera.startPreview();
      }
    });

    mCamera.takePicture(null,null,mPicture);


    mCamera.stopPreview();

  }

  @OnLongClick(R.id.fab)
  boolean pickImage() {
    preview.setVisibility(GONE);
    imageView.setVisibility(VISIBLE);
    startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), PICK_IMAGE);
    return true;
  }



  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != RESULT_OK) {
      return;
    }
    switch(requestCode) {
      case PICK_IMAGE:
        final byte[] imageBytes = ClarifaiUtil.retrieveSelectedImage(this, data);
        if (imageBytes != null) {
          onImagePicked(imageBytes);
        }
        break;
    }
  }

  private void onImagePicked(@NonNull final byte[] imageBytes) {
    // Now we will upload our image to the Clarifai API
    setBusy(true);

    // Make sure we don't show a list of old concepts while the image is being uploaded
    adapter.setData(Collections.<Concept>emptyList());

    new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
      @Override protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
        // The default Clarifai model that identifies concepts in images
        final ConceptModel generalModel = App.get().clarifaiClient().getDefaultModels().generalModel();

        // Use this model to predict, with the image that the user just selected as the input
        return generalModel.predict()
            .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
            .executeSync();
      }

      @Override protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
        setBusy(false);
        if (!response.isSuccessful()) {
          showErrorSnackbar(R.string.error_while_contacting_api);
          return;
        }
        final List<ClarifaiOutput<Concept>> predictions = response.get();
        if (predictions.isEmpty()) {
          showErrorSnackbar(R.string.no_results_from_api);
          return;
        }
        adapter.setData(predictions.get(0).data());
          imageView.setVisibility(VISIBLE);
          preview.setVisibility(GONE);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
      }

      private void showErrorSnackbar(@StringRes int errorString) {
        Snackbar.make(
            root,
            errorString,
            Snackbar.LENGTH_INDEFINITE
        ).show();
      }
    }.execute();
  }


  @Override protected int layoutRes() { return R.layout.activity_recognize; }

  private void setBusy(final boolean busy) {
    runOnUiThread(new Runnable() {
      @Override public void run() {
        switcher.setDisplayedChild(busy ? 1 : 0);
        imageView.setVisibility(busy ? GONE : VISIBLE);
        fab.setEnabled(!busy);
      }
    });
  }



  public Camera.PictureCallback mPicture = new Camera.PictureCallback() {


    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
      File picFile = CameraHelper.getOutputMediaFile(MEDIA_TYPE_IMAGE);//camera helper yazılacak
      Log.d("tag","tag"+picFile.toString());
      if (picFile==null){

      }

      try {

        FileOutputStream fos=new FileOutputStream(picFile);
        fos.write(bytes);




        Log.d("tag","tag"+picFile.toString());
        Log.d("tag","tag"+fos.toString());

        Toast.makeText(getApplicationContext(), ""+picFile.toString(), Toast.LENGTH_SHORT).show();
        fos.close();
        onImagePicked(bytes);
      }catch (FileNotFoundException e){
        Log.d("FİLE NOT FOUND","file not found"+e.getMessage());
      }catch (IOException e){
        Log.d("File error","error accesing file"+e.getMessage());
      }
    }
  };

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

}
