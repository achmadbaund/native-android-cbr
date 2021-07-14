package com.workshop.menusehatku.database;

import java.util.List;

import com.workshop.menusehatku.NamaGejala;
import com.workshop.menusehatku.R;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class GejalaAdapter extends ArrayAdapter<NamaGejala> {

	private List<NamaGejala> list;
	private LayoutInflater inflator;
	public GejalaAdapter(Activity context, List<NamaGejala> list) {
		super(context, R.layout.row, list);
		this.list = list;
		inflator = context.getLayoutInflater();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflator.inflate(R.layout.row, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.chk = (CheckBox) convertView.findViewById(R.id.checkbox);
			holder.chk
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton view,
								boolean isChecked) {
							int getPosition = (Integer) view.getTag();
							list.get(getPosition).setSelected(view.isChecked());
							//list.get(getPosition).getName()
							Log.v("MenuPuasa", "clicked id " + list.get(getPosition)+ " " + list.get(getPosition).getGejala() + " = " + list.get(getPosition).isSelected());	
						}
					});
			convertView.setTag(holder);
			convertView.setTag(R.id.title, holder.title);
			convertView.setTag(R.id.checkbox, holder.chk);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.chk.setTag(position);

		holder.title.setText(list.get(position).getGejala());
		holder.chk.setChecked(list.get(position).isSelected());
		return convertView;
	}

	static class ViewHolder {
		protected TextView title;
		protected CheckBox chk;
	}
}
