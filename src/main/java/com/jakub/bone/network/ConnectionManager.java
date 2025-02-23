package com.jakub.bone.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface ConnectionManager {

    void connect(String host, int port) throws IOException;

    PrintWriter getOut();

    BufferedReader getIn();

    void disconnect();
}
