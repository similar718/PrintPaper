package appliction;

import android.app.Application;

import utils.CrashHandler;

public class CrashHandlerApplication extends Application {
	
	private static CrashHandlerApplication mInstance;

	@Override
	public void onCreate() {
	    super.onCreate(); 
	    mInstance = this;
	    //Set exception handling procedures for the application here, and then our program 
	    //can catch the exception that is not handled
	    CrashHandler crashHandler = CrashHandler.getInstance();
	    crashHandler.init(this);
    }
	public static CrashHandlerApplication getInstance() {
	    return mInstance;
	}
}
