package com.allan.boardbuddies;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Utilities {
    public static @Nullable String getFileAsString(File file){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> Boolean compareArraylist(ArrayList<T> list1, ArrayList<T> list2){ //returns true if arraylists are equal
        if (list1 == list2){
            return true;
        }
        if (list1 == null || list2 == null){
            return false;
        }
        if (list1.size() != list2.size()){
            return false;
        }
        Iterator<T> it1 = list1.iterator();
        Iterator<T> it2 = list2.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            T t1 = it1.next();
            T t2 = it2.next();
            if (t1 instanceof float[]){
                if (!Arrays.equals((float[]) t1, (float[]) t2)) {
                    return false;
                }
            } else {
                if (!t1.equals(t2)) {
                    return false;
                }
            }
        }
        return true;
    }
}