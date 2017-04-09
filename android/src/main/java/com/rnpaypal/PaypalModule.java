package com.rnpaypal;

import android.content.Intent;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.LifecycleEventListener;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;

import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

public class PaypalModule extends ReactContextBaseJavaModule implements ActivityEventListener, LifecycleEventListener {

	private static PayPalConfiguration config;
	private Activity activity;

	private int requestedCode;

	private Callback paymentSuccessCallback;
	private Callback paymentFailureCallback;

	private static final int REQUEST_CODE_SINGLE_PAYMENT = 1;

	private static final String ERROR_USER_CANCELED = "ERROR_USER_CANCELED";
	private static final String ERROR_INVALID_CONFIG = "ERROR_INVALID_CONFIG";

	public PaypalModule(ReactApplicationContext reactContext) {
		super(reactContext);

		reactContext.addActivityEventListener(this);
		reactContext.addLifecycleEventListener(this);
	}

	@Override
	public String getName() {
		return "Paypal";
	}

	@Override
	public Map<String, Object> getConstants() {
		final Map<String, Object> constants = new HashMap<>();

		constants.put("SANDBOX", PayPalConfiguration.ENVIRONMENT_SANDBOX);
		constants.put("NO_NETWORK", PayPalConfiguration.ENVIRONMENT_NO_NETWORK);
		constants.put("PRODUCTION", PayPalConfiguration.ENVIRONMENT_PRODUCTION);
		constants.put(ERROR_USER_CANCELED, ERROR_USER_CANCELED);
		constants.put(ERROR_INVALID_CONFIG, ERROR_INVALID_CONFIG);

		return constants;
	}

	@ReactMethod
	public void configure(final ReadableMap params) {
		activity = getCurrentActivity();

		final String environment = params.getString("environment");
		final String clientId = params.getString("clientId");
		final String merchantName = params.getString("merchantName");

		config = new PayPalConfiguration().environment(environment).clientId(clientId).merchantName(merchantName);

		if(params.hasKey("acceptCreditCards"))
			config.acceptCreditCards(params.getBoolean("acceptCreditCards"));

		if(params.hasKey("defaultUserEmail"))
			config.defaultUserEmail(params.getString("defaultUserEmail"));

		if(params.hasKey("defaultUserPhone"))
			config.defaultUserPhone(params.getString("defaultUserPhone"));

		if(params.hasKey("defaultUserPhoneCountryCode"))
			config.defaultUserPhoneCountryCode(params.getString("defaultUserPhoneCountryCode"));

		if(params.hasKey("rememberUser"))
			config.rememberUser(params.getBoolean("rememberUser"));

		Intent intent = new Intent(activity, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		activity.startService(intent);
	}

	private PayPalPaymentDetails createPaymentDetails(final ReadableMap params) {
		final BigDecimal subtotal = new BigDecimal(params.getDouble("subtotal"));
		final BigDecimal shipping = new BigDecimal(params.getDouble("shipping"));
		final BigDecimal tax = new BigDecimal(params.getDouble("tax"));

		PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
		return(paymentDetails);
	}

	private ShippingAddress createShippingAddress(final ReadableMap params) {
		ShippingAddress shippingAddress = new ShippingAddress();

		shippingAddress.line1(params.getString("line1"));
		shippingAddress.city(params.getString("city"));
		shippingAddress.recipientName(params.getString("recipientName"));

		if(params.hasKey("line2"))
			shippingAddress.line2(params.getString("line2"));

		if(params.hasKey("postalCode"))
			shippingAddress.postalCode(params.getString("postalCode"));

		if(params.hasKey("state"))
			shippingAddress.postalCode(params.getString("state"));

		if(params.hasKey("countryCode"))
			shippingAddress.countryCode(params.getString("countryCode"));

		return(shippingAddress);
	}

	private PayPalPayment createPayment(final ReadableMap params) {
		return(this.createPayment(params, true, true));
	}
	
	private PayPalPayment createPayment(final ReadableMap params, final boolean addShippingAddress, final boolean addPaymentDetails) {
		final BigDecimal amount = new BigDecimal(params.getDouble("amount"));
		final String currency = params.getString("currency");
		final String description = params.getString("description");

		PayPalPayment payment = new PayPalPayment(amount, currency, description, PayPalPayment.PAYMENT_INTENT_SALE);

		if(params.hasKey("bnCode"))
			payment.bnCode(params.getString("bnCode"));

		if(params.hasKey("custom"))
			payment.custom(params.getString("custom"));

		if(params.hasKey("invoiceNumber"))
			payment.invoiceNumber(params.getString("invoiceNumber"));

		if(params.hasKey("softDescriptor"))
			payment.softDescriptor(params.getString("softDescriptor"));

		if(addShippingAddress && params.hasKey("shippingAddress"))
			payment.providedShippingAddress(this.createShippingAddress(params.getMap("shippingAddress")));

		if(addPaymentDetails && params.hasKey("paymentDetails"))
			payment.paymentDetails(this.createPaymentDetails(params.getMap("paymentDetails")));

		return payment;
	}

	@ReactMethod
	public void isProcessable(final ReadableMap params, final Callback callback) {
		PayPalPayment payment = createPayment(params, false, false);
		boolean result = payment.isProcessable();

		if(params.hasKey("shippingAddress")) {
			ShippingAddress shippingAddress = this.createShippingAddress(params.getMap("shippingAddress"));
			result = result && shippingAddress.isProcessable();
		}

		if(params.hasKey("paymentDetails")) {
			PayPalPaymentDetails paymentDetails = this.createPaymentDetails(params.getMap("paymentDetails"));
			result = result && paymentDetails.isProcessable();
		}
		
		callback.invoke(result);
	}

	@ReactMethod
	public void singlePayment(final ReadableMap params, final Callback successCallback, final Callback failureCallback) {
		this.paymentSuccessCallback = successCallback;
		this.paymentFailureCallback = failureCallback;

		this.requestedCode = REQUEST_CODE_SINGLE_PAYMENT;

		Intent intent = new Intent(activity, PaymentActivity.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, createPayment(params));

		activity.startActivityForResult(intent, this.requestedCode);
	}

	@ReactMethod
	public void logout() {
		PayPalService.clearAllUserData(getReactApplicationContext().getApplicationContext());
	}

	@Override
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		if(requestCode == this.requestedCode) {
			switch(resultCode) {
				case Activity.RESULT_OK: {
					PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

					if(confirm != null) {
						//invoke callback with confirmation and payment objects
						paymentSuccessCallback.invoke(ReactUtils.jsonObjectToWritableMap(confirm.toJSONObject()));
					}

					break;
				}
				case Activity.RESULT_CANCELED: {
					paymentFailureCallback.invoke(ERROR_USER_CANCELED);
					break;
				}
				case PaymentActivity.RESULT_EXTRAS_INVALID: {
					paymentFailureCallback.invoke(ERROR_INVALID_CONFIG);
					break;
				}
			}
		}
	}

	@Override
	public void onHostResume() {
		//Do nothing
	}

	@Override
	public void onHostPause() {
		//Do nothing
	}

	@Override
	public void onHostDestroy() {
		ActivityManager activityManager = (ActivityManager) getReactApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

		for(ActivityManager.RunningServiceInfo currentService: activityManager.getRunningServices(Integer.MAX_VALUE)) {
			if(currentService.service.getClassName().equals(PayPalService.class.getName())) {
				activity.stopService(new Intent(activity, PayPalService.class));
			}
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		//Do nothing
	}
}
