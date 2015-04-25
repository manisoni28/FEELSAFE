package com.feelsafe.contacts;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.Inflater;

import com.feelsafe.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomEditContactsAdapter extends BaseAdapter{
	
	Context myContext;
    LayoutInflater inflater;
    List<RowItem> data;
    ContactsDatabase db;
    //global values that store name and contact of itemdata for startactivityafterresult
    String name_aftr,contac_aftr;
     int id_aftr;
    //request codes
    final int FROM_GALLERY=1,FROM_CAMERA=2,PICTURE_CROP=3;
    //bitmap to store new image after star activity for result
    Bitmap newPhoto=null;
    Validation validator;

    public CustomEditContactsAdapter(Context myContext,List<RowItem> items,ContactsDatabase db) {
        
        inflater= LayoutInflater.from(myContext);
        this.myContext=myContext;
        this.data = items;
        this.db=db; 
        validator=new Validation(myContext);
    }
    
    public class ViewHolder {
        TextView Name;
        Button crown;
        Button edit;
        Button delete;
        ImageView photo;
    }
    
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return data.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.contacts_edit_list_row, null);
			holder = new ViewHolder();
            holder.Name=(TextView) convertView.findViewById(R.id.contactName);
            holder.crown=(Button) convertView.findViewById(R.id.crown2);
           holder.delete=(Button) convertView.findViewById(R.id.contactDeleteButtons);
           holder.edit=(Button) convertView.findViewById(R.id.contactEditButton);
           holder.photo=(ImageView) convertView.findViewById(R.id.editContactsImageview);
            convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final RowItem itemdata=(RowItem) getItem(position);
		
		holder.Name.setText(itemdata.getItemName());
		holder.photo.setImageDrawable(itemdata.getIcon());
		holder.crown.setVisibility(4); //setting the crown invisible
		if(itemdata.is_prime()){
			holder.crown.setVisibility(2); //visibility for prime contacts
		}
		
		//rowitem that store the details of current row
		final RowItem itemData2= data.get(position);
		
		
		
		
		//delete button on click listener
				holder.delete.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//Toast.makeText(v.getContext(),"Delete "+db.getContact(itemData.getItemName()),Toast.LENGTH_LONG).show();
						AlertDialog.Builder alert= new AlertDialog.Builder(myContext);
						alert.setTitle("Delete Contact");
			            alert.setMessage("Are you sure you want to Delete this Contact?");
			            alert.setPositiveButton("Yes",  new OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
								db.deleteContact(itemData2.getID());
								Intent i = new Intent(myContext,EditContacts.class);
								myContext.startActivity(i);
								((Activity) myContext).finish();
							   }
					    });
			            alert.setNegativeButton("Cancel", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
			            alert.show();
					}
				});
				
				
				
			//edit button on click listener
			holder.edit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(v.getContext(),"edit options coming soon", Toast.LENGTH_SHORT).show();
				
					int id=db.getId(itemdata.getItemName());
					showdialog(itemdata.getItemName(), itemdata.getItemContact(),((BitmapDrawable) itemdata.getIcon()).getBitmap(), id);
						
					
				}
			});
		
		return convertView;
	}
	
	
	public  void onActivityResult(int requestCode, int resultCode, Intent data) {
		 switch (requestCode) {
		    case FROM_GALLERY:
		      {
		        if (resultCode == Activity.RESULT_OK) {
		            Uri selectedImage = data.getData();

		            Intent cropIntent = new Intent("com.android.camera.action.CROP");
		            cropIntent.setDataAndType(selectedImage, "image/*");
		            cropIntent.putExtra("crop", "true");
		            cropIntent.putExtra("aspectX", 1);
		            cropIntent.putExtra("aspectY", 1);
		            cropIntent.putExtra("outputX", 200);
		            cropIntent.putExtra("outputY", 200);
		            
		            cropIntent.putExtra("return-data", true);
		            ((Activity) myContext).startActivityForResult(cropIntent, PICTURE_CROP);
		        }
		        break;
		    }
		    case PICTURE_CROP: {
		        if (resultCode == Activity.RESULT_OK) {
		            final Bundle extras = data.getExtras();

		            if (extras != null) {
		                newPhoto = extras.getParcelable("data");
		                showdialog(name_aftr, contac_aftr,newPhoto , id_aftr);
		                // Hurray! You now have the photo as a Bitmap
		            }
		        }
		        break;
		    }
		    }
	}
	
	
	public void showdialog(final String name,final String contact,final Bitmap newPhoto,final int id){
		final AlertDialog dialogDetails;
		LayoutInflater inflater = LayoutInflater.from(myContext);
		final View dialogview = inflater.inflate(R.layout.dialog_edit_or_enter_contact, null);
		AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(myContext);
		dialogbuilder.setView(dialogview);
		dialogDetails = dialogbuilder.create();
		dialogDetails.setCancelable(false);
		dialogDetails.setCanceledOnTouchOutside(false);
		dialogDetails.show();
		
		//intialising the views in the dialog view 
		final ImageView photo = (ImageView) dialogview.findViewById(R.id.editImageView);
		Button pickImageButton = (Button) dialogview.findViewById(R.id.pickImageButton);
		final EditText nameEditText =(EditText) dialogview.findViewById(R.id.nameEditText);
		final EditText contactEditText =(EditText) dialogview.findViewById(R.id.contactEditText);
		Button saveEditButton = (Button) dialogview.findViewById(R.id.saveButton);
		Button cancelButton = (Button) dialogview.findViewById(R.id.dialogCancelButton);
		TextView heading = (TextView) dialogview.findViewById(R.id.dialogHeading);
		TextView message = (TextView) dialogview.findViewById(R.id.dialogMessage);
		
		
		heading.setText("Edit Contact");
		message.setVisibility(4);
		photo.setImageBitmap(newPhoto);     //setting the photo of imageview
		nameEditText.setText(name);   //setting the editext to name
		contactEditText.setText(contact); //setting the editext to number
		
		//dismiss dialog box on click of cancel button
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogDetails.dismiss();
			}
		});
		
		
		//pick image button for choosing image from gallery
		pickImageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//int id=db.getId(itemdata.getItemContact());
				name_aftr=nameEditText.getText().toString();
				contac_aftr=contactEditText.getText().toString();
				id_aftr=id;
				Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				((Activity) myContext).startActivityForResult(pickPhoto, FROM_GALLERY);
				
			}

			
		});
		
	
	    //save button 
		saveEditButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//getting original name contact and image of the contact
				String oldName,oldNumber,newName,newNumber;
				Bitmap originalPhoto;
				Bitmap newPhotoBitmap;
				oldName=db.getName(id);
				oldNumber=db.getContact(id);
				originalPhoto=db.getPhoto(id);
				newPhotoBitmap= newPhoto;
				//the values in the edit texts of dialog box
				newName=nameEditText.getText().toString();
				newNumber=contactEditText.getText().toString();
				//flags
				boolean is_updated=false;
				boolean is_valid=true;
				
				if(!validator.isPhoneNumberValid(newNumber)){
					is_valid=false;
					Toast.makeText(v.getContext(),"Mobile number not Valid",Toast.LENGTH_LONG).show();
					//showdialog(newName, newNumber, newPhoto, id, "Please enter a valid Mobile Number.");
				}
				
				//if contact is valid then proceed for updation
				if(is_valid){  
					
					
					//on compraision if values updated updating the database
					if(oldName==null || oldNumber==null){
						db.updateNameNumber(newName, newNumber, id);
						Toast.makeText(v.getContext(),"Update succesful",Toast.LENGTH_LONG).show();
						is_updated=true;
					
					}else{
						//Toast.makeText(v.getContext(),"oldnum: "+oldNumber+" newnum: "+newNumber,Toast.LENGTH_LONG).show();
							if(!oldName.equals(newName) || !oldNumber.equals(newNumber)){

								//Toast.makeText(v.getContext(),"values change id:"+id,Toast.LENGTH_LONG).show();
								db.updateNameNumber(newName, newNumber, id);
								Toast.makeText(v.getContext(),"Update succesful",Toast.LENGTH_LONG).show();
								is_updated=true;
							} 
					}
				
				    //check if the photo changed if changed update the database
					if(!originalPhoto.sameAs(newPhoto)){
						//Toast.makeText(v.getContext(),"values change id:"+id,Toast.LENGTH_LONG).show();
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						CustomEditContactsAdapter.this.newPhoto.compress(Bitmap.CompressFormat.PNG,100, stream);
						db.updatePhoto(stream.toByteArray(), id);
						is_updated=true;
					}
					
					
					//on update reload the page otherwise dismiss
					if(is_updated){
						Intent i = new Intent(myContext,Contacts.class);
						myContext.startActivity(i);
						((Activity) myContext).finish();
					}else{
						dialogDetails.dismiss();
					}
					
				} // end of is valid
				
				
			}
		});
	}

}
