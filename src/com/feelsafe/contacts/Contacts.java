package com.feelsafe.contacts;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.feelsafe.MainActivity;
import com.feelsafe.R;
import android.net.Uri;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.DisplayMetrics;

public class Contacts extends Activity{
	
	ListView l;
	Context myContext;
	List<RowItem> rowItems;
	RowItem itemData;
	CustomContactsAdapter adapter;
	private Uri uriContact;
	ContentValues cvalues;

	
	
	private String contactID,contactName,contactNumber;
	ContactsDatabase db;
	//global values that store name and contact of itemdata for startactivityafterresult
    String name_aftr,contac_aftr;
     int id_aftr;
    //request codes
    final int FROM_GALLERY=3,FROM_CAMERA=4,PICTURE_CROP=5,choose_prime_contact_request=2;
  //bitmap to store new image after star activity for result
    Bitmap newPhoto=null;
    Validation validator=new Validation(this);
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_page);
		
		myContext=this;
		db=new ContactsDatabase(this);
		l=(ListView)findViewById(R.id.listView1);
		
		
		//db.deleteAll();
		//setting the adapter for the list view
		rowItems = db.getContactsList();
		adapter=new CustomContactsAdapter(myContext, rowItems,db);
		l.setAdapter(adapter);
		
		
		
		//check if the primary contacts are stored
		//Toast.makeText(myContext, "prime contacts: "+db.getPrimeContactsCount(), Toast.LENGTH_SHORT).show();
		if(db.getPrimeContactsCount() <=1){
			getPrimeContacts();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		 MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.contatcs_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId()) {
		case (R.id.contacts_action_addButton):
			startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 1);
		
			break;
		case (R.id.contacts_action_editButton):
			Intent i1=new Intent(Contacts.this,EditContacts.class);
		    startActivity(i1);
		    finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1 && resultCode == RESULT_OK){
			 uriContact = data.getData();
			 retrieveContactName();
			// Toast.makeText(getApplicationContext(), contactName, Toast.LENGTH_SHORT).show();
	         retrieveContactNumber();
	         //Toast.makeText(getApplicationContext(),"contact number: "+contactNumber, Toast.LENGTH_SHORT).show();
	        
	         Bitmap photo=retrieveContactPhoto();
	         Bitmap bitmap=photo;
	         ByteArrayOutputStream stream = new ByteArrayOutputStream();
	         bitmap.compress(Bitmap.CompressFormat.PNG,100, stream);
	         
	         if(contactName==null){
	 			 contactName="Ex: Bunny";
	 		 }
			
	         db=new ContactsDatabase(this);
	         db.addContact(contactName,contactNumber, stream.toByteArray(), false);
	         itemData=new RowItem(contactName,contactNumber,new BitmapDrawable(getResources(),photo),false,db.getId(contactName, contactNumber));
	         rowItems.add(itemData);
	 		 adapter.notifyDataSetChanged(); 
	 		 //contactNumber=contactNumber.replace(" ","");
	 		//contactNumber.trim();
	 		 
	 		
	 		 
	 		 if(contactNumber==null || contactNumber.equals("Ex: 9876543210")){
	 			
	        	 Toast.makeText(getApplicationContext(),"Couldnt import contact enter manually", Toast.LENGTH_LONG).show();
	        	 String message="Contact cannot be imported! The details of contacts saved by Third party apps cannot be imported directly.\n Please enter the details manually.";
	        	showdialog(contactName, contactNumber,photo,db.getId(contactName),message);
	        	 
	         }
	 		 
	 		 else if(!validator.isPhoneNumberValid(contactNumber)){
	 			 Toast.makeText(getApplicationContext(),"Contact number is not a mobile number", Toast.LENGTH_LONG).show();
	 			 String message2="The given number is not a Mobile number. Land-line numbers and other contacts are not supported. Please enter a valid mobile number.";
	        	 showdialog(contactName, contactNumber,photo,db.getId(contactName),message2); 
	 		 }
	 		//Toast.makeText(getApplicationContext(),""+db.getId(contactName), Toast.LENGTH_SHORT).show();
		}
		if (requestCode == 2 && resultCode == RESULT_OK){
			 uriContact = data.getData();
			 retrieveContactName();
			// Toast.makeText(getApplicationContext(), contactName, Toast.LENGTH_SHORT).show();
	         retrieveContactNumber();
	       //  Toast.makeText(getApplicationContext(), contactNumber, Toast.LENGTH_SHORT).show();
	         Bitmap photo=retrieveContactPhoto();
	         Bitmap bitmap=photo;
	         ByteArrayOutputStream stream = new ByteArrayOutputStream();
	         bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			 
	         if(contactName==null){
	 			 contactName="Ex: Bunny";
	 		 }
	         
	         db=new ContactsDatabase(this);
	         db.addContact(contactName,contactNumber, stream.toByteArray(), true);
	         itemData=new RowItem(contactName,contactNumber,new BitmapDrawable(getResources(),photo),true,db.getId(contactName, contactNumber));
	         rowItems.add(itemData);
	 		 adapter.notifyDataSetChanged(); 
	 		
	 		//contactNumber.trim();
	 		 if(contactNumber==null || contactNumber.equals("Ex: 9876543210")){
	 			 
	        	 Toast.makeText(getApplicationContext(),"Couldnt import contact enter manually", Toast.LENGTH_LONG).show();
	        	 String message="Mobile cannot be imported! The details of contacts saved by Third party apps cannot be imported directly.\n Enter the details manually.";
	        	  
	        	 showdialog(contactName, contactNumber,photo,db.getId(contactName),message);
	        	 return;
	         }
	 		
	 		 else if(!validator.isPhoneNumberValid(contactNumber)){
	 			 Toast.makeText(getApplicationContext(),"Contact number is not a mobile number", Toast.LENGTH_LONG).show();
	 			 String message2="The given number is not a Mobile number. Land-line numbers and other contacts are not supported. Please enter a valid mobile number.";
	        	 showdialog(contactName, contactNumber,photo,db.getId(contactName),message2);
	        	 return;
	 		 }
	 		// Toast.makeText(getApplicationContext(),""+db.getId(contactName), Toast.LENGTH_SHORT).show();
	         if(db.getPrimeContactsCount()<2){
	        	 Toast.makeText(getApplicationContext(),"One more contact required", Toast.LENGTH_SHORT).show();
	        	 getPrimeContacts();
	         }
	 		 
		}
		if (requestCode == FROM_GALLERY && resultCode == RESULT_OK){
			  Uri selectedImage = data.getData();

	            Intent cropIntent = new Intent("com.android.camera.action.CROP");
	            cropIntent.setDataAndType(selectedImage, "image/*");
	            cropIntent.putExtra("crop", "true");
	            cropIntent.putExtra("aspectX", 1);
	            cropIntent.putExtra("aspectY", 1);
	            cropIntent.putExtra("outputX", 200);
	            cropIntent.putExtra("outputY", 200);
	            cropIntent.putExtra("return-data", true);
	            startActivityForResult(cropIntent, PICTURE_CROP);
		}
		
		if (requestCode == PICTURE_CROP && resultCode == RESULT_OK){
			final Bundle extras = data.getExtras();

            if (extras != null) {
                newPhoto = extras.getParcelable("data");
                showdialog(name_aftr, contac_aftr,newPhoto , id_aftr,"New Image Set. Click Save to finish.");
            }
		}
		
	}
	
	
	private Bitmap retrieveContactPhoto() {
		 
        Bitmap photo = null;
 
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
 
            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            }else{
            	photo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_person);
            }
            assert inputStream != null;
            inputStream.close();
 
        } catch (Exception e) {
        	photo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_person);
            //e.printStackTrace();
        }
        return photo;
    }
	
	
	private void retrieveContactNumber() {
		 try {
			
		
        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        
        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
 
        cursorID.close();
 
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
 
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
 
                new String[]{contactID},
                null);
 
        if (cursorPhone.moveToFirst()) {;
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            
        }
 
        cursorPhone.close();
		 } catch (Exception e) {
				// TODO: handle exception
			 contactNumber="Ex: 9876543210";
			}
 
    }
 
    private void retrieveContactName() {

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
 
        if (cursor.moveToFirst()) {
 
            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
 
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
 
        cursor.close();
 
 
    }
    
    public void getPrimeContacts(){
    	AlertDialog.Builder alert1= new AlertDialog.Builder(myContext);
		alert1.setTitle("Less prime Contacts");
		alert1.setMessage("Exactly two contacts have to be set as Prime Contacts for proper functioning of the app!");
		alert1.setCancelable(false);
		alert1.setPositiveButton("choose Contact",new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), choose_prime_contact_request);
			   }
	    });
		alert1.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//Intent i1=new Intent(Contacts.this,MainActivity.class);
				//startActivity(i1);
				//overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
				finish();
			}
		});
		alert1.show();
    }
    
    
    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }
    
    
    public void showdialog(final String name,final String contact,final Bitmap newPhoto,final int id,String message){
    	/*Intent i = new Intent(myContext,ContactManual.class);
    	i.putExtra("name", name);
    	i.putExtra("contact", contact);
    	i.putExtra("id", id);
    	i.putExtra("message", message);
		myContext.startActivity(i); */
    	
    	
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
		TextView dialogMessage = (TextView) dialogview.findViewById(R.id.dialogMessage);
		
		
		heading.setText("Edit Contact");
		dialogMessage.setText(message);
		photo.setImageBitmap(newPhoto);     //setting the photo of imageview
		nameEditText.setText(name);   //setting the editext to name
		contactEditText.setText(contact); //setting the editext to number
		
		//dismiss dialog box on click of cancel button
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.deleteContact(id);
				dialogDetails.dismiss();
				Intent i = new Intent(myContext,Contacts.class);
				myContext.startActivity(i);
				finish();
			}
		});
		
		//pick image button for choosing image from gallery
		pickImageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//int id=db.getId(itemdata.getItemContact());
				dialogDetails.dismiss();
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
						Contacts.this.newPhoto.compress(Bitmap.CompressFormat.PNG,100, stream);
						db.updatePhoto(stream.toByteArray(), id);
						is_updated=true;
					}
					
					
					//on update reload the page otherwise dismiss
					if(is_updated){
						dialogDetails.dismiss();
						Intent i = new Intent(myContext,Contacts.class);
						myContext.startActivity(i);
						finish();
					}else{
						dialogDetails.dismiss();
					}
					
				} // end of is valid
				
				
			    	
				
			}
		});
		
		
	}
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    
    	
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	adapter.notifyDataSetChanged();
    	adapter.notifyDataSetInvalidated();
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	db.close();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
