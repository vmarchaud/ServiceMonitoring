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
package fr.thisismac.monitoring.task;

import java.util.TimerTask;

import fr.thisismac.monitoring.Core;
import fr.thisismac.monitoring.object.Server;
import fr.thisismac.monitoring.object.Server.State;
import fr.thisismac.monitoring.utils.LoggerUtil;
import fr.thisismac.monitoring.utils.PushbulletUtil;
import fr.thisismac.monitoring.utils.LoggerUtil.Level;

/**
 * Query task 
 * @author ThisIsMac
 *
 */
public class ServerQueryCheck extends TimerTask {

    public void run() {
        long startTime;

        LoggerUtil.printToConsole("Starting server query...", Level.INFO);
        startTime = System.currentTimeMillis();
        
        for (Server server : Core.getServers().values()) {
        	// If a server doesnt awnser to ping request
            if (!server.query()) {
            	
            	// If a server is in planned maintenance, just continue
            	if(server.isUnderMaintenance()) continue;
            	
            	// If a server was previously down and he didnt awnser, just continue, i dont want a spam :(
            	if(server.getState() == State.DOWN) continue;
            	
                // Query it again, to prevent false positives
                if(server.getState() != State.UP) {
                	LoggerUtil.printToConsole("Server " + server.getName() + " failed to respond to query. Retrying in 5 seconds.", Level.INFO);
                }
                
                // Sleep for 5 seconds before querying again
                try {
                    Thread.sleep(5000); 
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                boolean retry = server.query(); // Retry query

                if (!retry) {
                    LoggerUtil.printToConsole("Server " + server.getName() + " failed to 2 times, he's now offline.", Level.INFO);

                    server.setState(false);
                    for (String name : server.getDevicesName()) {
                        boolean sent = false;
                        while (!sent) { // Retry until the notification successfully sends
                            sent = PushbulletUtil.sendPush(PushbulletUtil.getIdenFromName(name), server.getName() + " is currently offline!", server.getError());
                        }
                    }
                }
                else {
                	
                }

            } 
         // If server awnser to ping request
            else {  
            	// If server was down
                if (server.getState() == State.DOWN) {
                	// Update his state
                    server.setState(true);
                    // Send an notification
                    if (Core.sendBackOnlineAlert) {
                        for (String name : server.getDevicesName()) {
                            boolean sent = false;
                            while (!sent) { // Retry until the notification successfully send
                                sent = PushbulletUtil.sendPush(PushbulletUtil.getIdenFromName(name), server.getName() + " is now back online!", "Everything is fine");
                            }
                        }
                    }
                }
            }
        }

        LoggerUtil.printToConsole("Server query complete! Took " + ((System.currentTimeMillis() - startTime) / 1000.0) + " seconds", Level.INFO);

    }

}
