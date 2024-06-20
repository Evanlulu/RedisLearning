package com.evanlu.thinkinjava.enumtest;

enum SecurityCategory {
    STOCK(Security.Stock.class), BOUND(Security.Bond.class);
    Security[] values;
    SecurityCategory(Class<? extends Security> kind){
        values = kind.getEnumConstants();
    }
    interface Security {
        enum Stock implements Security{ SHORT,LONG, MARGIN }
        enum Bond implements Security{ MUNICIPAL, JUNK }
    }


}
