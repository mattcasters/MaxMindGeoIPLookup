/*
 *
 *
 */

package com.maxmind.geoip;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * @author Daniel Einspanjer and Doug Moran
 * @since  April-16-2008
 */
public class MaxMindGeoIPLookupData extends BaseStepData implements StepDataInterface
{
	protected int ipAddressFieldIndex;
	protected RowMetaInterface outputRowMeta;
	protected int firstNewFieldIndex;

	protected MaxMindGeoIPLookupData()
	{
		super();
	}
}
