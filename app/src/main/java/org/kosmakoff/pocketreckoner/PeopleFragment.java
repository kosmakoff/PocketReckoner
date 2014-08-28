/**
 *
 */
package org.kosmakoff.pocketreckoner;

import java.util.ArrayList;

import org.kosmakoff.pocketreckoner.data.PeopleRepository;
import org.kosmakoff.pocketreckoner.data.Person;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author okosmakov
 */
public class PeopleFragment extends ListFragment
        implements OnItemClickListener, AdapterView.OnItemLongClickListener {
    private PeopleRepository peopleRepository;

    public PeopleFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Person> people = peopleRepository.getPeople();
        PeopleAdapter adapter = new PeopleAdapter(getActivity(), people);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        peopleRepository = new PeopleRepository(getActivity());

        return inflater.inflate(R.layout.fragment_people, null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Person person = (Person) adapterView.getItemAtPosition(position);

        String message = String.format("Clicked on '%s'", person.getName());

        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        Person person = (Person) adapterView.getItemAtPosition(position);

        String message = String.format("Long-clicked on '%s'", person.getName());

        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        toast.show();

        return true;
    }
}
