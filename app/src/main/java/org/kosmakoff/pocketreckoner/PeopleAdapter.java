package org.kosmakoff.pocketreckoner;

import java.util.ArrayList;

import org.kosmakoff.pocketreckoner.data.Person;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PeopleAdapter extends BaseAdapter {
	public Context getContext() {
		return context;
	}

	public ArrayList<Person> getPeople() {
		return people;
	}

	private Context context;

	private ArrayList<Person> people;
	
	public PeopleAdapter(Context context, ArrayList<Person> people) {
		this.context = context;
		this.people = people;
	}

	@Override
	public int getCount() {
		return people.size();
	}

	@Override
	public Object getItem(int position) {
		return people.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ((Person)getItem(position)).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;

		if (convertView != null) {
			view = convertView;
		} else {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.person_list_item,
					parent, false);
		}
		
		Person person = (Person) getItem(position);
		
		TextView tvName = (TextView) view.findViewById(R.id.text_name);
		tvName.setText(person.getName());
		
		TextView tvPhone = (TextView) view.findViewById(R.id.text_phone);
		tvPhone.setText(person.getPhone());
		
		TextView tvEmail = (TextView) view.findViewById(R.id.text_email);
		tvEmail.setText(person.getEmail());
		
		return view;
	}
}
