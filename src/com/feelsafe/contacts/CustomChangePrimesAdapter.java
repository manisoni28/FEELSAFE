package com.feelsafe.contacts;

import java.util.List;
import com.feelsafe.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class CustomChangePrimesAdapter extends BaseAdapter {
	
	Context myContext;
    LayoutInflater inflater;
    List<RowItem> data;
    int globalInc;

    public CustomChangePrimesAdapter(Context myContext,List<RowItem> items) {
        
        inflater= LayoutInflater.from(myContext);
        this.myContext=myContext;
        this.data = items;
        globalInc= getCheckedCount();
       // Toast.makeText(myContext, "count = " + globalInc, Toast.LENGTH_LONG).show();
    }
    
    public class ViewHolder {
        CheckBox Name;
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
	
	public int getCheckedCount(){
		int i,count=0;
		for(i=0;i<getCount();i++){
			if(data.get(i).is_prime()){
				count=count+1;
			}
		}
		return count;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.contacts_changeprime_list_row, null);
			holder = new ViewHolder();
            holder.Name=(CheckBox) convertView.findViewById(R.id.changePrimeCheckBox1);
            convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final RowItem itemdata=(RowItem) getItem(position);
		
		holder.Name.setText(itemdata.getItemName());
		holder.Name.setChecked(itemdata.is_prime());
		//holder.Name.setOnClickListener(this);
		final CheckBox c = (CheckBox) holder.Name;
		holder.Name.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(c.isChecked()){
					globalInc=globalInc+1;
					if(globalInc>2){
						c.setChecked(false);
						globalInc=globalInc-1;
					}else{
						data.get(position).setPrime(true);
					}
				}
				else{
					globalInc=globalInc-1;
					data.get(position).setPrime(false);
				}
			}
		});
		
		
		return convertView;
	}

	
	
	
}