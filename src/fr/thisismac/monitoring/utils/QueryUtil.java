/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Valentin 'ThisIsMac' Marchaud
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package fr.thisismac.monitoring.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

import fr.thisismac.monitoring.Core;
import fr.thisismac.monitoring.object.Server;

public class QueryUtil {
	
	/**
	 * Make a TCP request to server to check if its up or down
	 * An error will be set if there is any problem.
	 * 
	 * @param server Server where request will be send
	 * @return true if its online
	 */
	public static boolean startTCPQuery(Server server) {
        boolean result = false;
        try {
            Socket s = new Socket();
            s.setReuseAddress(true);
            SocketAddress sa = new InetSocketAddress(server.getHost(), server.getPort());
            s.connect(sa, Core.queryTimeOut);
            if (s.isConnected()) {
                s.close();
                result = true;
            }
        } catch (IOException e) {
        	server.setError("Bad response : " + e.getLocalizedMessage());
        	result = false;
        }
        
        return result;
    }
    
	/**
	 * Make a HTTP request to server to check if its up or down.
	 * An error will be set if there is any problem.
	 * 
	 * @param server Server where request will be send
	 * @return true if its online
	 */
     public static boolean startHttpQuery(Server server) {
        boolean result = false;
        try {
        	HttpURLConnection con = (HttpURLConnection) new URL(server.getHost()).openConnection();
        	if(String.valueOf(con.getResponseCode()).startsWith("2")) {
        		result = true;
        	} else {
        		server.setError("Bad response : " + con.getResponseCode() + " " + con.getResponseMessage());
        		return result;
        	}

        } catch (IOException e) {
        	server.setError("Bad response : " + e.getLocalizedMessage() + "" + e.getMessage());
            return result;
        }
        
        return result;
    }
}
