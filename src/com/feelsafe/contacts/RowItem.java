package com.feelsafe.contacts;

import android.graphics.drawable.Drawable;

public class RowItem {
	String itemName;
	Drawable icon;
	int ID;
	Boolean isPrime;
	String itemContact;
	public RowItem(String itemName, String itemContact,Drawable icon,Boolean isPrime,int ID) {
		super();
		this.itemName = itemName;
		this.icon = icon;
		this.isPrime = isPrime;
		this.itemContact = itemContact;
		this.ID=ID;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getItemContact() {
		return itemContact;
	}
	public void setItemContact(String itemContact) {
		this.itemContact = itemContact;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public boolean is_prime(){
		return isPrime;
	}
	
	public void setPrime(boolean prime){
		this.isPrime=prime;
	}

}
