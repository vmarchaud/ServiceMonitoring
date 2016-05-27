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
package fr.thisismac.monitoring;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;

import fr.thisismac.monitoring.object.Server;
import fr.thisismac.monitoring.task.ServerQueryCheck;
import fr.thisismac.monitoring.utils.ConfigUtil;
import fr.thisismac.monitoring.utils.LoggerUtil;
import fr.thisismac.monitoring.utils.PushbulletUtil;
import fr.thisismac.monitoring.utils.LoggerUtil.Level;

/**
 * Core class of the server
 * @author ThisIsMac
 */

public class Core {

	// Servers data
    public static HashMap<String, Server> servers = new HashMap<String, Server>();
    
    // Configuration
	public static int queryTimeOut = 6000;
    public static int queryInverval = 30;
    public static boolean sendBackOnlineAlert = true;
    public static String apiKey = "";
    
    // Object
    @SuppressWarnings("unused")
	private static LoggerUtil logger;
    public Timer queryCheckTimer = new Timer();
    public final static ServerQueryCheck queryChecker = new ServerQueryCheck();

    
    public Core() {
    	// Initialize logger
    	logger = new LoggerUtil();
    	
        LoggerUtil.printToConsole("Starting ServerTracker ...", Level.INFO);
        LoggerUtil.printToConsole("Loading configuration...", Level.INFO);

        // Handle configuration
        if (!(new File("config.xml").exists())) {
            ConfigUtil.copyConfig();
            LoggerUtil.printToConsole("No config file existed, so default one was created for you. Please edit the config file and restart.", Level.ERROR);
            shutdown();
        }
       
        ConfigUtil.loadConfiguration();

        // Handle pushbullet client
        PushbulletUtil.registerKey(apiKey);
        PushbulletUtil.initializeClient();
        
        // Schedule repeating task to ping
        queryCheckTimer.scheduleAtFixedRate(queryChecker, queryInverval * 1000, queryInverval * 1000);
        
        LoggerUtil.printToConsole("Successfully initialised!", Level.INFO);
        
        // Start repeating task
        queryChecker.run();
        
        
        // Wait for input command
        listenForCommand();
    }


	@SuppressWarnings("resource")
	public void listenForCommand() {
        Scanner reader = new Scanner(System.in);

        String command;
        while ((command = reader.nextLine()) != null) {
        	switch(command) {
	        	case "end" : {
	        		 LoggerUtil.printToConsole("Shutting down ...!", Level.INFO);
	                 shutdown();
	        	}
        	}
        }
    }

	/**
	 * Use to shutdown properly the server
	 */
    public void shutdown() {
    	LoggerUtil.close();
        queryChecker.cancel();
    	System.exit(0);
    }
    
	/**
	 * Get server list
	 * 
	 * @return server list
	 */
    public static HashMap<String, Server> getServers() {
        return servers;
    }
}
