/*
 *
 *
 */

package plugin.com.maxmind.geoip;

import java.io.IOException;
import java.lang.ref.WeakReference;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;
import com.maxmind.geoip.*;

/**
 * @author Daniel Einspanjer
 * @since  April-16-2008
 */
public class MaxMindGeoIPLookupData extends BaseStepData implements StepDataInterface
{
	private static WeakReference<LookupService> globalLookupService = new WeakReference<LookupService>(null);
	protected LookupService lookupService;
	protected int ipAddressFieldIndex;
	protected RowMetaInterface outputRowMeta;
	protected int firstNewFieldIndex;

    protected MaxMindGeoIPLookupData()
	{
		super();
	}

	protected void initLocationService(String dbLocation) throws IOException
	{
		lookupService = MaxMindGeoIPLookupData.initGlobalLookupService(dbLocation);
	}

	/**
	 * Until someone complains that they want to be looking up 
	 * GeoIP data in different databases at the same time in the same VM, 
	 * let's just keep one copy of this thing in memory.
	 * @param dbLocation The path to the binary database
	 * @return a reference to the global instance of the lookup service
	 * @throws IOException
	 */
	private static synchronized LookupService initGlobalLookupService(String dbLocation) throws IOException
	{
		LookupService ls = globalLookupService.get();
		if (ls == null)
		{
			ls = new LookupService(dbLocation, LookupService.GEOIP_MEMORY_CACHE);
			globalLookupService = new WeakReference<LookupService>(ls);
		}
		return ls;
	}
}
