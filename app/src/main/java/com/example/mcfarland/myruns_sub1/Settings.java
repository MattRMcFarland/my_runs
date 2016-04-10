package com.example.mcfarland.myruns_sub1;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.app.DialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.soundcloud.android.crop.Crop;

public class Settings extends Activity {

    /* ------------------------------ Callback Codes ------------------------------ */
    public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
    public static final int REQUEST_CODE_FROM_GALLERY = 1;

    /* ------------------------------ Constants for Settings data ------------------------------ */
    private static final String TAG = "Settings";

    private static final String SAVE_FILE = "profile_data";

    /* shared preferences keys */
    private static final String NAME_KEY = "name_text";
    private static final String EMAIL_KEY = "email_text";
    private static final String PHONE_KEY = "phone_text";
    private static final String MAJOR_KEY = "major_text";
    private static final String MALE_KEY = "male_button";
    private static final String FEMALE_KEY = "female_button";
    private static final String CLASS_KEY = "class_text";

    /* saved instance state keys */
    private static final String PROFILE_PICTURE_URI_KEY = "saved_profile_picture_uri";
    private static final String TEMPORARY_PICTURE_LIST_KEY = "temporary_pictures_list";
    private static final String CROPPED_PICTURE_URI_KEY = "cropped_profile_picture_uri";
    private static final String CURRENT_PICTURE_URI_KEY = "current_profile_picture_uri";
    private static final String PREVIOUS_PICTURE_URI_KEY = "previous_profile_picture_uri";

    private Uri mImageCaptureUri;                               // holds camera snap
    private Uri mCroppedImageUri;                               // holds cropped snap
    private Uri mCurrentImageUri;                               // holds current profile image
    private Uri mPreviousImageUri;                              // holds last profile image
    private ArrayList<Uri> mTempPictureList;                    // list of temp photos

    private ImageView mImageView;                               // profile picture view
    private boolean isTakenFromCamera;


    /* ------------------------------ LifeCycle Functions ------------------------------ */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // reload picture temps if available
        if (savedInstanceState != null) {
            Log.d(TAG, "restoring picture URIs");
            mTempPictureList = savedInstanceState.getParcelableArrayList(TEMPORARY_PICTURE_LIST_KEY);
            mImageCaptureUri = savedInstanceState.getParcelable(PROFILE_PICTURE_URI_KEY);
            mCroppedImageUri = savedInstanceState.getParcelable(CROPPED_PICTURE_URI_KEY);
            mCurrentImageUri = savedInstanceState.getParcelable(CURRENT_PICTURE_URI_KEY);
            mPreviousImageUri = savedInstanceState.getParcelable(PREVIOUS_PICTURE_URI_KEY);
        }

        mImageView = (ImageView) findViewById(R.id.profile_picture);

        if (mTempPictureList == null)
            mTempPictureList = new ArrayList<Uri>();

        loadInfo();
        loadPicture();
    }

    @Override
    protected void onSaveInstanceState(Bundle cur_state) {
        Log.d(TAG, "saving instance state");
        super.onSaveInstanceState(cur_state);
        // Save the image capture uri before the activity goes into background
        cur_state.putParcelableArrayList(TEMPORARY_PICTURE_LIST_KEY, mTempPictureList);
        cur_state.putParcelable(PROFILE_PICTURE_URI_KEY, mImageCaptureUri);
        cur_state.putParcelable(CROPPED_PICTURE_URI_KEY, mCroppedImageUri);
        cur_state.putParcelable(CURRENT_PICTURE_URI_KEY, mCurrentImageUri);
        cur_state.putParcelable(PREVIOUS_PICTURE_URI_KEY, mPreviousImageUri);

    }

    /* ------------------------------ UI Callback Functions ------------------------------ */

    /*
     * ClickSave is called when the user clicks on the "Save" button widget
     */
    public void clickSave(View v) {
        Log.d(TAG, "clicked Save");
        saveInfo();
        saveCurrentPictureAsProfile();

        cleanSnaps();
        Toast toast = Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }

    /*
     * ClickCancel cancel's the user profile setting
     */
    public void clickCancel(View v) {
        Log.d(TAG, "clicked Cancel");

        // delete unsaved, temporary snaps
        cleanSnaps();

        finish();
    }

    /*
     * ClickClear clears all user profile settings
     */
    public void clickClear(View v) {
        Log.d(TAG, "clicked Clear");
        clearData();
        mCurrentImageUri = null;
        deleteProfilePicture();
        cleanSnaps();

        Toast toast = Toast.makeText(this, "Cleared Data", Toast.LENGTH_SHORT);
        toast.show();

        loadInfo();
        loadPicture();
    }

    /*
     * ClickGenderMale is called when the user clicks on the "male" radiobutton
     */
    public void clickMale(View v) {
        Log.d(TAG, "clicked Male");
        RadioButton is_male = (RadioButton) findViewById(R.id.male_button);
        is_male.setChecked(true);
    }

    /*
     * ClickGenderFemale is called when the user clicks on the "female" radiobutton
     */
    public void clickFemale(View v) {
        Log.d(TAG, "clicked Female");
        RadioButton is_female = (RadioButton) findViewById(R.id.female_button);
        is_female.setChecked(true);
    }



    /* ------------------------------ Profile Management Functions ------------------------------ */

    /*
     * Loads any saved user information
     */
    private void loadInfo() {
        Log.d(TAG, "loading info");
        SharedPreferences p = getSharedPreferences(SAVE_FILE, Context.MODE_PRIVATE);
        loadUserData(p);
    }

    /*
     * Loads profile views (if they exist) from Shared Preference into appropriate fields
     */
    private void loadUserData(SharedPreferences p) {

        EditText name_text = (EditText) findViewById(R.id.name_field);
        name_text.setText(p.getString(NAME_KEY, ""));
        Log.d(TAG, "\tloaded name -- " + p.getString(NAME_KEY, ""));

        EditText email_text = (EditText) findViewById(R.id.email_field);
        email_text.setText(p.getString(EMAIL_KEY, ""));
        Log.d(TAG, "\tloaded email -- " + p.getString(EMAIL_KEY, ""));

        EditText phone_text = (EditText) findViewById(R.id.phone_field);
        phone_text.setText(p.getString(PHONE_KEY, ""));
        Log.d(TAG, "\tloaded phone number --" + p.getString(PHONE_KEY, ""));

        EditText major_text = (EditText) findViewById(R.id.major_field);
        major_text.setText(p.getString(MAJOR_KEY, ""));
        Log.d(TAG, "\tloaded major --" + p.getString(MAJOR_KEY, ""));

        RadioButton is_male = (RadioButton) findViewById(R.id.male_button);
        is_male.setChecked(p.getBoolean(MALE_KEY, false));
        Log.d(TAG, "\tloaded sex (male) -- " + String.valueOf(p.getBoolean(MALE_KEY, false)));

        RadioButton is_female = (RadioButton) findViewById(R.id.female_button);
        is_female.setChecked(p.getBoolean(FEMALE_KEY, false));
        Log.d(TAG, "\tloaded sex (female) -- " + String.valueOf(p.getBoolean(FEMALE_KEY, false)));

        EditText class_text = (EditText) findViewById(R.id.class_field);
        class_text.setText(p.getString(CLASS_KEY, ""));
        Log.d(TAG, "\tloaded class -- " + p.getString(CLASS_KEY, ""));
    }

    /*
     * Saves the user's information to a shared preference - SAVE_FILE
     */
    private void saveInfo() {
        Log.d(TAG, "saving info");
        SharedPreferences p = getSharedPreferences(SAVE_FILE, Context.MODE_PRIVATE);
        updateUserData(p);
    }

    /*
     * Saves user profile values to a Shared Preferences
     */
    private void updateUserData(SharedPreferences p) {
        SharedPreferences.Editor e = p.edit();

        EditText name_text = (EditText) findViewById(R.id.name_field);
        Log.d(TAG, "\tsaving name -- " + name_text.getText().toString());
        e.putString(NAME_KEY, name_text.getText().toString());

        EditText email_text = (EditText) findViewById(R.id.email_field);
        Log.d(TAG, "\tsaving email -- " + email_text.getText().toString());
        e.putString(EMAIL_KEY, email_text.getText().toString());

        EditText phone_text = (EditText) findViewById(R.id.phone_field);
        Log.d(TAG, "\tsaving phone -- " + phone_text.getText().toString());
        e.putString(PHONE_KEY, phone_text.getText().toString());

        EditText major_text = (EditText) findViewById(R.id.major_field);
        Log.d(TAG, "\tsaving major -- " + major_text.getText().toString());
        e.putString(MAJOR_KEY, major_text.getText().toString());

        RadioButton is_male = (RadioButton) findViewById(R.id.male_button);
        Log.d(TAG, "\tsaving sex (male) -- " + String.valueOf(is_male.isChecked()));
        e.putBoolean(MALE_KEY, is_male.isChecked());

        RadioButton is_female = (RadioButton) findViewById(R.id.female_button);
        Log.d(TAG, "\tsaving sex (female) --" + String.valueOf(is_female.isChecked()));
        e.putBoolean(FEMALE_KEY, is_female.isChecked());

        EditText class_text = (EditText) findViewById(R.id.class_field);
        Log.d(TAG, "\tsaving class -- " + class_text.getText());
        e.putString(CLASS_KEY, class_text.getText().toString());

        e.apply();
    }

    /*
     * Clears all user data in the SAVE FILE
     */
    private void clearData() {
        Log.d(TAG, "clearing data");
        SharedPreferences p = getSharedPreferences(SAVE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.clear();
        e.apply();
    }

     /* ------------------------------ Dialog Handling ------------------------------ */

    /*
     * initiates a new picture sequence
     */
    public void clickChangePicture(View V) {
        Log.d(TAG, "clicked Change Picture");

        // save current image for possible reversion if user cancels photo / crop
        mPreviousImageUri = mCurrentImageUri;

        DialogFragment fragment = MyRunsDialogFragment.
                newInstance(MyRunsDialogFragment.DIALOG_ID_PHOTO_PICKER);

        fragment.show(getFragmentManager(),
                getString(R.string.dialog_fragment_tag_photo_picker));
    }

    /*
     * Handles photo dialog choice
     */
    public void onPhotoPickerItemSelected(int item) {
        switch (item) {
            case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_CAMERA:
                Log.d(TAG, "\ttaking new picture with camera");
                isTakenFromCamera = true;
                getNewProfilePictureFromCamera();
                break;

            case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_GALLERY:
                Log.d(TAG, "\tchoosing from gallery");
                chooseFromGallery();
                // launch gallery activity

            default:
        }
    }

    private void chooseFromGallery() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        // save current in case of cancellation
        mPreviousImageUri = mCurrentImageUri;

        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_CODE_FROM_GALLERY);

    }

    /* ------------------------------ Camera / Picture Functions ------------------------------ */

    /*
     * load image view with current profile picture
     */
    private void loadPicture() {
        Log.d(TAG, "Loading picture");

        if (mCurrentImageUri == null) {

            // if no image should be set, then try to load saved profile picture / default
            File saved_photo = new File(getApplicationContext().getFilesDir(),
                    getString(R.string.settings_profile_picture_filename));
            if (saved_photo.exists()) {
                Log.d(TAG, "\tloading saved picture: " + saved_photo.getPath());
                mCurrentImageUri = Uri.fromFile(saved_photo);
                mImageView.setImageURI(mCurrentImageUri);
            } else {
                Log.d(TAG, "\tloading default profile picture");
                mImageView.setImageResource(R.drawable.profile_default);
            }

        } else {
            Log.d(TAG, "\tloading current image resource");
            mImageView.setImageURI(mCurrentImageUri);
        }
    }

    /*
     * deletes saved profile picture file
     */
    private void deleteProfilePicture() {
        Log.d(TAG, "attempting to delete file " +
                getString(R.string.settings_profile_picture_filename));

        if (deleteFile(getString(R.string.settings_profile_picture_filename)))
            Log.d(TAG, "\tsuccess!");
    }

    /*
     * delete all temp pictures
     */
    private void cleanSnaps() {
        for (Uri temp : mTempPictureList) {
            if (temp != null)
                deleteUriImage(temp);
        }
    }

    /*
     * deletes a uri file
     */
    private void deleteUriImage(Uri deletable) {
        if (deletable == null)
            return;

        File to_delete = new File(deletable.getPath());
        if (to_delete.exists()) {
            Log.d(TAG, "\tdeleting temp file " + to_delete.getPath());
            to_delete.delete();
        }
    }

    /*
     * saved current profile image as the profile picture for future
     */
    private void saveCurrentPictureAsProfile() {
        Log.d(TAG, "Saving current Picture as profile picture");

        String profile_filename = (getApplicationContext().getFilesDir() + "/" +
                getString(R.string.settings_profile_picture_filename));

        if (mCurrentImageUri == null) {
            Log.d(TAG, "current image is null");
            return;
        } else if (mCurrentImageUri.getPath().compareTo(profile_filename) == 0) {
            Log.d(TAG, "\tcurrent image is already saved image");
            return;
        }

        try {
            OutputStream fos = openFileOutput(
                    getString(R.string.settings_profile_picture_filename), MODE_PRIVATE);

            File src = new File(mCurrentImageUri.getPath());
            InputStream fis = new FileInputStream(src);

            byte[] buffer = new byte[2048];
            int transferred = 0;
            while ((transferred = fis.read(buffer)) > 0) {
                fos.write(buffer);
            }

            fis.close();
            fos.close();

            src.delete();                                           // delete current
            cleanSnaps();                                           // delete all temps

        } catch (IOException e) {
            Log.d(TAG, "\tcurrent image file not found");
        }

    }

    /* ------------------------------ Camera / Picture Activities ------------------------------ */

    // using onPhotoPickerItemSelected from class notes as model
    private void getNewProfilePictureFromCamera() {

        Log.d(TAG, "getting new profile picture from camera");
        Intent take_picture_I = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // record new camera snap
        String filename = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), filename));
        Log.d(TAG, "\tsaving camera image at " + mImageCaptureUri.getPath());
        mTempPictureList.add(mImageCaptureUri);

        take_picture_I.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        take_picture_I.putExtra("return-data", true);
        try {
            // Start a camera capturing activity
            // REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
            // defined to identify the activity in onActivityResult()
            // when it returns
            Log.d(TAG, "starting new activity to take picture from camera");
            startActivityForResult(take_picture_I, REQUEST_CODE_TAKE_FROM_CAMERA);

        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "failed to start camera activity");
            e.printStackTrace();
        }
    }

    /*
     * Crop and resize the image for profile -- 3rd party
     */
    private void cropImage(Uri source) {
        Log.d(TAG,"starting crop image activity");

        // record new cropped snap
        String filename = "tmp_cropped_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mCroppedImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), filename));
        Log.d(TAG, "saving cropped image at " + mCroppedImageUri.getPath());
        mTempPictureList.add(mCroppedImageUri);

        // initiate 3rd party crop
        Crop.of(source, mCroppedImageUri).asSquare().start(this);
    }

    // Handle data after activity returns.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Log.d(TAG, "activity did not return RESULT_OK (Result Code: " + resultCode + ")");

            if (isTakenFromCamera) {
                // delete snaps on failure / cancellation
                deleteUriImage(mImageCaptureUri);
                isTakenFromCamera = false;
            }

            // delete temp cropped if exists
            deleteUriImage(mCroppedImageUri);

            // revert to previous image
            mCurrentImageUri = mPreviousImageUri;
            loadPicture();

            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_FROM_GALLERY:
                Log.d(TAG, "returned from choosing gallery image. cropping image.");
                cropImage(data.getData());
                break;

            case REQUEST_CODE_TAKE_FROM_CAMERA:
                Log.d(TAG,"returned from receiving camera image. cropping image.");
                cropImage(mImageCaptureUri);
                break;

            case Crop.REQUEST_CROP:
                Log.d(TAG, "returned from cropping (3rd party crop). saving.");
                mCroppedImageUri = Crop.getOutput(data);
                mCurrentImageUri = mCroppedImageUri;
                loadPicture();
                break;
        }
    }

}
