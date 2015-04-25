package com.feelsafe.contacts;

import java.util.ArrayList;
import java.util.List;

import com.feelsafe.R;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Toast;


public class ContactsDatabase extends SQLiteOpenHelper{
	public static final String DATABASE_NAME = "db_contacts.db";
	   public static final String TABLE_NAME = "table_contacts";
	   Resources res;
	   Cursor c;
	   SQLiteDatabase db;
	   Context context;
	   
	public ContactsDatabase(Context context) {
		super(context, DATABASE_NAME, null, 2);
		// TODO Auto-generated constructor stub
		 this.context=context;
		 res= context.getResources();   //later used in getContactsList()
	}	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists "+TABLE_NAME+"(name VARCHAR,contact VARCHAR,image TEXT,is_prime BOOLEAN,ID INTEGER PRIMARY KEY);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean addContact(String name,String contact,byte[] bs,boolean is_prime){
		
		 db = this.getWritableDatabase();
	    ContentValues contentValues = new ContentValues();
	    contentValues.put("name", name);
	    contentValues.put("image", bs);
	    contentValues.put("contact", contact);
	    contentValues.put("is_prime", is_prime);
		db.insert(TABLE_NAME, null, contentValues);
		
		return true;
	}
	
/*	public void deleteContact(String name){
		 db = this.getWritableDatabase();
		db.execSQL("delete from table_contacts where name='"+name+"'");
		return;
	} */
	public void deleteContact(int ID){
		 db = this.getWritableDatabase();
		db.execSQL("delete from table_contacts where ID="+ID);
		return;
	}
	
	public int updateNameNumber(String name,String contact,int id){
		db = this.getWritableDatabase();
		db.execSQL("update table_contacts set name='"+name+"', contact='"+contact+"' where ID="+id);
		return 1;
	}
	
	public int updatePhoto(byte[] bs,int id){
		db = this.getWritableDatabase();
		 ContentValues contentValues = new ContentValues();
		    contentValues.put("image", bs);
		    db.update(TABLE_NAME, contentValues, "ID = ?", new String[] { Integer.toString(id) });
		return 1;
	}
	
	
	
	public String getContact(String name){
		String contact="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select contact from table_contacts where name='"+name+"'",null);
		while(c.moveToNext()){
			contact=c.getString(0);
		}
		return contact;
	}
	public String getContact(int ID){
		String contact="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select contact from table_contacts where ID="+ID,null);
		while(c.moveToNext()){
			contact=c.getString(0);
		}
		return contact;
	}
	public String getName(int ID){
		String name="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select name from table_contacts where ID="+ID,null);
		while(c.moveToNext()){
			name=c.getString(0);
		}
		return name;
	}
	
	public String getName(String contact){
		String name="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select name from table_contacts where contact='"+contact+"'",null);
		while(c.moveToNext()){
			name=c.getString(0);
		}
		return name;
	}
	public Bitmap getPhoto(int id){
		Bitmap photo1= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_person) ;
		db = this.getWritableDatabase();
		c=db.rawQuery("select image from table_contacts where ID="+id,null);
		while(c.moveToNext()){
			byte[] imageByteArray = c.getBlob(0);
	    	 photo1 = BitmapFactory.decodeByteArray(imageByteArray,0,imageByteArray.length);
		}
		
		return photo1;
	}
	public byte[] getphotoblob(int id){
		db = this.getWritableDatabase();
		c=db.rawQuery("select image from table_contacts where ID="+id,null);
		c.moveToNext();
		byte[] imageByteArray = c.getBlob(0);
		return imageByteArray;
	}
	
	
	public List<RowItem> getContactsList(){
		SQLiteDatabase db = this.getWritableDatabase();
		List<RowItem> temp = new ArrayList<RowItem>();
		 c = db.rawQuery("select * from table_contacts order by is_prime desc", null);
		while(c.moveToNext()){
        	byte[] imageByteArray = c.getBlob(2);
        	Bitmap photo1 = BitmapFactory.decodeByteArray(imageByteArray,0,imageByteArray.length);
        	//Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.contacts);
        	boolean value = c.getInt(3)>0;
        	temp.add(new RowItem(""+c.getString(0),""+c.getString(1),new BitmapDrawable(res,photo1),value,c.getInt(4)));
        
        }  
		
		return temp;
		
	}
	
	public List<String> getContactNumberList(){
		SQLiteDatabase db = this.getWritableDatabase();
		List<String> temp = new ArrayList<String>();
		 c = db.rawQuery("select contact from table_contacts order by is_prime desc", null);
		while(c.moveToNext()){
        	temp.add(""+c.getString(0));
        } 
		return temp;
	}
	
	public int getPrimeContactsCount(){
		int count=0;
		db = this.getWritableDatabase();
		c=db.rawQuery("select is_prime from table_contacts",null);
		while(c.moveToNext()){
	     boolean value = c.getInt(0)>0;
		 if(value){
			 count=count+1;
		 }
		}
		return count;
	}
	
	public List<String> getPrimeContactNames(){
		List<String> list= new ArrayList<String>();
		db = this.getWritableDatabase();
		c=db.rawQuery("select name,is_prime from table_contacts",null);
		while(c.moveToNext()){
			boolean value = c.getInt(1)>0;
			 if(value){
				 list.add(c.getString(0));
			 }
		}
		return list;
	}
	
	public int getContactsCount(){
		db = this.getWritableDatabase();
		c=db.rawQuery("select contact from table_contacts",null);
		return c.getCount();
	}
	
	public void deleteAll(){
		db = this.getWritableDatabase();
		db.execSQL("delete from table_contacts");
	}
	
	public int getId(String name){
		int id=-1;
		db = this.getWritableDatabase();
		c=db.rawQuery("select ID from table_contacts where name='"+name+"'",null);
		while(c.moveToNext()){
			id=c.getInt(0);
		}
		return id;
	}
	public int getId(String name,String number){
		int id=-1;
		db = this.getWritableDatabase();
		c=db.rawQuery("select ID from table_contacts where name='"+name+"' and contact='"+number+"'",null);
		while(c.moveToNext()){
			id=c.getInt(0);
		}
		return id;
	}
	public ArrayList<String> getContacts(){
		SQLiteDatabase db;
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT name,contact FROM table_contacts", null);
		cursor.moveToFirst();
		ArrayList<String> name = new ArrayList<String>();
		while(!cursor.isAfterLast()) {
			name.add(cursor.getString(cursor.getColumnIndex("name"))+"("+cursor.getString(cursor.getColumnIndex("contact"))+")");
			cursor.moveToNext();
		}
		cursor.close();
		return name;
	}
	
	public void setIdAsPrime(int id,int b){ 
		db = this.getWritableDatabase();
		db.execSQL("update table_contacts set is_prime="+b+" where ID="+id);
	}
}
