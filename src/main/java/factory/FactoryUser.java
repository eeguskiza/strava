package factory;

import serviceGateway.FacebookGateway;
import serviceGateway.GoogleGateway;
import serviceGateway.ServiceGateway;

public class FactoryUser {
	public static ServiceGateway selectService(String what) {
        if (what.equals("Google")) {
        	return new GoogleGateway();
        }
        else if (what.equals("Facebook")) {
            return new FacebookGateway();
        }
        else {
        	return null;
        }
    }

}
