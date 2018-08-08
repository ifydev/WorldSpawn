package me.ifydev.worldspawn.api.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Innectic
 * @since 08/07/2018
 */
public class MiscUtil {

    public static<T> List<T> convertIteratorToList(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) list.add(iterator.next());
        return list;
    }
}
