package com.maxmind.geoip;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import org.pentaho.di.core.Const;

import com.maxmind.geoip.DatabaseInfo;
import com.maxmind.geoip.LookupService;
/*
 * The Overall management class for all the MaxMind databases
 */
public class MaxMindGeoIP {

  private static final HashMap<String, WeakReference<LookupService>> globalLookupServices = new HashMap<String, WeakReference<LookupService>>();
  
  /*
   * Should probably change this to enum at some point
   */
  private static final String DB_CITY = "CITY"; //$NON-NLS-1$
  private static final String DB_COUNTRY = "COUNTRY"; //$NON-NLS-1$
  private static final String DB_ISP = "ISP"; //$NON-NLS-1$
  private static final String DB_ORG = "ORG"; //$NON-NLS-1$

  private static final String[] dbTypes = { DB_CITY, DB_COUNTRY, DB_ISP, DB_ORG };
  
  //TODO: Use these friendly names for the selector in the dialog and make them I18n-able
  private static final String[] dbNames = { "City", "Country", "Internet Service Provider", "Organization" };

  public static final String[] getDatabaseTypes() {
    return (dbTypes);
  }

  public static final String[] getDatabaseNames() {
    return (dbNames);
  }

  public static final MaxMindDatabase getDatabase(String dbType) {
    if (DB_CITY.equals(dbType)) {
      return (new MaxMindCityData());
    } 
    else if (DB_COUNTRY.equals(dbType)) {
      return (new MaxMindCountryData());
    } 
    else if (DB_ORG.equals(dbType)) {
      return (new MaxMindOrgData());
    } 
    else if (DB_ISP.equals(dbType)) {
      return (new MaxMindIspData());
    } 
    else {
      return (null);
    }
  }
  
  /*
   * Return the fields for the specific MaxMind databases 
   */
  public static final String[] getDbFieldanmes(String dbType) {
    MaxMindDatabase mmDb = MaxMindGeoIP.getDatabase( dbType );
    return( ( mmDb == null ) ? new String[]{} : mmDb.getFieldNames() );
  }

  /**
   * Let's just keep one copy of each database type in memory (avoid, multiple caches).
   * @param dbLocation The path to the binary database
   * @return a reference to the global instance of the lookup service
   * @throws IOException
   */
  public static final synchronized LookupService initLookupService(String dbLocation) throws IOException {
    LookupService ls = null;
    WeakReference<LookupService> wrLs = globalLookupServices.get(dbLocation);
    if ((wrLs == null) || ((ls = wrLs.get()) == null)) {
      ls = new LookupService(dbLocation, LookupService.GEOIP_MEMORY_CACHE);  // Cache here for runtime
      globalLookupServices.put(dbLocation, new WeakReference<LookupService>(ls));
    }
    return ls;
  }

  /*
   * Returns the database info string from the database files.  The API from the MaxMind
   * classes is broken.  Returns the wrong database type for some DBs, on error it assumes 
   * Country DB, can't tell if the file is not a real database file or not.  For now we will
   * just display the string and let the user figure out what is going on.
   */
  public static String getDbInfo(String fileName) {
    if (Const.isEmpty(fileName)) {
      return (""); //$NON-NLS-1$
    }

    String dbInfoStr = null;
    try {
      LookupService ls = new LookupService(fileName, LookupService.GEOIP_STANDARD);  // Don't cache here, it slows down design time
      DatabaseInfo dbInfo = ls.getDatabaseInfo();
      dbInfoStr = (dbInfo == null) ? null : dbInfo.toString();
      ls.close();
    } catch (Exception e) {
      dbInfoStr = "Error opening DB file: '" + fileName + "'";
    }
    return (Const.isEmpty(dbInfoStr) ? "No DB info header, the file still may be valid." : dbInfoStr);
  }


}
