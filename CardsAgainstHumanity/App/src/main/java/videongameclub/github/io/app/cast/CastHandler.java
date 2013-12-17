package videongameclub.github.io.app.cast;

import android.content.Context;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;

import com.google.cast.ApplicationChannel;
import com.google.cast.ApplicationMetadata;
import com.google.cast.ApplicationSession;
import com.google.cast.CastContext;
import com.google.cast.CastDevice;
import com.google.cast.MediaRouteAdapter;
import com.google.cast.MediaRouteHelper;
import com.google.cast.MediaRouteStateChangeListener;
import com.google.cast.SessionError;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by brobzilla on 12/13/13.
 */
public class CastHandler extends MediaRouter.Callback implements MediaRouteAdapter, ApplicationSession.Listener {

    private static final String TAG = "CastHandler";
    private static CastHandler mInstance;

    /**
     * Who wants to know about casting?
     */
    private Set<ICastListener> mListeners;

    /**
     * This is APP_NAME but it is actually the app id that we received from Google.
     */
    private static final String APP_NAME = "TicTacToe";

    private ApplicationSession mSession;

    private CastContext mCastContext;
    private CastDevice mSelectedDevice;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private Context mContext;

    synchronized public static CastHandler getInstance(Context context){
        if (null == mInstance) {
            mInstance = new CastHandler(context);
        }
        return mInstance;
    }


    private CastHandler(Context context) {
        mContext = context;
        mListeners = new HashSet<ICastListener>();

        mCastContext = new CastContext(context);
        MediaRouteHelper.registerMinimalMediaRouteProvider(mCastContext, this);
        mMediaRouter = MediaRouter.getInstance(context);
        mMediaRouteSelector = MediaRouteHelper.buildMediaRouteSelector(
                MediaRouteHelper.CATEGORY_CAST, APP_NAME, null);

        mMediaRouter.addCallback(mMediaRouteSelector, this,
                MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
    }

    private void setSelectedDevice(CastDevice device) {
        mSelectedDevice = device;

        if (mSelectedDevice != null) {
            mSession = new ApplicationSession(mCastContext, mSelectedDevice);
            mSession.setListener(this);

            try {
                mSession.startSession(APP_NAME);
            } catch (IOException e) {
                Log.e(TAG, "Failed to open a session", e);
            }
        } else {
            endSession();
        }
    }

    /**
     * Ends any existing application session with a Chromecast device.
     */
    private void endSession() {
        if ((mSession != null) && (mSession.hasStarted())) {
            try {
                if (mSession.hasChannel()) {
                    //mGameMessageStream.leave();
                }
                mSession.endSession();
            } catch (IOException e) {
                Log.e(TAG, "Failed to end the session.", e);
            } catch (IllegalStateException e) {
                Log.e(TAG, "Unable to end session.", e);
            } finally {
                mSession = null;
            }
        }
    }

    @Override
    public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo route) {
        super.onRouteSelected(router, route);
        MediaRouteHelper.requestCastDeviceForRoute(route);
    }

    @Override
    public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo route) {
        super.onRouteUnselected(router, route);
        setSelectedDevice(null);
    }

    @Override
    public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo route) {
        super.onRouteAdded(router, route);
    }

    @Override
    public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo route) {
        super.onRouteRemoved(router, route);
    }

    @Override
    public void onRouteChanged(MediaRouter router, MediaRouter.RouteInfo route) {
        super.onRouteChanged(router, route);
    }

    @Override
    public void onRouteVolumeChanged(MediaRouter router, MediaRouter.RouteInfo route) {
        super.onRouteVolumeChanged(router, route);
    }

    @Override
    public void onRoutePresentationDisplayChanged(MediaRouter router, MediaRouter.RouteInfo route) {
        super.onRoutePresentationDisplayChanged(router, route);
    }

    @Override
    public void onProviderAdded(MediaRouter router, MediaRouter.ProviderInfo provider) {
        super.onProviderAdded(router, provider);
    }

    @Override
    public void onProviderRemoved(MediaRouter router, MediaRouter.ProviderInfo provider) {
        super.onProviderRemoved(router, provider);
    }

    @Override
    public void onProviderChanged(MediaRouter router, MediaRouter.ProviderInfo provider) {
        super.onProviderChanged(router, provider);
    }

    /* implements from MediaRouteAdapter */
    @Override
    public void onDeviceAvailable(CastDevice castDevice, String s, MediaRouteStateChangeListener mediaRouteStateChangeListener) {
          setSelectedDevice(castDevice);
    }

    /* implements from MediaRouteAdapter */
    @Override
    public void onSetVolume(double v) {

    }

    /* implements from MediaRouteAdapter */
    @Override
    public void onUpdateVolume(double v) {

    }

    /* implements from ApplicationSession.Listener */
    @Override
    public void onSessionStarted(ApplicationMetadata applicationMetadata) {
        ApplicationChannel channel = mSession.getChannel();
        if (channel == null) {
            Log.w(TAG, "onStarted: channel is null");
            return;
        }
        //channel.attachMessageStream(mGameMessageStream);
        //mGameMessageStream.join("MyName");
    }

    /* implements from ApplicationSession.Listener */
    @Override
    public void onSessionStartFailed(SessionError sessionError) {

    }

    /* implements from ApplicationSession.Listener */
    @Override
    public void onSessionEnded(SessionError sessionError) {
        if ((mSession != null) && (mSession.hasStarted())) {
            try {
                if (mSession.hasChannel()) {
                    //mGameMessageStream.leave();
                }
                mSession.endSession();
            } catch (IOException e) {
                Log.e(TAG, "Failed to end the session.", e);
            } catch (IllegalStateException e) {
                Log.e(TAG, "Unable to end session.", e);
            } finally {
                mSession = null;
            }
        }
    }

    public void addCastListener(ICastListener listener) {
        if (null != listener) {
            boolean result = mListeners.add(listener);
            if (result) {
                Log.d(TAG, "Successfully added the new listener " + listener);
            } else {
                Log.d(TAG, "Adding Listener " + listener + " was already registered, skipping this step");
            }
        }
    }

    public void removeCastListener(ICastListener listener) {
        if (null != listener) {
            mListeners.remove(listener);
        }
    }

    /**
     * Clean up method.
     */
    public void onDestroy() {
        endSession();
        mMediaRouter.removeCallback(this);

        MediaRouteHelper.unregisterMediaRouteProvider(mCastContext);
        if (mCastContext != null) {
            mCastContext.dispose();
            mCastContext = null;
        }
    }

    public MediaRouteSelector getMediaRouteSelector() {
        return mMediaRouteSelector;
    }

}
