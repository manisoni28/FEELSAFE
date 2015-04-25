package com.feelsafe.contacts;

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
import android.net.Uri;
import android.opengl.Visibility;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomContactsAdapter extends BaseAdapter{
	
	Context myContext;
    LayoutInflater inflater;
    List<RowItem> data;
    ContactsDatabase db;

    public CustomContactsAdapter(Context myContext,List<RowItem> items,ContactsDatabase db) {
        
        inflater= LayoutInflater.from(myContext);
        this.myContext=myContext;
        this.data = items;
        this.db=db; 
    }
    
    public class ViewHolder {
        ImageView Photo;
        TextView Name;
        Button call;
        Button message;
        Button crown;
     //   Button edit;
     //   Button delete;
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
			convertView=inflater.inflate(R.layout.contacts_list_row, null);
			holder = new ViewHolder();
            holder.Photo=(ImageView) convertView.findViewById(R.id.contactImage);
            holder.Name=(TextView) convertView.findViewById(R.id.contactName);
            holder.call=(Button) convertView.findViewById(R.id.contactCallButton);
            holder.message=(Button) convertView.findViewById(R.id.contactMessage);
            holder.crown=(Button) convertView.findViewById(R.id.crown);
          //  holder.delete=(Button) convertView.findViewById(R.id.contactDeleteButton);
          //  holder.edit=(Button) convertView.findViewById(R.id.contactEditButton);
            convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final RowItem itemdata=(RowItem) getItem(position);
		
		holder.Photo.setImageDrawable(itemdata.getIcon());
		holder.Name.setText(itemdata.getItemName());
		holder.crown.setVisibility(4); //setting the crown invisible
		if(itemdata.is_prime()){
			holder.crown.setVisibility(2); //visibility for prime contacts
		}
		
		//rowitem that store the details of current row
		final RowItem itemData= data.get(position);
		
		
		//+db.getContact(itemData.getItemName())
		//call button on click listener
		
		holder.call.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    //Toast.makeText(v.getContext(),"call "+db.getContact(itemData.getItemName()),Toast.LENGTH_LONG).show();
				
				//=============Alert For Call======================
				AlertDialog.Builder alert = new AlertDialog.Builder(myContext);
				alert.setTitle("Call "+itemdata.getItemName());
	            alert.setMessage("Are you sure you want to call "+itemdata.getItemName()+" ?");
	            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						try{
						Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+itemdata.getItemContact()));
						myContext.startActivity(callIntent);
						}catch(Exception e){
							Toast.makeText(myContext, "Calling not supported", Toast.LENGTH_LONG).show();
						}
					}
				});
	            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				alert.show();
				//===============================================
			
			}
		});
		
		
		
		//message button on click listener
		holder.message.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(v.getContext(),"Message "+db.getContact(itemData.getItemName()),Toast.LENGTH_LONG).show();
				try{
					Intent smsIntent = new Intent(Intent.ACTION_VIEW);
					smsIntent.setType("vnd.android-dir/mms-sms");
					smsIntent.putExtra("address",itemdata.getItemContact());
					myContext.startActivity(smsIntent);
				}catch(Exception e){
					Toast.makeText(myContext, "Messaging not supported", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		
		
		return convertView;
	}
	
	
	

}
