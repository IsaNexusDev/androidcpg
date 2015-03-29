package com.isanexusdev.androidcpg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;


public class Settings extends PreferenceActivity
implements SharedPreferences.OnSharedPreferenceChangeListener{
	private static final String TAG = Settings.class.getName();

	private AlertDialog creditsDialog = null;
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		getPreferenceManager().setSharedPreferencesName("settings");
		addPreferencesFromResource(R.xml.settings);

		Preference credits = (Preference)findPreference("credits");
        credits.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (creditsDialog != null){
                    creditsDialog.dismiss();
                    creditsDialog = null;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);

                String sOk = getString(android.R.string.ok);
                builder.setTitle(R.string.credits_title);
                builder.setIcon(0);
                builder.setPositiveButton(sOk, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (creditsDialog != null){
                            creditsDialog.dismiss();
                            creditsDialog = null;
                        }
                    }
                });

                builder.setNegativeButton(null,null);
                HashMap<String, String> languages = new HashMap<String, String>();
                languages.put(getLocaleName("en","English"), "IsaNexusDev");
                languages.put(getLocaleName("el","Greek"), "dimangelid");
                languages.put(getLocaleName("es","Spanish"), "IsaNexusDev");
                

                String creditsText = getString(R.string.credits_dialog_title)+ "\n";
                List<String> sortedKeys=new ArrayList(languages.keySet());
                Collections.sort(sortedKeys);
                for (String key: sortedKeys){
                    creditsText = creditsText + "\n"+key+" : "+languages.get(key);
                }
                creditsDialog = builder.create();


                creditsDialog.setMessage(creditsText);
                creditsDialog.setCancelable(false);
                creditsDialog.setCanceledOnTouchOutside(false);

                creditsDialog.show();

                return false;
            }

            private String getLocaleName(String locale, String defName){
                Locale[] locales = Locale.getAvailableLocales();
                int index = locale.indexOf("_");
                if (index > 0){
                    String langLocale = locale.substring(0, index);
                    String simpleMatch = "";
                    for (Locale tmpLocale : locales){
                        String tmpLocaleLanguageString = tmpLocale.getLanguage().toLowerCase();
                        String tmpLocaleCountryString = tmpLocale.getCountry().toLowerCase();
                        if ((tmpLocaleLanguageString+"_"+tmpLocaleCountryString).equals(locale)){
                            return capitalize(tmpLocale.getDisplayName());//.getDisplayLanguage();
                        } else if (simpleMatch.length() == 0 && tmpLocaleLanguageString.equals(langLocale)){
                            simpleMatch = tmpLocale.getDisplayName();//.getDisplayLanguage();
                        }						
                    }

                    if (simpleMatch.length() > 0){
                        return capitalize(simpleMatch);
                    }
                } else {
                    for (Locale tmpLocale : locales){
                        String tmpLocaleLanguageString = tmpLocale.getLanguage().toLowerCase();
                        if (tmpLocaleLanguageString.equals(locale)){
                            return capitalize(tmpLocale.getDisplayName());//.getDisplayLanguage();
                        }
                    }
                }
                return defName;
            }

            private String capitalize(String string){
                String value;
                try{
                    value = string.substring(0,1).toUpperCase()+string.substring(1);
                } catch (Exception e) {
                    value = string;
                }

                return value;
            }
        });
        
		getWindow().getDecorView().setBackgroundColor(0xff909090);
	}


	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.i(TAG, key);
	}
}