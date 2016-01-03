package crixec.app.imagefactory.utils;
import java.io.File;

public class PartitionUtils
{
	private static File[] RecoveryList = {
		new File("/dev/block/platform/omap/omap_hsmmc.0/by-name/recovery"),
		new File("/dev/block/platform/omap/omap_hsmmc.1/by-name/recovery"),
		new File("/dev/block/platform/sdhci-tegra.3/by-name/recovery"),
		new File("/dev/block/platform/sdhci-pxav3.2/by-name/RECOVERY"),
		new File("/dev/block/platform/msm_sdcc.1/by-name/FOTAKernel"),
		new File("/dev/block/platform/15570000.ufs/by-name/RECOVERY"),
		new File("/dev/block/platform/comip-mmc.1/by-name/recovery"),
		new File("/dev/block/platform/msm_sdcc.1/by-name/recovery"),
		new File("/dev/block/platform/mtk-msdc.0/by-name/recovery"),
		new File("/dev/block/platform/sdhci-tegra.3/by-name/SOS"),
		new File("/dev/block/platform/sdhci-tegra.3/by-name/USP"),
		new File("/dev/block/platform/dw_mmc.0/by-name/recovery"),
		new File("/dev/block/platform/dw_mmc.0/by-name/RECOVERY"),
		new File("/dev/block/platform/hi_mci.1/by-name/recovery"),
		new File("/dev/block/platform/hi_mci.0/by-name/recovery"),
		new File("/dev/block/platform/sdhci-tegra.3/by-name/UP"),
		new File("/dev/block/platform/sdhci-tegra.3/by-name/SS"),
		new File("/dev/block/platform/sdhci.1/by-name/RECOVERY"),
		new File("/dev/block/platform/sdhci.1/by-name/recovery"),
		new File("/dev/block/platform/dw_mmc/by-name/recovery"),
		new File("/dev/block/platform/dw_mmc/by-name/RECOVERY"),
		new File("/dev/block/bootdevice/by-name/recovery"),
		new File("/dev/block/recovery"),
		new File("/dev/block/nandg"),
		new File("/dev/block/acta"),
		new File("/dev/recovery"),
    };
    /**
     * Collection of known Kernel Partitions on some devices
     */
    private static File[] KernelList = {
		new File("/dev/block/platform/omap/omap_hsmmc.0/by-name/boot"),
		new File("/dev/block/platform/sprd-sdhci.3/by-name/KERNEL"),
		new File("/dev/block/platform/sdhci-tegra.3/by-name/LNX"),
		new File("/dev/block/platform/15570000.ufs/by-name/BOOT"),
		new File("/dev/block/platform/msm_sdcc.1/by-name/Kernel"),
		new File("/dev/block/platform/mtk-msdc.0/by-name/boot"),
		new File("/dev/block/platform/msm_sdcc.1/by-name/boot"),
		new File("/dev/block/platform/sdhci.1/by-name/KERNEL"),
		new File("/dev/block/platform/hi_mci.0/by-name/boot"),
		new File("/dev/block/platform/sdhci.1/by-name/boot"),
		new File("/dev/block/bootdevice/by-name/boot"),
		new File("/dev/block/nandc"),
		new File("/dev/boot"),
		new File("/dev/bootimg")
    };
	private static File mKernelPath = null;
	private static File mRecoveryPath = null;
	public static File getKernelPath(){
		if(mKernelPath != null){
			return mKernelPath;
		}else{
			for(File file: KernelList){
				if(file.exists()){
					mKernelPath = file;
				}
			}
		}

		if(mKernelPath == null){
			mKernelPath = new File("/");
		}
		return mKernelPath;
	}
	public static File getRecoveryPath(){
		if(mRecoveryPath != null){
			return mRecoveryPath;
		}else{
			for(File file: RecoveryList){
				if(file.exists()){
					mRecoveryPath = file;
				}
			}
		}
		if(mRecoveryPath == null){
			mRecoveryPath = new File("/");
		}
		return mRecoveryPath;
	}	
}


