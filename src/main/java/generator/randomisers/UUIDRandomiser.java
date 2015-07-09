package generator.randomisers;

import java.util.LinkedHashMap;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;

public class UUIDRandomiser implements IRandomiserFunctionality {
	
    private Logger logger = Logger.getLogger(UUIDRandomiser.class);
	private int nulls;
	private Random nullGen;

	@Override
	public void setRandomiserInstance(RandomiserInstance ri) {
        String sNull;
        LinkedHashMap hashMap;
        
        hashMap  = ri.getProperties();
        sNull    = (String) hashMap.get("nullField");
        
        try {
            nulls = Integer.parseInt(sNull);
        }
        catch(Exception e) {
            logger.warn("Error setting the numerical values", e);
            nulls=0;
        }
        
        nullGen = new Random();
	}

	@Override
	public Object generate() {
		int prob;
		String randUUID = null;
		
		prob = nullGen.nextInt(100);
		
		if (prob >= nulls) {
			randUUID = UUID.randomUUID().toString().replaceAll("-", "");
		}
		
		return randUUID;
	}

	@Override
	public void destroy() {
	}

}
