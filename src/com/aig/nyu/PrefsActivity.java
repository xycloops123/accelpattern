package com.aig.nyu;

import java.util.regex.Pattern;

import com.aig.nyu.R;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity
{

	SharedPreferences prefs;
	Editor myEditor;
	private EditTextPreference ipTextBox, portNumTextBox, textPortNumTextBox;
	private static final String IP_REG_EXPRESSION = "^((1\\d{2}|2[0-4]\\d|25[0-5]|\\d?\\d)\\.){3}(?:1\\d{2}|2[0-4]\\d|25[0-5]|\\d?\\d)$";
	
	private String IP_FROM_PREFS = "ipAddressPref";
	private String PORT_NUM_PREFS = "portNumberPref";
	private String TEXT_PORT_PREFS = "portTexturePref";
	@Override
	/**
	 * The onCreate method handles thing when starting this activity, 
	 * mainly display the activity_settings.xml.
	 */
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.activity_settings);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		ipTextBox = (EditTextPreference) getPreferenceScreen().findPreference(IP_FROM_PREFS);
		portNumTextBox = (EditTextPreference) getPreferenceScreen().findPreference(PORT_NUM_PREFS);
		textPortNumTextBox = (EditTextPreference) getPreferenceScreen().findPreference(TEXT_PORT_PREFS);
		
		
}
}