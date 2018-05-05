package com.tjlcast.server;

/**
 * Created by tangjialiang on 2018/5/2.
 */
public interface Constant {

    final int ZK_SESSION_TIMEOUT = 5000 ;

    final String ZK_REGISTER_PATH = "/registry" ;

    final String ZK_DATA_PATH = ZK_REGISTER_PATH + "/data" ;
}
