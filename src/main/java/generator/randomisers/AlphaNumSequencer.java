package generator.randomisers;

import java.util.LinkedHashMap;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;

public class AlphaNumSequencer implements IRandomiserFunctionality {

    Logger  logger = Logger.getLogger(AlphaNumSequencer.class);
    
    Random  nullGen;
    int     alphaNumSize, nulls, seqSize;
    boolean useCaps;
    char[]  chSmallLetters = new char[36];
    char[]  chCapsLetters  = new char[36];
    long    counter;
    
    
    public AlphaNumSequencer() {
    }

    public void setRandomiserInstance(RandomiserInstance ri) {
        String sNull, sCounter, sAlphaNumSize, sSeqSize, sUseCaps;
        LinkedHashMap<String, String> hashMap;
        
        hashMap = ri.getProperties();
        sNull  = (String) hashMap.get("nullField");
        sCounter = (String) hashMap.get("counterStartValue");
        sAlphaNumSize = (String) hashMap.get("alphaNumSize");
        sSeqSize = (String) hashMap.get("seqSize");
        sUseCaps = (String) hashMap.get("useCaps");
        try {
            nulls        = Integer.parseInt(sNull);
            counter      = Long.parseLong(sCounter);
            alphaNumSize = Integer.parseInt(sAlphaNumSize);
            seqSize      = Integer.parseInt(sSeqSize);
            useCaps      = Boolean.parseBoolean(sUseCaps);
        } catch(Exception e) {
            logger.warn("Error setting the numerical values", e);
            nulls=0;
            counter=0;
        }

        chSmallLetters[0] = 'a';
        chCapsLetters[0]  = 'A';
        for (int i=1; i<26; i++) {
            chSmallLetters[i] = (char)('a'+ i);
            chCapsLetters[i]  = (char)('A'+ i);
        }
        for (int i=0; i<10; i++) {
            chSmallLetters[i+26] = Character.forDigit(i, 10);
            chCapsLetters[i+26]  = Character.forDigit(i, 10);
        }

        nullGen  = new Random();
    }

    public Object generate() {
        int prob, currSeq;
        String seqFormat;
        StringBuffer sb = new StringBuffer(alphaNumSize + seqSize);
        char[] charList;

        prob = nullGen.nextInt(100);

        if (useCaps) {
            charList = chCapsLetters;
        } else {
            charList = chSmallLetters;
        }

        if (prob<nulls) {
            sb = null;
        } else {
            currSeq = (int) counter;
            counter++;
            
            seqFormat = "%" + Integer.toString(seqSize) + "d";

            sb.append(RandomStringUtils.random(alphaNumSize, charList));
            sb.append(String.format(seqFormat, currSeq));
        }

        return sb.toString();
    }

    public void destroy() {
    }
    
}
