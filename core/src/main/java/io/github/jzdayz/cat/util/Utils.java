package io.github.jzdayz.cat.util;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Utils {
    public static <K> void remove(Iterator<K> iterator, Predicate<K> test){
        while (iterator.hasNext()){
            K next = iterator.next();
            if (test.test(next)){
                iterator.remove();
            }
        }
    }

    public static <K> K operation(K k, Consumer<K> handle){
        handle.accept(k);
        return k;
    }


}
