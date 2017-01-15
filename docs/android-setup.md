# Android Setup

###1. Add the library to your project

####Method 1

* Add `react-native-paypal` as an npm dependency to your project.

####Method 2

* Add `react-native-paypal` as an npm dependency to your project.
* In android/settings.gradle, add the foloowing:

```
include ':react-native-paypal'
project(':react-native-paypal').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-paypal/android')
```

* In android/app/build.gradle, add the following:

```
dependencies {
	//...
    compile project(':react-native-paypal')
	//...
}
```

###2. Add the module to your activity

* Add the following to your MainApplication.java

```
import com.rnpaypal.PaypalPackage;

@Override
protected List<ReactPackage> getPackages() {
	//...
	return Arrays.<ReactPackage>asList(
		//...
		new PaypalPackage()
		//...
	);
	//...
}
```
