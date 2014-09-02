/**
 The MIT License (MIT)

 Copyright (c) 2014 Oleg Kosmakov

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

package org.kosmakoff.pocketreckoner;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import junit.framework.Assert;

import org.kosmakoff.pocketreckoner.data.PeopleRepository;
import org.kosmakoff.pocketreckoner.data.Person;

import java.util.ArrayList;

public class AddEditPersonActivity extends Activity {

//    static final int REQUEST_IMAGE_CAPTURE = 1;
//    static final int REQUEST_IMAGE_PICK = 2;
//    static final int REQUEST_IMAGE_CROP = 3;

    static final String LOG_TAG = "ADD_EDIT_PERSON";

    private PeopleRepository peopleRepository;

    private ImageButton imageButton;

    private EditText nameEditText;
    private EditText phoneNumberEditText;
    private EditText emailEditText;

    private static Bitmap personBitmap;

    private long personId; // person Id as per DB

    // private static Uri personImageUriOriginal;
    // private static Uri personImageUriCropped;

    // private static ArrayList<Uri> urisToRemove = new ArrayList<Uri>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_person);

        personBitmap = null;
        peopleRepository = new PeopleRepository(this);

        // mapping controls
        imageButton = (ImageButton) findViewById(R.id.addPersonImageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageButtonClicked(view);
            }
        });

        nameEditText = (EditText) findViewById(R.id.addPersonName);
        phoneNumberEditText = (EditText) findViewById(R.id.addPersonPhoneNumber);
        emailEditText = (EditText) findViewById(R.id.addPersonEmail);

        // custom action bar
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done, null);

        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDoneButtonClicked();
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelButtonClicked();
            }
        });

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        // loading data for edit person intent
        Intent intent = getIntent();
        personId = intent.getLongExtra("personId", 0);

        if (personId != 0) {
            // if person was passed - it's Edit action
            Log.d(LOG_TAG, "Loading person with Id=" + personId);
            Person person = peopleRepository.getPerson(personId);
            if (person == null)
                throw new NullPointerException("No person found with Id=" + personId);

            nameEditText.setText(person.getName());
            emailEditText.setText(person.getEmail());
            phoneNumberEditText.setText(person.getPhone());
        } else {
            // it's new action, so nothing to do here
            Log.d(LOG_TAG, "Adding new person");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void onDoneButtonClicked() {
        if (validate()) {
            // save and return

            String name = nameEditText.getText().toString().trim();
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            if (personId == 0) {
                // saving new person
                peopleRepository.addPerson(name, phoneNumber, email, personBitmap);
            } else {
                peopleRepository.updatePerson(personId, name, phoneNumber, email, personBitmap);
            }

            setResult(RESULT_OK);
            finish();
        }
    }

    private void onCancelButtonClicked() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private boolean validate() {

        return validateName() && validateEmail();
    }

    private boolean validateName() {
        String name = nameEditText.getText().toString().trim();
        if (name.trim().isEmpty()) {
            nameEditText.setError(getString(R.string.name_required));
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        String email = emailEditText.getText().toString();
        if (!email.trim().isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.invalid_email));
            return false;
        }
        return true;
    }

    private void onImageButtonClicked(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        MenuItem menuItem;

        if (personBitmap != null) {
            menuItem = popupMenu.getMenu().add(getString(R.string.clear_photo));
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    handleClearPhotoMenuItem();

                    return true;
                }
            });
        }

        if (Utils.isCameraAvailable(this)) {
            menuItem = popupMenu.getMenu().add(getString(R.string.take_photo));
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    handleTakePhotoMenuItem();

                    return true;
                }
            });
        }

        menuItem = popupMenu.getMenu().add(getString(R.string.browse_gallery));
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                handleBrowseGalleryMenuItem();

                return true;
            }
        });

        popupMenu.show();
    }

    private void handleClearPhotoMenuItem() {
        Log.d(LOG_TAG, "Clear photo");

        Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();

        // personBitmap = null;
        // imageButton.setImageResource(R.drawable.ic_action_emo_laugh);
    }

    private void handleTakePhotoMenuItem() {
        Log.d(LOG_TAG, "Taking photo");

        Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();

//        File outputDir = getExternalCacheDir();
//
//        try {
//            personImageUriOriginal = Uri.fromFile(File.createTempFile("reckoner.person.orig.", ".image", outputDir));
//            urisToRemove.add(personImageUriOriginal);
//        } catch (IOException ex) {
//            return;
//        }
//
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, personImageUriOriginal);
//
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
    }

    private void handleBrowseGalleryMenuItem() {
        Log.d(LOG_TAG, "Browse gallery");

        Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();


//        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        switch (requestCode) {
//            case REQUEST_IMAGE_CAPTURE:
//                if (resultCode == RESULT_OK) {
//                    Uri uri = personImageUriOriginal;
//                    Log.d("Add Person", "Image URI: " + uri.toString());
//                    cropBitmap(personImageUriOriginal);
//                } else {
//                    clearCachedFiles();
//                }
//
//                break;
//            case REQUEST_IMAGE_PICK:
//                if (resultCode == RESULT_OK) {
//                    Uri imageUri = data.getData();
//                    cropBitmap(imageUri);
//                } else {
//                    clearCachedFiles();
//                }
//                break;
//
//            case REQUEST_IMAGE_CROP:
//                if (resultCode == RESULT_OK) {
//                    try {
//                        personBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), personImageUriCropped);
//                        imageButton.setImageBitmap(personBitmap);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//
//                    // drop old images
//                }
//
//                clearCachedFiles();
//
//                break;
//        }
    }

    private void clearCachedFiles() {

        /*
        for(Uri u : urisToRemove) {
            getContentResolver().delete(u, null, null);
        }
        */

//        urisToRemove.clear();
//
//        personImageUriOriginal = null;
//        personImageUriCropped = null;
    }

    private void cropBitmap(Uri uri) {


//        String fileName = uri.getPath();
//
//        File outputDir = getExternalCacheDir();
//        try {
//            personImageUriCropped = Uri.fromFile(File.createTempFile("reckoner.person.cropped.", ".image", outputDir));
//            urisToRemove.add(personImageUriCropped);
//        } catch (IOException ex) {
//            return;
//        }
//
//        Intent cropIntent = new CropImageIntentBuilder(1,1,256,256,personImageUriCropped)
//                .setSourceImage(uri)
//                .setDoFaceDetection(false)
//                .getIntent(this);
//
//        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }
}
