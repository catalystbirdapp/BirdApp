package com.catalyst.android.birdapp.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.catalyst.android.birdapp.BirdFormActivity;

public class AlertDialogFragment extends DialogFragment implements
		DialogInterface.OnClickListener {

	public static AlertDialogFragment newInstance(String message) {

		AlertDialogFragment adf = new AlertDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("alert-message", message);
		adf.setArguments(bundle);

		return adf;
	}

	@Override
	public void onAttach(Activity act) {
		// If the activity we're being attached to has
		// not implemented the OnDialogDoneListener
		// interface, the following line will throw a
		// ClassCastException. This is the earliest we
		// can test if we have a well-behaved activity.
		try {
			@SuppressWarnings("unused")
			OnDialogDoneListener test = (OnDialogDoneListener) act;
		} catch (ClassCastException cce) {
			// Here is where we fail gracefully.
			Log.e(BirdFormActivity.LOGTAG, "Activity is not listening");
		}
		super.onAttach(act);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setCancelable(true);
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		setStyle(style, theme);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
				.setTitle("Alert!!!").setPositiveButton("Yes", this)
				.setNegativeButton("No", this)
				.setMessage(this.getArguments().getString("alert-message"));
		return b.create();
	}

	public void onClick(DialogInterface dialog, int which) {
		OnDialogDoneListener act = (OnDialogDoneListener) getActivity();
		boolean cancelled = false;
		if (which == AlertDialog.BUTTON_NEGATIVE) {
			cancelled = true;
			act.onDialogDone(getTag(), cancelled, "Alert dismissed");
		} else {

			act.onDialogDone(getTag(), cancelled, "Sighting will be submitted");
		}
	}
}
