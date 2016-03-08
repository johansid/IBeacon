package com.app.market.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by OmarAli on 26/01/2016.
 */
public class BusProvider {
    public static final Bus BUS=new Bus(ThreadEnforcer.ANY);

    /**
     * Returns an instance of Bus class
     */
    public static Bus getInstance(){
        return BUS;
    }
    private BusProvider(){

    }
}
