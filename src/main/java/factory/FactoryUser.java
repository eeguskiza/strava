package factory;

import serviceGateway.FacebookGatewayI;
import serviceGateway.GoogleGatewayI;
import serviceGateway.IServiceGateway;

public class FactoryUser {
    public static IServiceGateway createService(String what) throws IllegalArgumentException {
        switch (what.toLowerCase()) {
            case "google":
                return new GoogleGatewayI();
            case "facebook":
                return new FacebookGatewayI();
            default:
                throw new IllegalArgumentException("Service " + what + " is not supported");
        }
    }
}
