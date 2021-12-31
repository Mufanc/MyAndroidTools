package android.content.pm;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface IPackageManager extends IInterface {

    void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage);

    void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId);

    abstract class Stub extends Binder implements IPackageManager {
        public static IPackageManager asInterface(IBinder obj) {
            throw new RuntimeException("stub!");
        }
    }
}
