package com.maxmind.geoip;

import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.variables.VariableSpace;

public class MaxMindHelper {

  private MaxMindDatabase maxMindDatabase = null;
  private VariableSpace space;
  private MaxMindGeoIPLookupMeta meta;
  private String filenameLocation;

  public MaxMindHelper(VariableSpace space, MaxMindGeoIPLookupMeta meta) {
    this.space = space;
    this.meta = meta;
  }

  public void setupMaxMindDatabase() throws KettleStepException {
    maxMindDatabase = null;
    if (meta.getDbType() != null) {
      maxMindDatabase = MaxMindGeoIP.getDatabase(meta.getDbType());
      if (maxMindDatabase != null) {
        filenameLocation = space.environmentSubstitute(meta.getDbLocation());
        try {
          maxMindDatabase.setDbLocation(filenameLocation);
          maxMindDatabase.setSelectedFields(meta.getFieldLookupType());
          maxMindDatabase.setSelectedFields(meta.getFieldLookupType(), meta.getFieldName(), meta.getFieldIfNull());
        } catch (Exception e) { // Invalid Location
          throw new KettleStepException("Unable to set up MaxMind database '" + filenameLocation + "'", e);
        }
      }
    }
  }

  public String getFilenameLocation() {
    return filenameLocation;
  }

  public MaxMindDatabase getMaxMindDatabase() {
    return maxMindDatabase;
  }
}
