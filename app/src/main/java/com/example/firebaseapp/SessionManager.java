package com.example.firebaseapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

	private static final String PREF_NAME = "UserSession";
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
	private static final String KEY_USER_EMAIL = "userEmail";

	SharedPreferences pref;
	SharedPreferences.Editor editor;
	Context context;

	// Constructor
	public SessionManager(Context context)
	{
		this.context = context;
		pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		editor = pref.edit();
	}

	// Create login session
	public void createLoginSession(String email) {
		editor.putBoolean(KEY_IS_LOGGED_IN, true);
		editor.putString(KEY_USER_EMAIL, email);
		editor.commit();
	}

	// Check login status
	public boolean isLoggedIn() {
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}

	// Get stored session data (email)
	public String getUserEmail() {
		return pref.getString(KEY_USER_EMAIL, null);
	}

	// Logout user
	public void logoutUser() {
		editor.clear();
		editor.commit();
	}
}
