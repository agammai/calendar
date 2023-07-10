# GIC calendar
A simple calendar calculator for GIC to decide whether a particular
date is a holiday and to calculate the next business day. 

This calculator allows you to select a country 
<li>Identify if a date selected is a working day, weekend or a public holiday. 
<li>Allow you to find out the next business day for the selected country.

## Assumption

1. If the date is marked as public holiday, it will take the highest precedence even if it is a weekend
2. Application works only for the predefined holiday list being provided for country
3. Application restart is required to populate the new holiday list being loaded in resources folder
4. Calendar will work for any LocalDate (i.e.., Any year)
5. What shpuld happen when holiday list for the same country is being provided across multiple text files?

## How to run the application 

#### Java version - 17
Execute the *Main.class* (Starting point of the application) after running build.gradle

### How to load the holiday list for a new country

1. Create holiday list for a country with the following
    1. txt file extension  
    2. Country name as file name
2. Place it under the resources folder src/main/resources/holidays
3. Restart the application

