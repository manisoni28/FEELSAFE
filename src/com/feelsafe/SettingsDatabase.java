package com.feelsafe;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingsDatabase extends SQLiteOpenHelper{
	   public static final String DATABASE_NAME = "db_contacts.db";
	   public static final String TABLE_NAME = "table_settings";
	   Cursor c;
	   SQLiteDatabase db;
	   Context context;

	public SettingsDatabase(Context context) {
		super(context, DATABASE_NAME, null, 2);
		// TODO Auto-generated constructor stub
		 this.context=context;
	}	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists table_settings(attr VARCHAR,value VARCHAR);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public String getSiren(){
		String siren="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select value from "+TABLE_NAME+" where attr='siren'", null);
		while(c.moveToNext()){
			siren=c.getString(0);
		}
		return siren;
	}
	
	public void setSiren(String siren){
		db.execSQL("update table_settings set value='"+siren+"' where attr='siren'");
	}
	
	
	
	
	public String getPanicModeInterval(){
		String siren="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select value from "+TABLE_NAME+" where attr='panic_interval'", null);
		while(c.moveToNext()){
			siren=c.getString(0);
		}
		return siren;
	}
	public void setPanicModeInterval(String interval){
		db.execSQL("update table_settings set value='"+interval+"' where attr='panic_interval'");
	}
	
	
	
	public String getPanicModeMessage(){
		String mode="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select value from "+TABLE_NAME+" where attr='panic_mode'", null);
		while(c.moveToNext()){
			mode=c.getString(0);
		}
		return mode;
	}
	public void setPanicModeMessage(String mode){
		db.execSQL("update table_settings set value='"+mode+"' where attr='panic_mode'");
	}
	
	
	
	
	public String getTrackMeInterval(){
		String interval="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select value from "+TABLE_NAME+" where attr='timer_interval'", null);
		while(c.moveToNext()){
			interval=c.getString(0);
		}
		return interval;
	}
	public void setTrackMeInterval(String interval){
		db.execSQL("update table_settings set value='"+interval+"' where attr='timer_interval'");
	}
	
	
	
	
	public String getSMSAlert(){
		String alert="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select value from "+TABLE_NAME+" where attr='SMS_alert_sound'", null);
		while(c.moveToNext()){
			alert=c.getString(0);
		}
		return alert;
	}
	public void setSMSAlert(String interval){
		db = this.getWritableDatabase();
		db.execSQL("update table_settings set value='"+interval+"' where attr='SMS_alert_sound'");
	}

	
	public String getTrackMeSMS(){
		String alert="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select value from "+TABLE_NAME+" where attr='Track_me_SMS'", null);
		while(c.moveToNext()){
			alert=c.getString(0);
		}
		return alert;
	}
	public void setTrackMeSMS(String interval){
		db = this.getWritableDatabase();
		db.execSQL("update table_settings set value='"+interval+"' where attr='Track_me_SMS'");
	}
	
	
	public String getcontact1status(){
		String alert="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select value from "+TABLE_NAME+" where attr='contact1'", null);
		while(c.moveToNext()){
			alert=c.getString(0);
		}
		return alert;
	}
	public void setcontact1status(String interval){
		db = this.getWritableDatabase();
		db.execSQL("update table_settings set value='"+interval+"' where attr='contact1'");
	}
	
	public String getcontact2status(){
		String alert="";
		db = this.getWritableDatabase();
		c=db.rawQuery("select value from "+TABLE_NAME+" where attr='contact2'", null);
		while(c.moveToNext()){
			alert=c.getString(0);
		}
		return alert;
	}
	public void setcontact2status(String interval){
		db = this.getWritableDatabase();
		db.execSQL("update table_settings set value='"+interval+"' where attr='contact2'");
	}
}
