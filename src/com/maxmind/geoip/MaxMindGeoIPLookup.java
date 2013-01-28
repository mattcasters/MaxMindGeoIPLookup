package com.maxmind.geoip;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;


/*
 * @author Daniel Einspanjer and Doug Moran
 * @since  April-16-2008
 */

public class MaxMindGeoIPLookup extends BaseStep implements StepInterface
{
  private static Class<?> PKG = MaxMindGeoIPLookup.class;
  
  private MaxMindGeoIPLookupData data;
  private MaxMindGeoIPLookupMeta meta;
	private MaxMindDatabase maxMindDatabase;

	public MaxMindGeoIPLookup(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis)
	{
    super(s, stepDataInterface, c, t, dis);
  }

  public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException
  {
    meta = (MaxMindGeoIPLookupMeta) smi;
    data = (MaxMindGeoIPLookupData) sdi;

    Object[] r = getRow(); // get row, blocks when needed!
    if (r == null) // no more input to be expected...
    {
      setOutputDone();
      return false;
    }

    if (first)
    {
      first = false;
      
      data.firstNewFieldIndex = getInputRowMeta().size();
      data.outputRowMeta = getInputRowMeta().clone();
      
      data.ipAddressFieldIndex = data.outputRowMeta.indexOfValue(meta.getIpAddressFieldName());

      meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

      // only String type allowed
      if (!data.outputRowMeta.getValueMeta(data.ipAddressFieldIndex).isString())
      {
        throw new KettleValueException((BaseMessages.getString(PKG, "MaxMindGeoIPLookup.Log.IpAddressFieldNotValid",meta.getIpAddressFieldName()))); //$NON-NLS-1$ 
      }
    }

    // reserve room
    Object[] outputRow = RowDataUtil.allocateRowData(data.outputRowMeta.size());

    for (int i = 0; i < data.firstNewFieldIndex; i++)
    {
      outputRow[i] = r[i];
    }

    maxMindDatabase.getRowData(outputRow, data.firstNewFieldIndex, data.outputRowMeta.getString(r, data.ipAddressFieldIndex) );
    
    putRow(data.outputRowMeta, outputRow); // copy row to possible alternate rowset(s).

    if (checkFeedback(getLinesRead())) logBasic("Linenr " + getLinesRead()); // Some basic logging every 5000 rows.

    return true;
  }


  public boolean init(StepMetaInterface smi, StepDataInterface sdi) 
  {
    meta = (MaxMindGeoIPLookupMeta) smi;
    data = (MaxMindGeoIPLookupData) sdi;

    if (super.init(smi, sdi))
    {
      MaxMindHelper helper = new MaxMindHelper(this, meta);
      try {
        helper.setupMaxMindDatabase();
        maxMindDatabase = helper.getMaxMindDatabase();
      } catch(Exception e) {
        logError("Error initializing max mind database file location '"+helper.getFilenameLocation()+"'", e);
        return false;
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
    meta = (MaxMindGeoIPLookupMeta) smi;
    data = (MaxMindGeoIPLookupData) sdi;

    super.dispose(smi, sdi);
  }
}
