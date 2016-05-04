package com.example.mcfarland.myruns_sub1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

//MyRunsDialogFragment handles all the customized dialog boxes in our project.
//Differentiated by dialog id.
// 
// Ref: http://developer.android.com/reference/android/app/DialogFragment.html

public class MyRunsDialogFragment extends DialogFragment {

	final private static String TAG = "CREATE DIALOG";

	// Different dialog IDs
	public static final int DIALOG_ID_ERROR = -1;
	public static final int DIALOG_ID_PHOTO_PICKER = 1;

	public static final int DIALOG_MANUAL_ENTRY_DATE = 2;
	public static final int DIALOG_MANUAL_ENTRY_TIME = 3;
	public static final int DIALOG_MANUAL_ENTRY_DURATION = 4;
	public static final int DIALOG_MANUAL_ENTRY_DISTANCE = 5;
	public static final int DIALOG_MANUAL_ENTRY_CALORIES = 6;
	public static final int DIALOG_MANUAL_ENTRY_HR = 7;
	public static final int DIALOG_MANUAL_ENTRY_COMMENT = 8;

	public static final int DIALOG_CONFIRM_CLEAR = 9;

	// For photo picker selection:
	public static final int ID_PHOTO_PICKER_FROM_CAMERA = 0;
	public static final int ID_PHOTO_PICKER_FROM_GALLERY = 1;

	private static final String DIALOG_ID_KEY = "dialog_id";

	Calendar mDateAndTime = Calendar.getInstance();

	public static MyRunsDialogFragment newInstance(int dialog_id) {
		MyRunsDialogFragment frag = new MyRunsDialogFragment();
		Bundle args = new Bundle();
		args.putInt(DIALOG_ID_KEY, dialog_id);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int dialog_id = getArguments().getInt(DIALOG_ID_KEY);

		final Activity parent = getActivity();

		Dialog to_return;
		// Setup dialog appearance and onClick Listeners
		switch (dialog_id) {

			/* --------------------------- DIALOGS FOR SETTINGS ACTIVITY --------------------------- */

			case DIALOG_ID_PHOTO_PICKER:
				AlertDialog.Builder builder = new AlertDialog.Builder(parent);
				builder.setTitle(R.string.ui_profile_photo_picker_title);
				DialogInterface.OnClickListener dlistener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// Item is ID_PHOTO_PICKER_FROM_CAMERA
						// Call the onPhotoPickerItemSelected in the parent
						((Settings) parent).onPhotoPickerItemSelected(item);
					}
				};
				builder.setItems(R.array.ui_profile_photo_picker_items, dlistener);
				return builder.create();

			case DIALOG_CONFIRM_CLEAR:
				AlertDialog.Builder builder_clear = new AlertDialog.Builder(parent);
				builder_clear.setTitle(R.string.settings_confirm_clear_title);

				builder_clear.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Toast.makeText(getActivity(), "Cleared All Data", Toast.LENGTH_SHORT).show();
						((Settings) parent).clearProfile();
					}
				});

				builder_clear.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// exit
					}
				});

				return builder_clear.create();


			/* --------------------------- DIALOGS FOR MANUAL ENTRY --------------------------- */

			case DIALOG_MANUAL_ENTRY_DATE:
				DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker view, int year, int monthOfYear,
										  int dayOfMonth) {
						((ManualEntryActivity)parent).setDate(year, monthOfYear, dayOfMonth);
					}
				};

				DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(),
						AlertDialog.THEME_DEVICE_DEFAULT_DARK,
						mDateListener,
						mDateAndTime.get(Calendar.YEAR),
						mDateAndTime.get(Calendar.MONTH),
						mDateAndTime.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.setTitle(R.string.manual_entry_date_dialog_title);

				return datePickerDialog;

			case DIALOG_MANUAL_ENTRY_TIME:
				TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						((ManualEntryActivity)parent).setTime(hourOfDay, minute);
					}
				};

				TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(),
						AlertDialog.THEME_DEVICE_DEFAULT_DARK,
						mTimeListener,
						mDateAndTime.get(Calendar.HOUR_OF_DAY),
						mDateAndTime.get(Calendar.MINUTE), true);
				timePickerDialog.setTitle(R.string.manual_entry_time_dialog_title);

				return timePickerDialog;

			case DIALOG_MANUAL_ENTRY_DURATION:
				AlertDialog.Builder builder_duration = new AlertDialog.Builder(parent);
				builder_duration.setTitle(R.string.manual_entry_duration_dialog_title);

				final EditText duration_field = new EditText(parent);
				duration_field.setInputType(InputType.TYPE_CLASS_NUMBER);
				duration_field.setHint(R.string.ME_duration_hint);
				builder_duration.setView(duration_field);

				builder_duration.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						((ManualEntryActivity) parent).setDuration(duration_field.getText().toString());
					}
				});

				builder_duration.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// exit
					}
				});

				return builder_duration.create();

			case DIALOG_MANUAL_ENTRY_DISTANCE:
				AlertDialog.Builder builder_distance = new AlertDialog.Builder(parent);
				builder_distance.setTitle(R.string.manual_entry_distance_dialog_title);

				Conversions converter = new Conversions(getActivity());

				final EditText distance_field = new EditText(parent);
				distance_field.setInputType(InputType.TYPE_CLASS_NUMBER);

				if (converter.isPreferenceMetric())
					distance_field.setHint(R.string.ME_distance_hint_metric);
				else
					distance_field.setHint(R.string.ME_distance_hint_imperial);

				builder_distance.setView(distance_field);

				builder_distance.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						((ManualEntryActivity) parent).setDistance(distance_field.getText().toString());
					}
				});

				builder_distance.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// exit
					}
				});

				return builder_distance.create();

			case DIALOG_MANUAL_ENTRY_CALORIES:
				AlertDialog.Builder builder_calories = new AlertDialog.Builder(parent);
				builder_calories.setTitle(R.string.manual_entry_calories_dialog_title);

				final EditText calories_field = new EditText(parent);
				calories_field.setInputType(InputType.TYPE_CLASS_NUMBER);
				builder_calories.setView(calories_field);

				builder_calories.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						((ManualEntryActivity) parent).setCalories(calories_field.getText().toString());
					}
				});

				builder_calories.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// exit
					}
				});

				return builder_calories.create();

			case DIALOG_MANUAL_ENTRY_HR:
				AlertDialog.Builder builder_HR = new AlertDialog.Builder(parent);
				builder_HR.setTitle(R.string.manual_entry_HR_dialog_title);

				final EditText HR_field = new EditText(parent);
				HR_field.setInputType(InputType.TYPE_CLASS_NUMBER);
				builder_HR.setView(HR_field);

				builder_HR.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						((ManualEntryActivity) parent).setHeartRate(HR_field.getText().toString());
					}
				});

				builder_HR.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// exit
					}
				});

				return builder_HR.create();

			case DIALOG_MANUAL_ENTRY_COMMENT:
				AlertDialog.Builder builder_comment = new AlertDialog.Builder(parent);
				builder_comment.setTitle(R.string.manual_entry_comment_dialog_title);

				final EditText comment_field = new EditText(parent);
				comment_field.setInputType(InputType.TYPE_CLASS_TEXT);
				comment_field.setHint(R.string.manual_entry_comment_dialog_prompt);
				builder_comment.setView(comment_field);

				builder_comment.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						((ManualEntryActivity) parent).setComment(comment_field.getText().toString());
					}
				});

				builder_comment.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// exit
					}
				});

				return builder_comment.create();



			default:
				return null;
		}

	}

}
