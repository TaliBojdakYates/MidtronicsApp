# Midtronics
# Android Country Information App

This Android application provides a three-screen experience with my profile and a feature to browse a list of countries, and view detailed information about a selected country. The app is designed to demonstrate HTTP GET requests, navigation between screens, and fetching data using APIs.

## Project Overview
The project consists of three main screens:
1. **Profile Screen**: Personal profile with my name, picture, and a scrollable summary of my education and work experience.
2. **Country List Screen**: Display a list of countries. The list is sourced from [this XML file](https://github.com/vinaygaba/Ultimate-String-Array-List/blob/master/Countries.xml).
3. **Country Detail Screen**: When a country is selected, an HTTP GET request is made to the REST Countries API to retrieve detailed information about the country, including its name, capital, population, area, region, and sub-region.

## Future Improvements
- **Verification of Country Map URLs**: Ensure the accuracy and completeness of country map outlines.
- **Expandability**: The app's architecture allows for easy expansion to include additional fields from country data requests.
- **Performance Optimization**: Continue improving performance

## Dependencies
This project uses the following dependencies:
- Internet Access
- GSON for XML parsing
- OkHttp for HTTP requests.


