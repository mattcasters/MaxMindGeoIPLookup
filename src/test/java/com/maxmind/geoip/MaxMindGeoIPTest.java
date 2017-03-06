package com.maxmind.geoip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MaxMindGeoIPTest {

  @Test
  public void testGetDatabaseTypes() {
    assertEquals( 5, MaxMindGeoIP.getDatabaseTypes().length );
    List<String> dbTypes = Arrays.asList( MaxMindGeoIP.getDatabaseTypes() );
    assertTrue( dbTypes.contains( "COUNTRY" ) );
    assertTrue( dbTypes.contains( "CITY" ) );
    assertTrue( dbTypes.contains( "ISP" ) );
    assertTrue( dbTypes.contains( "ORG" ) );
    assertTrue( dbTypes.contains( "DOMAIN" ) );
  }

  @Test
  public void testGetDatabaseNames() {
    assertEquals( 5, MaxMindGeoIP.getDatabaseNames().length );
    List<String> dbNames = Arrays.asList( MaxMindGeoIP.getDatabaseNames() );
    assertTrue( dbNames.contains( "Country" ) );
    assertTrue( dbNames.contains( "City" ) );
    assertTrue( dbNames.contains( "Internet Service Provider" ) );
    assertTrue( dbNames.contains( "Organization" ) );
    assertTrue( dbNames.contains( "Domain" ) );
  }

  @Test
  public void testGetDatabase() {
    assertTrue( MaxMindGeoIP.getDatabase( "COUNTRY" ) instanceof MaxMindCountryData );
    assertTrue( MaxMindGeoIP.getDatabase( "CITY" ) instanceof MaxMindCityData );
    assertTrue( MaxMindGeoIP.getDatabase( "ISP" ) instanceof MaxMindIspData );
    assertTrue( MaxMindGeoIP.getDatabase( "ORG" ) instanceof MaxMindOrgData );
    assertTrue( MaxMindGeoIP.getDatabase( "DOMAIN" ) instanceof MaxMindDomainData );
  }
}
