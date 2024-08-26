package com.sixdegreesit.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class SharedPrefrenceManager {
    private static final String SECURITY_SP = "security-sp";

    public static void setMemberDetailsInSP(final Context context,
                                            HashMap<String, String> defaultSectionInfoMap) {
        SharedPreferences sp = context.getSharedPreferences(SECURITY_SP,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Set<String> keySet = defaultSectionInfoMap.keySet();

        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            editor.putString(key, defaultSectionInfoMap.get(key));
        }
        editor.commit();
    }

    /***
     * @param context
     * @return HashMap<String, String> defaultSectionInfoMap
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, String> getMemberDetailsFromSP(
            final Context context) {
        SharedPreferences sp = context.getSharedPreferences(SECURITY_SP,
                Context.MODE_PRIVATE);
        return (HashMap<String, String>) sp.getAll();
    }
    
    public static void removeMemberDetailsFromSP(
            final Context context) {
        SharedPreferences sp = context.getSharedPreferences(SECURITY_SP,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
    }

}