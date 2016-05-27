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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import fr.thisismac.monitoring.Core;
import fr.thisismac.monitoring.object.Maintenance;
import fr.thisismac.monitoring.object.Server;
import fr.thisismac.monitoring.object.Server.ServerType;
import fr.thisismac.monitoring.utils.LoggerUtil.Level;

/**
 * Util to load the configuration
 * @author Mac'
 *
 */
public class ConfigUtil {

	/**
	 * Load configuration from config.xml file
	 */
	@SuppressWarnings("unchecked")
	public static void loadConfiguration() {
        SAXBuilder builder = new SAXBuilder();
        Document document;
        FileInputStream fileStream;
        
        // Start loading
		try {
			fileStream = new FileInputStream("config.xml");
			

			// Put config in a string builder
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileStream));
	        final StringBuilder stringBuilder = new StringBuilder();
	        String line;
	        while ((line = bufferedReader.readLine()) != null) stringBuilder.append(line); 

	        bufferedReader.close();

	        // If its not empty (and it shouldnt)
	        if (!stringBuilder.toString().isEmpty()) {
	            // Build the document
	            document = builder.build(new StringReader(stringBuilder.toString()));

	            // Get general settings
	            Element generalSettings = (Element) document.getRootElement().getChildren("general").get(0);
	            Core.queryInverval = Integer.parseInt(generalSettings.getChildText("queryInterval"));
	            Core.sendBackOnlineAlert = Boolean.parseBoolean(generalSettings.getChildText("sendBackOnlineAlert"));
	            Core.queryTimeOut = Integer.parseInt(generalSettings.getChildText("queryTimeOut"));
	            Core.apiKey = String.valueOf(generalSettings.getChildText("apiKeyForPushbullet"));
	            
	            
	            // Get Servers
	            Element elementServers = (Element) document.getRootElement().getChildren("servers").get(0);
	            for (Object item : elementServers.getChildren("server")) {
	                Element element = (Element) item;
	                
	                // Set basic variable
	                String name = element.getChildText("name");
	                String hostname = element.getChildText("hostname");
	                int port = Integer.parseInt(element.getChildText("port"));

	                // Set the service type
	                ServerType type = ServerType.UNKNOWN;
		            if (element.getChildText("type") != null) {
		                type = ServerType.valueOf(element.getChildText("type").toUpperCase());
		            }
	                
		            // Build the server object
	                Server server = new Server(name, hostname, port, type);

	                // Device names to send the alerts to
	                Element sendTo = (Element) element.getChildren("sendAlertTo").get(0);
	                for (Object device : sendTo.getChildren("device")) {
	                	server.getDevicesName().add(((Element) device).getValue());
	                }
	                
	                // Maintenance list of when the server will be offline for planned thing
	                List<Object> plannedMaintenance = element.getChildren("ignoreDownAt");
	                // If no maintenance has been found just go one
	                if(plannedMaintenance != null) {
	                	for (Object maintenance : plannedMaintenance) {
	                		Element one = (Element) maintenance;
		                	server.getPlannedMaintenance().add(new Maintenance(one.getChild("begin").getValue(), one.getChild("end").getValue()));
		                }
	                }
	                
	             // Put the server in server list
	                Core.servers.put(name, server);
	                
	            }
	        }
		} catch (IOException | JDOMException e) {
        	LoggerUtil.printToConsole(e.getMessage(), Level.ERROR);
            LoggerUtil.printToConsole("An error occured while loading configuration! Make sure it includes all settings included in the default config and that data types are correct.", Level.ERROR);
            
		}
        
    }

	/**
	 * Copy the default config next to the jar to be edited by the user
	 */
    public static void copyConfig() {
        InputStream stream = Core.class.getResourceAsStream("/config.xml");
        OutputStream resStreamOut;
        int readBytes;
        byte[] buffer = new byte[4096];
        try {
            resStreamOut = new FileOutputStream(new File("config.xml"));
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }

            stream.close();
            resStreamOut.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
