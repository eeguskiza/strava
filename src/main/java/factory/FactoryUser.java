package factory;

import serviceGateway.FacebookGateway;
import serviceGateway.GoogleGateway;
import serviceGateway.ServiceGateway;

public class FactoryUser {
    public static ServiceGateway selectService(String what) {
        if (what.equalsIgnoreCase("Google")) {
            return new GoogleGateway();
        } else if (what.equalsIgnoreCase("Facebook")) {
            return new FacebookGateway();
        } else {
            return null;
        }
    }
}
