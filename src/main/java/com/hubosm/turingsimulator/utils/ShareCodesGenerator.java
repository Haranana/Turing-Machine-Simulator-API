package com.hubosm.turingsimulator.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ShareCodesGenerator {
    private static final List<Character> legalChars = List.of(
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '0','1','2','3','4','5','6','7','8','9'
    );
    private static final int codeLength = 5;

    public static String getShareCode(){
        char[] out = new char[codeLength];
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for(int i=0; i<codeLength; i++){
            int randomId = random.nextInt(legalChars.size());
            out[i] = legalChars.get(randomId);
        }

        return String.valueOf(out);
    }
}
