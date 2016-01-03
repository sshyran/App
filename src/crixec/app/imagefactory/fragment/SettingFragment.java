package crixec.app.imagefactory.fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import crixec.app.imagefactory.R;

public class SettingFragment extends PreferenceFragment
{
	public SettingFragment()
	{}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
	}
}
