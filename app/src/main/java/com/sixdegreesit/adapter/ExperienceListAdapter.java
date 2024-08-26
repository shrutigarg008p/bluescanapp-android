package com.sixdegreesit.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sixdegreesit.securityapp.R;
import com.sixdegreesit.utils.GaurdProfileDTO.ExperienceDTO;

public class ExperienceListAdapter extends ArrayAdapter<ExperienceDTO> {

	Context context;
	int layoutResourceId;
	List<ExperienceDTO> eventsList = null;
	ViewHolder holder = null;
	ExperienceDTO events = null;
	
	public ExperienceListAdapter(Context context, int layoutResourceId,
			List<ExperienceDTO> data) {
		super(context, layoutResourceId, data);
		// TODO Auto-generated constructor stub
		try {
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.eventsList = data;			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class ViewHolder {
		TextView tvName,tvExp;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_empdetails, parent, false);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name_item);
				holder.tvExp = (TextView) convertView.findViewById(R.id.tv_exp_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			events = eventsList.get(position);
			holder.tvName.setText(events.getCompany_name());	
			holder.tvExp.setText(events.getExp_duration_year()+" years & "+events.getExp_duration_month()+" months");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}

}
