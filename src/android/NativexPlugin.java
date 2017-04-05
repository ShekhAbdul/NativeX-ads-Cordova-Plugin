
import android.util.Log;
import android.webkit.WebView;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nativex.monetization.MonetizationManager;
import com.nativex.monetization.business.reward.Reward;
import com.nativex.monetization.communication.RedeemRewardData;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.listeners.OnAdEventV2;
import com.nativex.monetization.enums.NativeXAdPlacement;
import com.nativex.monetization.listeners.RewardListener;
import com.nativex.monetization.listeners.OnAdEvent;
import com.nativex.monetization.listeners.SessionListener;
import com.nativex.monetization.mraid.AdInfo;

public class NativexPlugin extends CordovaPlugin {
	public static final String TAG = "NativeX Plugin";
	
	public static final String EVENT_ALREADY_FETCHED = "ALREADY_FETCHED";
    public static final String EVENT_ALREADY_SHOWN = "ALREADY_SHOWN";
    public static final String EVENT_BEFORE_DISPLAY = "BEFORE_DISPLAY";
    public static final String EVENT_DISMISSED = "DISMISSED";
    public static final String EVENT_DOWNLOADING = "DOWNLOADING";
	public static final String EVENT_DISPLAYED = "DISPLAYED";
	public static final String EVENT_ERROR = "ERROR";
    public static final String EVENT_EXPIRED = "EXPIRED";
    public static final String EVENT_FETCHED = "FETCHED";
    public static final String EVENT_NO_AD = "NO_AD";
	public static final String EVENT_USER_NAVIGATES_OUT_OF_APP = "USER_NAVIGATES_OUT_OF_APP";
	public static final String EVENT_VIDEO_COMPLETED = "VIDEO_COMPLETED";
	public static final String EVENT_DEFAULT = "default";
	public static final String EVENT_REWARDS = "rewards";
	
	public NativexPlugin() {}
	
	 @Override
    protected void pluginInitialize() {
    	super.pluginInitialize();
    	
	}

	
	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            Log.i(TAG, action);
			//webView.loadUrl("javascript:console.log('" + action + "');");
            if (action.equals("init")) {
				
				MonetizationManager.createSession(this.cordova.getActivity().getApplicationContext() , args.getString(0), args.getString(1), sessionListener);
				//MonetizationManager.createSession(this.cordova.getActivity().getApplicationContext() , "46624", sessionListener);
				//webView.loadUrl("javascript:console.log('Create Sesssion');");
				Log.v(TAG, "Create Sesssion");
                callbackContext.success();
            } else if (action.equals("onRedeem")) {
				MonetizationManager.setRewardListener(rewardListener);
				Log.d(TAG, "setRewardListener");
                callbackContext.success();
            } else if (action.equals("fetchAd")) {
                MonetizationManager.fetchAd(this.cordova.getActivity(), NativeXAdPlacement.Store_Open, onAdEventListener);
				//webView.loadUrl("javascript:console.log('Fetching ad');");
				Log.v(TAG, "fetchAd");
                callbackContext.success();
            } else if (action.equals("showReadyAd")) {
				MonetizationManager.showReadyAd(this.cordova.getActivity(), NativeXAdPlacement.Store_Open, null);
				MonetizationManager.setRewardListener(rewardListener);
				Log.v(TAG, "showReadyAd");
                callbackContext.success();
            }  else if (action.equals("isAdReady")) {
				if( MonetizationManager.isAdReady(NativeXAdPlacement.Store_Open) ) {
						MonetizationManager.showReadyAd(this.cordova.getActivity(), NativeXAdPlacement.Store_Open, null);
						Log.v(TAG, "isAdReady");
					}
                callbackContext.success();
            }  else if (action.equals("fetchAdVideo")) {
                MonetizationManager.fetchAd(this.cordova.getActivity(), NativeXAdPlacement.Main_Menu_Screen, onAdEventListener);
				//webView.loadUrl("javascript:console.log('Fetching ad');");
				Log.v(TAG, "fetchAdVideo");
                callbackContext.success();
            } else if (action.equals("showReadyAdVideo")) {
				MonetizationManager.showReadyAd(this.cordova.getActivity(), NativeXAdPlacement.Main_Menu_Screen, null);
				MonetizationManager.setRewardListener(rewardListener);
				Log.v(TAG, "showReadyAdVideo");
                callbackContext.success();
            }else {

                callbackContext.error("Unknown Action");
                return false;
            }
            return true;
        } catch (Exception e) {

            Log.e("NativexPlugin", e.getMessage());
            callbackContext.error("NativexPlugin: " + e.getMessage());
            return false;
        }
    }
	
	private SessionListener sessionListener = new SessionListener() {
		@Override
		public void createSessionCompleted(boolean success, boolean isOfferWallEnabled, String sessionId) {
			webView.loadUrl("javascript:console.log('" + sessionId + "');");
			if(success) {
				webView.loadUrl("javascript:console.log('Wahoo! Now I'm ready to show an ad.');");
				// a session with our servers was established successfully.
				// the app is now ready to show ads.
				System.out.println("Wahoo! Now I'm ready to show an ad.");
				
			} else {
				webView.loadUrl("javascript:console.log('Oh no! Something isn't set up correctly');");
				// establishing a session with our servers failed;
				// the app will be unable to show ads until a session is established 
				System.out.println("Oh no! Something isn't set up correctly - re-read the documentation or ask customer support for some help - https://selfservice.nativex.com/Help");
				
			}
		}
	};
	
	private void fireEvent(final String event,final String amt) {

        final CordovaWebView view = this.webView;
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.loadUrl(String.format("javascript:cordova.fireWindowEvent('%s', %s);", event, amt));
            }
        });
    }
	
	 RewardListener rewardListener = new RewardListener() {
        @Override
        public void onRedeem(RedeemRewardData rewardData) {
			webView.loadUrl("javascript:console.log('RedeemRewardData');");
            // take possession of the balances returned here
            int totalRewardAmount = 0;
            for (Reward reward : rewardData.getRewards()) {
                Log.d("Sample", "Reward: rewardName:" + reward.getRewardName()
                        + " rewardId:" + reward.getRewardId()
                        + " amount:" + Double.toString(reward.getAmount()));
                // add the reward amount to the total
                totalRewardAmount += reward.getAmount();
			//fireEvent(EVENT_REWARDS, Double.toString(reward.getAmount()));
            }
			fireEvent(EVENT_REWARDS);
        }
    };
	
	private void fireEvent(final String event) {
        final CordovaWebView view = this.webView;
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.loadUrl("javascript:cordova.fireWindowEvent('" + event + "');");
				
            }
        });
    }
	
	 OnAdEventV2 onAdEventListener = new OnAdEventV2() {
		@Override
			public void onEvent(AdEvent event, AdInfo adInfo, String message) {
				webView.loadUrl("javascript:console.log('onAdEventListener');");
				System.out.println("Placement: "+adInfo.getPlacement());
				switch (event) {
				case ALREADY_FETCHED:
				Log.d(TAG, "ALREADY_FETCHED");
					// fetchAd() is called with an Ad Name and there is already a fetched ad with the same name ready to be shown.
					fireEvent(EVENT_ALREADY_FETCHED);
					break;
				case ALREADY_SHOWN:
				Log.d(TAG, "ALREADY_SHOWN");
				fireEvent(EVENT_ALREADY_SHOWN);
					// showAd() is called with an Ad Name and there is an ad already being shown with the same name at this moment.
					break;
				case BEFORE_DISPLAY:
				Log.d(TAG, "BEFORE_DISPLAY");
				fireEvent(EVENT_BEFORE_DISPLAY);
					// Just before the Ad is displayed on the screen.
					break;
				case DISMISSED:
				Log.d(TAG, "DISMISSED");
				fireEvent(EVENT_DISMISSED);
					// The ad is dismissed by the user or by the application.
					break;
				case DISPLAYED:
				Log.d(TAG, "DISPLAYED");
				webView.loadUrl("javascript:console.log('DISPLAYED');");
				fireEvent(EVENT_DISPLAYED);
					// The ad is shown on the screen. For fetched ads this event will fire when the showAd() method is called.
					break;
				case DOWNLOADING:
				Log.d(TAG, "DOWNLOADING");
				fireEvent(EVENT_DOWNLOADING);
					// fetchAd() is called with an Ad Name and there is an ad already being fetched with the same name at this moment.
					break;
				case ERROR:
				Log.d(TAG, "ERROR");
				webView.loadUrl("javascript:console.log('" + message + "');");
				webView.loadUrl("javascript:console.log('ERROR');");
				fireEvent(EVENT_ERROR);
					// An error has occurred and the ad is going to be closed.
					// More information about the error is passed in the "message" parameter.
					break;
				case EXPIRED:
				Log.d(TAG, "EXPIRED");
				fireEvent(EVENT_EXPIRED);
					// A fetched ad expires. All fetched ads will expire after a certain time period if not shown.
					break;
				case FETCHED:
				Log.d(TAG, "FETCHED");
				webView.loadUrl("javascript:console.log('FETCHED');");
				fireEvent(EVENT_FETCHED);
					// The ad is ready to be shown. For fetched ads this method means that the ad is fetched successfully.
					// You may want to initially put the showReadyAd() call here when you're doing your initial testing, but for production you should move it to a more appropriate place, as described in the Show an Ad section.
					System.out.println("Placement: "+adInfo.getPlacement());
					break;
				case NO_AD:
				Log.d(TAG, "NO_AD");
				fireEvent(EVENT_NO_AD);
					// The device contacts the server, but there is no ad ready to be shown at this time.
					break;
				case USER_NAVIGATES_OUT_OF_APP:
				Log.d(TAG, "USER_NAVIGATES_OUT_OF_APP");
				fireEvent(EVENT_USER_NAVIGATES_OUT_OF_APP);
					// The user clicks on a link or a button in the ad and is going to navigate out of the app
					// to the Google Play or a browser applications.
					break;
				case VIDEO_COMPLETED:
				Log.d(TAG, "VIDEO_COMPLETED");
				fireEvent(EVENT_VIDEO_COMPLETED);
				   // Video has completed playing; rewards will be rewarded if applicable
				   break;
				default:
				Log.d(TAG, "default");
				fireEvent(EVENT_DEFAULT);
					// Others do not apply to Interstitial ads.
					break;
				}
			}
		};
	
}