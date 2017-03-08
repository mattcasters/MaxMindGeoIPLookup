package com.maxmind.geoip;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaPluginType;

import com.maxmind.geoip.MaxMindCityData.CityFields;
import com.maxmind.geoip.MaxMindCountryData.CountryFields;
import com.maxmind.geoip.MaxMindDomainData.DomainFields;
import com.maxmind.geoip.MaxMindIspData.IspFields;
import com.maxmind.geoip.MaxMindOrgData.OrgFields;

public class MaxMindDatabaseTest { 

  @BeforeClass
  public static void setUpBeforeClass() throws KettlePluginException {
	PluginRegistry.addPluginType( ValueMetaPluginType.getInstance() );
	PluginRegistry.init();
  }

  @Test
  public void testFields() {

    // CountryFields Enum
    assertEquals( 2, CountryFields.values().length );
    assertEquals( ValueMetaInterface.TYPE_STRING, CountryFields.country_code.getValueMetadata().getType() );
    assertEquals( 2, CountryFields.country_code.getValueMetadata().getLength() );
    assertEquals( -1, CountryFields.country_code.getValueMetadata().getPrecision() );
    assertEquals( ValueMetaInterface.TYPE_STRING, CountryFields.country_name.getValueMetadata().getType() );
    assertEquals( 50, CountryFields.country_name.getValueMetadata().getLength() );
    assertEquals( -1, CountryFields.country_name.getValueMetadata().getPrecision() );

    // CityFields Enum
    assertEquals( 8, CityFields.values().length );
    assertEquals( ValueMetaInterface.TYPE_STRING, CityFields.city_name.getValueMetadata().getType() );
    assertEquals( 255, CityFields.city_name.getValueMetadata().getLength() );
    assertEquals( -1, CityFields.city_name.getValueMetadata().getPrecision() );
    assertEquals( ValueMetaInterface.TYPE_STRING, CityFields.country_code.getValueMetadata().getType() );
    assertEquals( 2, CityFields.country_code.getValueMetadata().getLength() );
    assertEquals( -1, CityFields.country_code.getValueMetadata().getPrecision() );
    assertEquals( ValueMetaInterface.TYPE_STRING, CityFields.country_name.getValueMetadata().getType() );
    assertEquals( 50, CityFields.country_name.getValueMetadata().getLength() );
    assertEquals( -1, CityFields.country_name.getValueMetadata().getPrecision() );
    assertEquals( ValueMetaInterface.TYPE_NUMBER, CityFields.latitude.getValueMetadata().getType() );
    assertEquals( 10, CityFields.latitude.getValueMetadata().getLength() );
    assertEquals( 4, CityFields.latitude.getValueMetadata().getPrecision() );
    assertEquals( ValueMetaInterface.TYPE_NUMBER, CityFields.longitude.getValueMetadata().getType() );
    assertEquals( 10, CityFields.latitude.getValueMetadata().getLength() );
    assertEquals( 4, CityFields.latitude.getValueMetadata().getPrecision() );
    assertEquals( ValueMetaInterface.TYPE_STRING, CityFields.region_code.getValueMetadata().getType() );
    assertEquals( 2, CityFields.region_code.getValueMetadata().getLength() );
    assertEquals( -1, CityFields.region_code.getValueMetadata().getPrecision() );
    assertEquals( ValueMetaInterface.TYPE_STRING, CityFields.region_name.getValueMetadata().getType() );
    assertEquals( 50, CityFields.region_name.getValueMetadata().getLength() );
    assertEquals( -1, CityFields.region_name.getValueMetadata().getPrecision() );
    assertEquals( ValueMetaInterface.TYPE_STRING, CityFields.timezone.getValueMetadata().getType() );
    assertEquals( 255, CityFields.timezone.getValueMetadata().getLength() );
    assertEquals( -1, CityFields.timezone.getValueMetadata().getPrecision() );

    // DomainFields Enum
    assertEquals( 1, DomainFields.values().length );
    assertEquals( ValueMetaInterface.TYPE_STRING, DomainFields.domain_name.getValueMetadata().getType() );
    assertEquals( 255, DomainFields.domain_name.getValueMetadata().getLength() );
    assertEquals( -1, DomainFields.domain_name.getValueMetadata().getPrecision() );

    // IspFields Enum
    assertEquals( 1, IspFields.values().length );
    assertEquals( ValueMetaInterface.TYPE_STRING, IspFields.isp_name.getValueMetadata().getType() );
    assertEquals( 255, IspFields.isp_name.getValueMetadata().getLength() );
    assertEquals( -1, IspFields.isp_name.getValueMetadata().getPrecision() );

    // OrgFields Enum
    assertEquals( 1, OrgFields.values().length );
    assertEquals( ValueMetaInterface.TYPE_STRING, OrgFields.organization_name.getValueMetadata().getType() );
    assertEquals( 255, OrgFields.organization_name.getValueMetadata().getLength() );
    assertEquals( -1, OrgFields.organization_name.getValueMetadata().getPrecision() );
  }

  @Test
  public void testCountryData() {
    MaxMindCountryData data = spy( new MaxMindCountryData() );
    LookupService service = mock( LookupService.class );
    Country country = mock( Country.class );
    when( country.getName() ).thenReturn( "TestCountry" );
    when( country.getCode() ).thenReturn( "TC" );
    when( data.getLookupService() ).thenReturn( service );
    when( service.getCountry( anyLong() ) ).thenReturn( country );

    assertEquals( CountryFields.values().length, data.getAllFields().length );

    data.setSelectedFields( null );
    assertEquals( 0, data.getSelectedFields().length );

    data.setSelectedFields( new String[] { CountryFields.country_code.name(), CountryFields.country_name.name() } );
    assertEquals( 2, data.getSelectedFields().length );
    assertEquals( CountryFields.country_code, data.getSelectedFields()[0] );
    assertEquals( CountryFields.country_name, data.getSelectedFields()[1] );

    Object[] rowData = new Object[2];
    data.getRowData( rowData, 0, "1.1.1.1" );
    assertEquals( "TC", rowData[0] );
    assertEquals( "TestCountry", rowData[1] );
    
    rowData = new Object[10];
    data.getRowData( rowData, 5, "1.1.1.1" );
    assertEquals( 10, rowData.length );
    assertEquals( "TC", rowData[5] );
    assertEquals( "TestCountry", rowData[6] );
  }

  @Test
  public void testCityData() {
    final double DELTA = (double) 0.00001;

    MaxMindCityData data = spy( new MaxMindCityData() );
    LookupService service = mock( LookupService.class );
    Location location = mock( Location.class );
    location.countryCode = "US";
    location.countryName = "United States";
    location.region = "NY";
    location.city = "TestCity";
    location.latitude = (float) 128.001;
    location.longitude = (float) 45.123;
    when( data.getLookupService() ).thenReturn( service );
    when( service.getLocation( anyLong() ) ).thenReturn( location );

    assertEquals( CityFields.values().length, data.getAllFields().length );

    data.setSelectedFields( null );
    assertEquals( 0, data.getSelectedFields().length );

    data.setSelectedFields( new String[] {
      CityFields.country_code.name(), CityFields.country_name.name(),
      CityFields.region_code.name(), CityFields.region_name.name(),
      CityFields.city_name.name(), CityFields.timezone.name(),
      CityFields.latitude.name(), CityFields.longitude.name() } );
    assertEquals( 8, data.getSelectedFields().length );
    assertEquals( CityFields.country_code, data.getSelectedFields()[0] );
    assertEquals( CityFields.country_name, data.getSelectedFields()[1] );
    assertEquals( CityFields.region_code, data.getSelectedFields()[2] );
    assertEquals( CityFields.region_name, data.getSelectedFields()[3] );
    assertEquals( CityFields.city_name, data.getSelectedFields()[4] );
    assertEquals( CityFields.timezone, data.getSelectedFields()[5] );
    assertEquals( CityFields.latitude, data.getSelectedFields()[6] );
    assertEquals( CityFields.longitude, data.getSelectedFields()[7] );

    Object[] rowData = new Object[8];
    data.getRowData( rowData, 0, "1.1.1.1" );
    assertEquals( "US", rowData[0] );
    assertEquals( "United States", rowData[1] );
    assertEquals( "NY", rowData[2] );
    assertEquals( "New York", rowData[3] );
    assertEquals( "TestCity", rowData[4] );
    assertEquals( "America/New_York", rowData[5] );
    assertEquals( DELTA, (double) 128.001, (double) rowData[6] );
    assertEquals( DELTA, (double) 45.123, (double) rowData[7] );

    rowData = new Object[13];
    data.getRowData( rowData, 5, "1.1.1.1" );
    assertEquals( "US", rowData[5] );
    assertEquals( "United States", rowData[6] );
    assertEquals( "NY", rowData[7] );
    assertEquals( "New York", rowData[8] );
    assertEquals( "TestCity", rowData[9] );
    assertEquals( "America/New_York", rowData[10] );
    assertEquals( DELTA, (double) 128.001, (double) rowData[11] );
    assertEquals( DELTA, (double) 45.123, (double) rowData[12] );
  }

  @Test
  public void testDomainData() {
    MaxMindDomainData data = spy( new MaxMindDomainData() );
    LookupService service = mock( LookupService.class );
    when( data.getLookupService() ).thenReturn( service );
    when( service.getOrg( anyLong() ) ).thenReturn( "TestDomain.com" );

    assertEquals( DomainFields.values().length, data.getAllFields().length );
    data.setSelectedFields( new String[] { DomainFields.domain_name.name() } );
    assertEquals( 1, data.getSelectedFields().length );
    assertEquals( DomainFields.domain_name, data.getSelectedFields()[0] );

    Object[] rowData = new Object[1];
    data.getRowData( rowData,  0, "1.1.1.1" );
    assertEquals( "TestDomain.com", rowData[0] );

    rowData = new Object[4];
    data.getRowData( rowData, 2, "1.1.1.1" );
    assertEquals( "TestDomain.com", rowData[2] );
  }

  @Test
  public void testIspData() {
    MaxMindIspData data = spy( new MaxMindIspData() );
    LookupService service = mock( LookupService.class );
    when( data.getLookupService() ).thenReturn( service );
    when( service.getOrg( anyLong() ) ).thenReturn( "FooBar Communications" );

    assertEquals( IspFields.values().length, data.getAllFields().length );
    data.setSelectedFields( new String[] { IspFields.isp_name.name() } );
    assertEquals( 1, data.getSelectedFields().length );
    assertEquals( IspFields.isp_name, data.getSelectedFields()[0] );

    Object[] rowData = new Object[1];
    data.getRowData( rowData, 0, "1.1.1.1" );
    assertEquals( "FooBar Communications", rowData[0] );

    rowData = new Object[4];
    data.getRowData( rowData, 2, "1.1.1.1" );
    assertEquals( "FooBar Communications", rowData[2] );
  }

  @Test
  public void testOrgData() {
    MaxMindOrgData data = spy( new MaxMindOrgData() );
    LookupService service = mock( LookupService.class );
    when( data.getLookupService() ).thenReturn( service );
    when( service.getOrg( anyLong() ) ).thenReturn( "ACME Corporation" );

    assertEquals( OrgFields.values().length, data.getAllFields().length );
    data.setSelectedFields( new String[] { OrgFields.organization_name.name() } );
    assertEquals( 1, data.getSelectedFields().length );
    assertEquals( OrgFields.organization_name, data.getSelectedFields()[0] );

    Object[] rowData = new Object[1];
    data.getRowData( rowData, 0, "1.1.1.1" );
    assertEquals( "ACME Corporation", rowData[0] );

    rowData = new Object[4];
    data.getRowData( rowData, 2, "1.1.1.1" );
    assertEquals( "ACME Corporation", rowData[2] );
  }
}
