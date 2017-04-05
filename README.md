# README #

### What is this repository for? ###

* NativexPlugin for IONIC android app NativeX ads

### How do I get set up? ###

document.addEventListener('deviceready', function onDeviceReady() {
						
 var ANDROID_KEY = 'XXXXX';
 var IOS_KEY = 'XXXXX';
 var nxAds = new NativexPlugin(cordova.platformId === 'ios' ? IOS_KEY : ANDROID_KEY, user_id);
 						
 nxAds.init();
						
						
$scope.showNativeVideo =  function(){
	nxAds.fetchAdVideo();
	$ionicLoading.show({
	    template: 'Loading ...'
	}); 
  window.addEventListener("FETCHED", function() {
	$ionicLoading.hide();
	nxAds.showReadyAdVideo();
   }, false);
};
						
$scope.showIndicator = function(){
    nxAds.fetchAd();
   $ionicLoading.show({
	template: 'Loading ...'
   }); 
								
   window.addEventListener("FETCHED", function() {
	$ionicLoading.hide();
	nxAds.showReadyAd();
    }, false);
};
						
  window.addEventListener("ERROR", function() {
	$ionicLoading.hide();
	var alertPop = $ionicPopup.alert({
	    title: "Error!",
	    template: "Try Again !!!"
        });
							
  }, false);
  window.addEventListener("NO_AD", function() {
	console.log("NO_AD");
	$ionicLoading.hide();
	var alertPop = $ionicPopup.alert({
		title: "Error!",
		template: "No Ads Available, Try Again !!!"
	});
  }, false);
						
  window.addEventListener("EXPIRED", function() {
	console.log("EXPIRED");
	$ionicLoading.hide();
	var alertPop = $ionicPopup.alert({
		title: "Error!",
		template: "This Ad Expired, Try Again !!!"
	});	
 }, false);
 window.addEventListener("DISMISSED", function() {
	console.log("DISMISSED");

 }, false);
						
 window.addEventListener("VIDEO_COMPLETED", function() {
	console.log("VIDEO_COMPLETED");
	var alertPop = $ionicPopup.alert({
	    title: "Success",
	    template: "Video Completed..."
        });		
 }, false);
 window.addEventListener("rewards", function() {
	console.log("rewards");
	var alertPop = $ionicPopup.alert({
		title: "Success",
		template: "App Installed Reward will update"
	});		
 }, false);
 window.addEventListener("default", function() {
	console.log("default");
 }, false);
}, false);