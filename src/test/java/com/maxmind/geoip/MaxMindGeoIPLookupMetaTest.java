package com.maxmind.geoip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.loadsave.LoadSaveTester;
import org.pentaho.di.trans.steps.loadsave.validator.ArrayLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.FieldLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.StringLoadSaveValidator;

public class MaxMindGeoIPLookupMetaTest {

  @BeforeClass
  public static void setUpBeforeClass() throws KettleException {
    KettleEnvironment.init( false );
  }

  @Test
  public void testRoundTrip() throws KettleException {
    List<String> attributes =
      Arrays.asList( "IpAddressFieldName", "DbLocation", "DbType", "FieldName", "FieldLookupType", "FieldIfNull" );

    HashMap<String, FieldLoadSaveValidator<?>> fieldValidators = new HashMap<String,FieldLoadSaveValidator<?>>();
    int records = new Random().nextInt( 9 ) + 1;
    fieldValidators.put( "FieldName", new ArrayLoadSaveValidator<String>( new StringLoadSaveValidator(), records ) );
    fieldValidators.put( "FieldLookupType", new ArrayLoadSaveValidator<String>( new StringLoadSaveValidator(), records ) );
    fieldValidators.put( "FieldIfNull", new ArrayLoadSaveValidator<String>( new StringLoadSaveValidator(), records ) );
    LoadSaveTester loadSaveTester =
      new LoadSaveTester( MaxMindGeoIPLookupMeta.class, attributes,
        new HashMap<String, String>(), new HashMap<String, String>(), fieldValidators,
        new HashMap<String,FieldLoadSaveValidator<?>>() );

    loadSaveTester.testRepoRoundTrip();
    loadSaveTester.testXmlRoundTrip();
  }

  @Test
  public void testDefault() {
	  MaxMindGeoIPLookupMeta meta = new MaxMindGeoIPLookupMeta();
	  meta.setDefault();
	  assertEquals( "", meta.getIpAddressFieldName() );
	  assertEquals( "", meta.getDbLocation() );
	  assertEquals( "", meta.getDbType() );
	  assertEquals( 0, meta.getFieldName().length );
	  assertEquals( 0, meta.getFieldLookupType().length );
	  assertEquals( 0, meta.getFieldIfNull().length );
  }

  @Test
  public void testGetStepData() {
    MaxMindGeoIPLookupMeta meta = new MaxMindGeoIPLookupMeta();
    assertTrue( meta.getStepData() instanceof MaxMindGeoIPLookupData );
  }
}
