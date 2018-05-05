package com.tjlcast.test;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

/**
 * Created by tangjialiang on 2018/5/4.
 */
public class TestForObjenesis {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Objenesis objenesis = new ObjenesisStd(); // or ObjenesisSerializer
        MyThingy thingy1 = (MyThingy) objenesis.newInstance(MyThingy.class);

        while(true) ;
//        Objenesis objenesis = new ObjenesisStd(); // or ObjenesisSerializer
//        ObjectInstantiator thingyInstantiator = objenesis.getInstantiatorOf(MyThingy.class);
//
//        MyThingy thingy2 = (MyThingy)thingyInstantiator.newInstance();
//        MyThingy thingy3 = (MyThingy)thingyInstantiator.newInstance();
//        MyThingy thingy4 = (MyThingy)thingyInstantiator.newInstance();
    }
}
