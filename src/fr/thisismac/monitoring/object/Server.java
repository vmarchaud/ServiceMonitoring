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
package fr.thisismac.monitoring.object;

import java.util.ArrayList;
import java.util.Calendar;

import fr.thisismac.monitoring.utils.QueryUtil;

/**
 * Server object
 * @author ThisIsMac
 *
 */
public class Server {

	// All parameters given with the constructor
    private String name;
    private String hostname;
    private int port;
    private ArrayList<String> devicesName = new ArrayList<String>();
    private ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
    
    
    // Set the default serverType to unknow if its doesnt recognize in config
    private ServerType serverType = ServerType.UNKNOWN;
    // Set the default error to nothing to prevent NPE
    private String error = "";
    // Set the default state to unknow
    private State state = State.UNKNOWN;
    
    // Object used
    private Calendar cal;
    
    /**
     * Construct a server 
     * @param name name of the server
     * @param hostname adress of the server
     * @param port port of the server
     * @param serverType type of the server
     */
    public Server(String name, String hostname, int port, ServerType serverType) {
        this.name = name;
        this.hostname = hostname;
        this.port = port;
        this.serverType = serverType;
    }

    /**
     * Get the name of the server
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the adress of the server
     * @return host address
     */
    public String getHost() {
        return hostname;
    }

    /**
     * Get the port of the server
     * @return port 
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Get pushbullet's device linked to this server
     * @return devices list
     */
    public ArrayList<String> getDevicesName() {
        return devicesName;
    }

    /**
     * Get the type of this server
     * @return ServiceType
     */
    public ServerType getServiceType() {
        return serverType;
    }
    
    /**
     * Get the current state of this server
     * @return State
     */
    public State getState() {
    	return state;
    }
    
    /**
     * Set error if query failed
     * @param error why it failed
     */
    public void setError(String error) {
    	this.error = error;
    }
    
    /**
     * Set the current state of the server from a boolean (true if online or false if offline)
     * @param online
     */
    public void setState(boolean online) {
    	if(online) {
    		state = State.UP;
    	} else {
    		state = State.DOWN;
    	}
    }
    
    /**
     * Get error if query failed
     * @return error
     */
    public String getError() {
    	return error;
    }

    /**
     * Start a query for the given server
     * @return true if its awnser to the request
     */
    public boolean query() {
    	if(serverType == ServerType.HTTP) return QueryUtil.startHttpQuery(this);
    	else if (serverType == ServerType.TCP) return QueryUtil.startTCPQuery(this);
    	return false;
    }
    
    /**
     * Get planned maintenance of this server 
     * @return maintenance list
     */
    public ArrayList<Maintenance> getPlannedMaintenance() {
    	return maintenances;
    }
    
    /**
     * Know if a server is down because off a maintenance or anything that was be planed
     * @return true if its down for maintenance raison
     */
    public boolean isUnderMaintenance() {
    	boolean result = false;
    	cal = Calendar.getInstance();
    	for(Maintenance maintenance : maintenances) {
    		if(cal.get(Calendar.HOUR_OF_DAY) >= maintenance.getBegin_HOUR() && cal.get(Calendar.MINUTE) >= maintenance.getBegin_MINUTE() && cal.get(Calendar.HOUR_OF_DAY) <= maintenance.getEnd_HOUR() && cal.get(Calendar.MINUTE) <= maintenance.getEnd_MINUTE()) {
    			result = true;
    		}
    	}
    	
    	return result;
    }
    
    /**
     * Type of a server, used to use different way to make a request
     * @author Mac'
     */
    public enum ServerType {

        TCP, HTTP, UNKNOWN;

    }
    
    /**
     * State of a server, sued to know if a server is online or not
     * @author Mac'
     */
    public enum State {

        UP, DOWN, UNKNOWN;

    }
}
