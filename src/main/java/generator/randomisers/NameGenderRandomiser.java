/*
 * dgMaster: A versatile, open source data generator.
 *(c) 2007 M. Michalakopoulos, mmichalak@gmail.com
 */


package generator.randomisers;
import generator.misc.Utils;
//import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Vector;
import org.apache.log4j.Logger;
import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import java.io.File;


public class NameGenderRandomiser implements IRandomiserFunctionality
{
    Logger logger = Logger.getLogger(NameGenderRandomiser.class);
    int nulls;
    int iFNames;
    Random nullGen, gen1;    
    Vector<String> vFirstNames;
            
    
    /** Creates a new instance of FirstnameRandomiser */
    public NameGenderRandomiser()
    {
    }

    public void setRandomiserInstance(RandomiserInstance ri)
    {
        LinkedHashMap<String, String> hashMap;
        String sNull, sFnameSeed;
        long   fnameSeed;
        

        hashMap = ri.getProperties();
        sFnameSeed  = (String) hashMap.get("FirstnameSeedField");
        sNull  = (String) hashMap.get("nullField");
        try
        {
            nulls = Integer.parseInt(sNull);
            fnameSeed = Long.parseLong(sFnameSeed);
        }
        catch(Exception e)
        {
            logger.warn("Error setting the numerical values", e);
            fnameSeed = System.currentTimeMillis();
        }        
        
        Utils utils = new Utils();
        
        logger.debug("loading file1");
        vFirstNames = utils.readResource("resources" + File.separator + "en_firstnames.txt");
        iFNames = vFirstNames.size();

        nullGen = new Random();
        gen1        = new Random(fnameSeed);
        
        logger.debug("loading files.Done");        
    }

    public Object generate()
    {
        //first "consume" the generator nextInt,
        // so as to keep compatibility with email randomiser

        int i2 = gen1.nextInt(iFNames);        
        
         //check the nulls
        int prob;
        prob = nullGen.nextInt(100);
        if(prob<nulls)
            return "Unidentified";
        
        int len;
        String gender;

        
        //get firstname, avoid the last two chars ,f or ,m
        gender = vFirstNames.elementAt(i2);
        len   = gender.length();
        gender = gender.substring(len-1, len);

        if ("m".equals(gender)) {
            gender = "Male";
        } else if ("f".equals(gender)) {
            gender = "Female";
        } else {
            gender = "Unidentified";
        }
        
        return gender;        
    }

    public void destroy()
    {
    }
    
}
