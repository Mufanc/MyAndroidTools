package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

import java.util.List;

public interface IActivityManager extends IInterface {

    List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses();

    long[] getProcessPss(int[] pids);

    abstract class Stub extends Binder implements IActivityManager {
        public static IActivityManager asInterface(IBinder iBinder) {
            throw new RuntimeException("stub!");
        }
    }
}
