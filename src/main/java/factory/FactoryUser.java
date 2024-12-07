package factory;

import serviceGateway.FacebookGateway;
import serviceGateway.GoogleGateway;
import serviceGateway.ServiceGateway;

public class FactoryUser {
    public static ServiceGateway selectService(String what) {
        String service = what.toLowerCase();
        //Swich case to select the service
        switch (what) {
            case "google":
                return new GoogleGateway();
            case "facebook":
                return new FacebookGateway();
            default:
                return null;
        }
    }
}
