package com.zdmoney.credit.common.util;

import java.util.Random;

/**
 * 
* @ClassName: RandomNumberUtils 
* @author liyl 
* @date 2016年11月24日 下午4:04:22 
*
 */
public class RandomNumberUtils {

    public static String getfourRandomNumber(){
        Random random = new Random();
        String fourRandom = random.nextInt(10000) + "";
        int randLength = fourRandom.length();
        if(randLength<4){
          for(int i=1; i<=4-randLength; i++)
              fourRandom = "0" + fourRandom  ;
      }
        return fourRandom;
    }
    
}