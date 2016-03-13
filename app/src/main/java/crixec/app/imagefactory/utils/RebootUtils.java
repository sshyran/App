package crixec.app.imagefactory.utils;
import crixec.app.imagefactory.ui.Toast;

public class RebootUtils
{
	public static void reboot(){
		Toast.makeLongText(ShellUtils.exec("reboot", false));
	}
	public static void softReboot(){
		Toast.makeLongText(ShellUtils.exec("killall system_server", false));
	}
	public static void recovery(){
		Toast.makeLongText(ShellUtils.exec("reboot recovery", false));
	}
	public static void bootloader(){
		Toast.makeLongText(ShellUtils.exec("reboot bootloader", false));
	}
}
