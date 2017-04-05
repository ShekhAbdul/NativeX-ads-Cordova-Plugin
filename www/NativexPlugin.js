function NativexPlugin(appKey, userId) {
	
	this.init = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'NativexPlugin', 'init', [appKey, userId]);
		console.log("NativexPlugin.js: init");
	};
	this.onRedeem = function(placementName, claimSpace, successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'NativexPlugin', 'onRedeem', []);
		console.log("NativexPlugin.js: onRedeem");
	};

	this.fetchAd = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'NativexPlugin', 'fetchAd', []);
		console.log("NativexPlugin.js: fetchAd");
	};

	this.showReadyAd = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'NativexPlugin', 'showReadyAd', []);
		console.log("NativexPlugin.js: showReadyAd");
	};
	this.isAdReady = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'NativexPlugin', 'isAdReady', []);
		console.log("NativexPlugin.js: isAdReady");
	};
	this.fetchAdVideo = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'NativexPlugin', 'fetchAdVideo', []);
		console.log("NativexPlugin.js: fetchAd");
	};

	this.showReadyAdVideo = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'NativexPlugin', 'showReadyAdVideo', []);
		console.log("NativexPlugin.js: showReadyAd");
	};
}

if(typeof module !== undefined && module.exports) {

	module.exports = NativexPlugin;
}