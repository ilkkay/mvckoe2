package translateit2.util;

import java.util.Locale;
import java.util.function.Predicate;

/**
 * Utility class for internationalization. This class provides a 
 * central location to do specialized formatting in both 
 * a default and a locale specific manner.
 *
 * Possible filename notations: 
 * Application+"_"+language_code+"+"_"+region code"+..+".properties"
 * Application+"_"+language_code+..+".properties"
 * 
 * @version $Revision: 1.2 $ 
 */
public final class ISO8859Checker
{
    private ISO8859Checker()
    {
        // protects from instantiation 
    }

    public static String sanityCheck(String localeString)
    {
        if (localeString == null)
        {
            return null;
        }
        localeString = localeString.trim();
        
        // Extract application name
        int appIndex = localeString.indexOf('_');
        if (appIndex == -1)
        {
            // No further "_" so this is "{application}" only
            return null;
        }
        else
        	return localeString.substring(0, appIndex);        
    }
    
    /**
     * Convert a string based locale into a Locale Object.
     * Assumes the string has form "{language}_{country}_{variant}".
     *  
     * @param localeString The String
     * @return the Locale
     */
    public static Locale getLocaleFromString(String localeString,Predicate<String> p)
    {
        if (localeString == null)
        {
            return null;
        }
        localeString = localeString.trim();


        String extension = "";
        int i = localeString.lastIndexOf('.');
        if (i > 0) {
            extension = localeString.substring(i+1);
        }
        
        if (!p.test(extension)) {
        	return null;
        }
        
        // get application name end position
        int appIndex = localeString.indexOf('_');

        // Extract language
        int languageIndex = localeString.indexOf('_',appIndex + 1);
        String language = null;
        if (languageIndex == -1)
        {
            // No further "_" so is "{language}" only
        	language = localeString.substring(appIndex + 1, appIndex + 3);
        	return new Locale(language, language.toUpperCase());
        }
        else
        {
            language = localeString.substring(appIndex + 1, languageIndex);
        }        
        
        // Extract country
        int countryIndex = localeString.indexOf('_', languageIndex + 1);
        String country = null;
        if (countryIndex == -1)
        {
            // No further "_" so is "{language}_{country}"
            country = localeString.substring(languageIndex+1);
            country = country.substring(0, 2);
            return new Locale(language, country);
        }
        else
        {
            // Assume all remaining is the variant so is "{language}_{country}_{variant}"
            country = localeString.substring(languageIndex+1, countryIndex);
            String variant = localeString.substring(countryIndex+1);
            return new Locale(language, country, variant);
        	//return new Locale(language, language.toUpperCase());
        }
    }
}

