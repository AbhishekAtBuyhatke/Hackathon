package lab.abhishek.apiaiimplementation.Models;

/**
 * Created by Bhargav on 9/21/2016.
 */
public interface ResultCallBack<T> {
    void onSuccess(T result);
    void onError(Throwable throwable);
}
