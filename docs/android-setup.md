# Android Setup

###1. Add the library to your project

####Method 1

* Add rn-paypal as an npm dependency to your project.
* Use ```rnpm link``` to automatically link the library.

####Method 2

* Add rn-paypal as an npm dependency to your project.
* In android/settings.gradle, add the foloowing:

```
include ':rn-paypal'
project(':rn-paypal').projectDir = new File(rootProject.projectDir, '../node_modules/rn-paypal/android')
```

* In android/app/build.gradle, add the following:

```
dependencies {
	//...
    compile project(':rn-paypal')
	//...
}
```

###2. Add the module to your activity

* For react native versions <= 0.28, add the following to your MainActivity.java. For react native versions >= 0.29, add the following to your MainApplication.java

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