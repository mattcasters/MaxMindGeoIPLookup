Facilitates GeoIP lookups using MaxMind's GeoIP data files and Java APIs.

This plugin uses the MaxMind binary data files to lookup the information based on an IP address.  Maxmind has
several data sets available for free and for pay.  For a comprehensive list of what's available, check out 
the MaxMind web site: http://www.maxmind.com/app/geolocation

You will need to download a suitable datafile for the data you are interested in

Currently supported databases and fields
========================================

GeoLite Country (free) and GeoIP Country (pay):
    country_code
    country_name

    TODO: Add Continent data

GeoLite City (free) and GeoIP City (pay):
    country_code
    country_name
    region_code (state/provence)
    region_name
    city_name
    latitude
    longitude
    timezone

    TODO: Add postcode, Metro Code, Area Code, Continent
    
GeoIP Organization (pay)
    organization_name

GeoIP ISP (pay)
    isp_name

The following databases are not directly supported since I don't have access to them
but they may work by selecting a different database type that has the same fields and 
only selecting valid fields:

GeoIP Domain (pay) - Verified working
Hack: Select "ORG" as "MaxMind DB Type" in UI
      organization_name is actually the domain

GeoIP Region (pay) and GeoIP Metro (pay) - Should work
Hack: Select "CITY" as "MaxMind DB Type" in UI
      Only use country_code, country_name, region_code, region_name
      
GeoIP Netspeed (pay) and GeoIP Accuracy (pay)
Hack: None, sorry


TODOs:
I18n strings, and "MaxMind DB Type" drop down
Add support for IPv6 - now available in GeoIP v1.2.4
Add Support for Netspeed Database - Hack Should Work
Add Support for DomainName Database - Hack works
Add Support for Region Database
Add Support for Metro Database
Add Support for Accuracy Radius DB
