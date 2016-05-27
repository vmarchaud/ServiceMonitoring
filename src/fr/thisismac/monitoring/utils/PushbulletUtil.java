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

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonParser;

import net.iharder.jpushbullet2.Device;
import net.iharder.jpushbullet2.PushbulletClient;
import net.iharder.jpushbullet2.PushbulletException;

/**
 * Util to link the notification to pushbullet
 * @author Mac'
 *
 */
public class PushbulletUtil {

    private static PushbulletClient pushClient;
    private static String key;
    private static List<Device> devices = new ArrayList<Device>();

    /**
     * Register the client api key
     * @param apiKey key for the api
     */
	public static void registerKey(String apiKey) {
		key = apiKey;
	}

	/**
	 * Use for initialize the client with api key and get all devices linked to the client.
	 */
	public static void initializeClient() {
		pushClient = new PushbulletClient(key);
		try {
			devices = pushClient.getActiveDevices();
		} catch (PushbulletException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a push notification
	 * 
	 * @param device where the notification will be send
	 * @param title title of the notification
	 * @param body message of the notification
	 * @return true if the push notification is successfull
	 */
	public static boolean sendPush(String device, String title, String body) {
		boolean result = false;
		String temp = "";
		try {
			temp = pushClient.sendNote(device, title, body);
		} catch (PushbulletException e) {
			e.printStackTrace();
		}
		result = Boolean.valueOf(new JsonParser().parse(temp).getAsJsonObject().get("active").toString());
		return result;
	}
	
	
	/**
	 * Get Iden of an device from his name
	 * @param name name of device 
	 * @return iden of this device
	 */
	public static String getIdenFromName(String name) {
		for(Device device : devices) {
			if(device.getNickname().equalsIgnoreCase(name)) {
				return device.getIden();
			}
		}
		
		return null;
	}
	
}
