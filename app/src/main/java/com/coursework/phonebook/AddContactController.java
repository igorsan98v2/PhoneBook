package com.coursework.phonebook;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest.permission.*;
import android.Manifest.permission_group.*;

import java.security.acl.Group;


public class AddContactController extends DialogFragment {
    private RecycleAdapter recycleAdapter;
    private String photoURL;
    private ImageView imageView;
    private boolean isEdit  = false;
    private int position = 0;
    private static final int PERMISSIONS_REQUEST_ACCESS_FILES = 2;

    public void setEdit(boolean edit,int position) {
        isEdit = edit;
        this.position = position;
    }

    public RecycleAdapter getRecycleAdapter() {
        return recycleAdapter;
    }

    public void setRecycleAdapter(RecycleAdapter recycleAdapter) {
        this.recycleAdapter = recycleAdapter;
    }
    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data){
               if(data!=null){
                   Uri pickedImage = data.getData();
                         // Let's read picked image path using content resolver
                   String[] filePath = { MediaStore.Images.Media.DATA };
                   Cursor cursor = getContext().getContentResolver().query(pickedImage, filePath, null, null, null);
                   cursor.moveToFirst();
                   String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                         // Now we need to set the GUI ImageView data with data read from the picked file.
                   imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                   photoURL = imagePath;
                         // At the end remember to close the cursor or you will end with the RuntimeException!
                   cursor.close();
               }
           }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstance) {

        final View view =  inflater.inflate(R.layout.contact_editor_layout,null);
        imageView = view.findViewById(R.id.photo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create an Intent with action as ACTION_PICK
                Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.setType("image/*");
                //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                // Launching the Intent
                startActivityForResult(intent,PERMISSIONS_REQUEST_ACCESS_FILES);


            }
        });

        Button button = view.findViewById(R.id.ok);
        if(isEdit){
            TextView textView = view.findViewById(R.id.add_user);
            textView.setText(getString(R.string.edit_user));
            button.setText(getString(R.string.edit));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact contact = new Contact();
                EditText editText = view.findViewById(R.id.name);
                contact.setName(editText.getText().toString());
                editText = view.findViewById(R.id.surname);
                contact.setSurname(editText.getText().toString());
                editText = view.findViewById(R.id.company);
                contact.setCompany(editText.getText().toString());
                editText = view.findViewById(R.id.phone);
                contact.setPhone(editText.getText().toString());
                contact.setPhotoPath(photoURL);
                if(isEdit){

                    recycleAdapter.edit(position,contact);

                }
                else {
                    recycleAdapter.add(contact);
                }
                dismiss();
            }
        });

        return view;
    }

}
