package th.co.maximus.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GetMacAddress {

	public static String getMACAddress() {
		String macAddress = "";
		try {
				
			StringBuilder sb = new StringBuilder();
			
			Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
	        NetworkInterface inter;
	        while (networks.hasMoreElements()) {
	            inter = networks.nextElement();
	            byte[] mac = inter.getHardwareAddress();
	            if (mac != null) {
	                for (int i = 0; i < mac.length; i++) {
//	                    System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
	                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
	                }
	                sb.append("||");
	            }
	        }
	        macAddress = sb.toString();
	        System.out.println(macAddress);
				
		} catch (SocketException e){
				
			e.printStackTrace();
				
		}
        
		return macAddress;
	}
	
}
