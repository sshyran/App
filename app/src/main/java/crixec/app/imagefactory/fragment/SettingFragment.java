package crixec.app.imagefactory.fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import crixec.app.imagefactory.R;
import android.preference.Preference;
import crixec.app.imagefactory.ui.Dialog;
import android.content.DialogInterface;
import crixec.app.imagefactory.utils.XmlDataUtils;
import crixec.app.imagefactory.utils.ShellUtils;
import crixec.app.imagefactory.core.ImageFactory;

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
		findPreference(getString(R.string.keyname_clean_datas)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){

				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					// TODO: Implement this method
					Dialog.build(getActivity())
					.setTitle(getString(R.string.setting_clean_datas))
					.setMessage(getString(R.string.setting_confirm_clean_datas))
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								// TODO: Implement this method
								XmlDataUtils.remove(getString(R.string.keyname_workpath));
								ShellUtils.execSH(String.format("toolbox rm -r %s", ImageFactory.getDataDirectory()));
								ShellUtils.execSH(String.format("toolbox rm -r %s/*", ImageFactory.getStorageDirectory().toString()));
								ImageFactory.forceStop();
							}
						})
					.setNegativeButton(android.R.string.no, null)
					.show();
					return false;
				}
		});
	}
}
