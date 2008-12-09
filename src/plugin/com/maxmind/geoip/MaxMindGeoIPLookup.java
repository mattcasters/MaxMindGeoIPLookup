
package plugin.com.maxmind.geoip;

import java.io.IOException;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.regionName;
import com.maxmind.geoip.timeZone;




/*
 * @author Daniel Einspanjer
 * @since  April-16-2008
 */

public class MaxMindGeoIPLookup extends BaseStep implements StepInterface
{
    private MaxMindGeoIPLookupData data;
	private MaxMindGeoIPLookupMeta meta;
	
	public MaxMindGeoIPLookup(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis)
	{
		super(s,stepDataInterface,c,t,dis);
		initializeIpTokens();
	}
	
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException
	{
	    meta = (MaxMindGeoIPLookupMeta)smi;
	    data = (MaxMindGeoIPLookupData)sdi;
	    
		Object[] r=getRow();    // get row, blocks when needed!
		if (r==null)  // no more input to be expected...
		{
			setOutputDone();
			return false;
		}
        
        if (first)
        {
            first = false;
            
            data.firstNewFieldIndex = getInputRowMeta().size();
            data.outputRowMeta = (RowMetaInterface)getInputRowMeta().clone();
            meta.getFields(data.outputRowMeta, getStepname(), null, null, this);            

			// search field
			data.ipAddressFieldIndex = data.outputRowMeta.indexOfValue(meta.getIpAddressFieldName());

			// only String type allowed
			if (!data.outputRowMeta.getValueMeta(data.ipAddressFieldIndex).isString())
			{
				throw new KettleValueException((Messages.getString("MaxMindGeoIPLookup.Log.IpAddressFieldNotValid",meta.getIpAddressFieldName()))); //$NON-NLS-1$ //$NON-NLS-2$
			}
        }
        
		// reserve room
		Object[] outputRow = RowDataUtil.allocateRowData(data.outputRowMeta.size());
				
		for (int i = 0; i < data.firstNewFieldIndex; i++)
		{
			outputRow[i] = r[i];
		}

		String ip = data.outputRowMeta.getString(r, data.ipAddressFieldIndex);
        
        Location location = data.lookupService.getLocation(getAddressFromIpV4(ip));
        
        if (location != null)
        {
        	outputRow[data.firstNewFieldIndex]   = location.countryCode;
        	outputRow[data.firstNewFieldIndex+1] = location.countryName;
        	outputRow[data.firstNewFieldIndex+2] = location.region;
        	outputRow[data.firstNewFieldIndex+3] = regionName.regionNameByCode(location.countryCode, location.region);
        	outputRow[data.firstNewFieldIndex+4] = location.city;
        	outputRow[data.firstNewFieldIndex+5] = (double)location.latitude;
        	outputRow[data.firstNewFieldIndex+6] = (double)location.longitude;
        	outputRow[data.firstNewFieldIndex+7] = timeZone.timeZoneByCountryAndRegion(location.countryCode, location.region);
        }
		putRow(data.outputRowMeta, outputRow);     // copy row to possible alternate rowset(s).

		if (checkFeedback(getLinesRead())) logBasic("Linenr "+getLinesRead());  // Some basic logging every 5000 rows.
			
		return true;
	}

    private static short[][][] ipTokens = new short[10][10][10];

    private static void initializeIpTokens() {
        for (short s = 0; s < 256; s++) {
            String ss = String.valueOf(s);
            switch (ss.length()) {
                case 1:
                    ipTokens[0][0][ss.charAt(0)-48] = s;
                    break;
                case 2:
                    ipTokens[0][ss.charAt(0)-48][ss.charAt(1)-48] = s;
                    break;
                case 3:
                    ipTokens[ss.charAt(0)-48][ss.charAt(1)-48][ss.charAt(2)-48] = s;
            }
        }
    }

	/**
	 * This is a heavily optimized method for extracting the long representation of the IP
	 * address without creating several Regex or String objects for each call.
	 * @param ip
	 * @return
	 */
	private long getAddressFromIpV4(String ip)
    {
        long retval = 0;
	    if (ip == null) return 0;
	    
        short parsedToken;
        int start = 0;
        int next = 0;

        for (int i = 0; i < 4; i++) {
            next = ip.indexOf('.', start);
            if (next == -1) {
                next = ip.length() - start;
            }
            try {
                switch (next - start) {
                    case 1:
                        parsedToken = ipTokens[0][0][ip.charAt(start)-48];
                        break;
                    case 2:
                        parsedToken = ipTokens[0][ip.charAt(start)-48][ip.charAt(start + 1)-48];
                        break;
                    case 3:
                        parsedToken = ipTokens[ip.charAt(start)-48][ip.charAt(start + 1)-48][ip.charAt(start + 2)-48];
                        break;
                    default:
                        return 0;
                }
            } catch (IndexOutOfBoundsException e) {
                return 0;
            }

	        retval += parsedToken << ((3-i)*8);
	    }
	    
	    return retval;
    }

    public boolean init(StepMetaInterface smi, StepDataInterface sdi)
	{
	    meta = (MaxMindGeoIPLookupMeta)smi;
	    data = (MaxMindGeoIPLookupData)sdi;

	    if (super.init(smi, sdi))
	    {
	    	try
	    	{
	    		data.initLocationService(meta.getDbLocation());
			}
	    	catch (IOException e)
			{
				e.printStackTrace();
			}
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	}

	public void dispose(StepMetaInterface smi, StepDataInterface sdi)
	{
	    meta = (MaxMindGeoIPLookupMeta)smi;
	    data = (MaxMindGeoIPLookupData)sdi;

	    super.dispose(smi, sdi);
	}
	
	//
	// Run is were the action happens!
	public void run()
	{
	    BaseStep.runStepThread(this, meta, data);
	}
}
