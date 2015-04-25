package com.feelsafe.contacts;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.feelsafe.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class EditContacts extends Activity{
	
	ListView l;
	Context myContext;
	List<RowItem> rowItems;
	RowItem itemData;
	CustomEditContactsAdapter adapter;
	ContentValues cvalues;
	ContactsDatabase db;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_edit_page);
		
		myContext=this;
		db=new ContactsDatabase(this);
		l=(ListView)findViewById(R.id.editContactsListView);
		
		
		
		//setting the adapter for the list view
		rowItems = db.getContactsList();
		adapter=new CustomEditContactsAdapter(myContext, rowItems,db);
		l.setAdapter(adapter);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		 MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.contatcs_edit_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		int id=item.getItemId();
		switch (id) {
		case (R.id.contacts_edit_action_changePrime):
		   
			//Toast.makeText(myContext, "Change Primes", Toast.LENGTH_LONG).show();
		
		final AlertDialog dialogDetails1;
		LayoutInflater inflater = LayoutInflater.from(myContext);
		final View dialogview = inflater.inflate(R.layout.dialog_change_prime_contacts, null);
		AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(myContext);
		dialogbuilder.setView(dialogview);
		dialogDetails1 = dialogbuilder.create();
		dialogDetails1.show();
		
		final ContactsDatabase cdb = new ContactsDatabase(myContext);
	
		ListView l=(ListView)dialogview.findViewById(R.id.changePrimeDialogListView);
		rowItems = cdb.getContactsList();
		CustomChangePrimesAdapter pAdapter=new CustomChangePrimesAdapter(myContext, rowItems);
		l.setAdapter(pAdapter);
		
		Button cancel=(Button)dialogview.findViewById(R.id.changeprimeCancelButton);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogDetails1.dismiss();
			}
		});
		
		Button set=(Button)dialogview.findViewById(R.id.changePrimeSetButton);
		set.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<rowItems.size();i++){
					if(rowItems.get(i).is_prime()){
						//Toast.makeText(myContext, ""+rowItems.get(i).getItemName(), Toast.LENGTH_SHORT).show();
						cdb.setIdAsPrime(rowItems.get(i).getID(), 1);
					}else{
						cdb.setIdAsPrime(rowItems.get(i).getID(), 0);
					}
				}
				
				
				dialogDetails1.dismiss();
				Intent i = new Intent(myContext,EditContacts.class);
				startActivity(i);
				finish();
			}
		});
		
		
		
			break;

		default:
			break;
		} 
		
		return super.onOptionsItemSelected(item);
		
		
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    adapter.onActivityResult(requestCode, resultCode, data);
	}
	
    @Override
    public void onBackPressed() {
        super.onBackPressed(); 
        Intent i=new Intent(EditContacts.this,Contacts.class);
        startActivity(i);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }
}
