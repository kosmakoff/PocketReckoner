/**
 * 
 */
package org.kosmakoff.pocketreckoner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author okosmakov
 *
 */
public class SessionsFragment extends Fragment {
	public SessionsFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_sessions, container, false);
		return rootView;
	}	
}
