package ir.engineerpc.shoecenter.MyClass.OnlineClass;

public interface ResponseFromNet {
    void onRequestSuccess(String response);
    void onRequestError(String errorMessage);
}
