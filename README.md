# Paypal for React Native

## Setup:

1. [Android setup](/docs/android-setup.md)
2. [iOS setup](/docs/ios-setup.md)
3. import Paypal from "react-native-paypal";
 
##### This module supports Paypal versions 2.15.3 for android and 2.16.3 for iOS.

## Methods:

1. configure(&lt;params object&gt;)

	Configure Paypal for single payment. Call this only once in your application.

	<table>
		<tr>
			<th colspan=4>&lt;params object&gt;</th>
		</tr>
		<tr>
			<th>Name</th>
			<th>Type</th>
			<th>Optional</th>
			<th>Description</th>
		</tr>
		<tr>
			<td>environment</td>
			<td>String</td>
			<td></td>
			<td>Paypal.SANDBOX<br/>Paypal.NO_NETWORK<br/>Paypal.PRODUCTION</td>
		</tr>
		<tr>
			<td>clientId</td>
			<td>String</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>merchantName</td>
			<td>String</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>acceptCreditCards</td>
			<td>boolean</td>
			<td>Yes</td>
			<td>Indicate whether credit card support should be enabled. Defaults to true</td>
		</tr>
		<tr>
			<td>defaultUserEmail</td>
			<td>String</td>
			<td>Yes</td>
			<td></td>
		</tr>
		<tr>
			<td>defaultUserPhone</td>
			<td>String</td>
			<td>Yes</td>
			<td></td>
		</tr>
		<tr>
			<td>defaultUserPhoneCountryCode</td>
			<td>String</td>
			<td>Yes</td>
			<td></td>
		</tr>
		<tr>
			<td>rememberUser</td>
			<td>boolean</td>
			<td>Yes</td>
			<td>Defaults to true</td>
		</tr>
	</table>

2. isProcessable(&lt;params object&gt;, &lt;callback&gt;)

	Checks if the payment object can be processed. The callback returns false for cases such as negative amount, currency string not recognized etc.  

	<table>
		<tr>
			<th colspan=4>&lt;params object&gt;</th>
		</tr>
		<tr>
			<th>Name</th>
			<th>Type</th>
			<th>Optional</th>
			<th>Description</th>
		</tr>
		<tr>
			<td>amount</td>
			<td>Number</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>currency</td>
			<td>String</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>description</td>
			<td>String</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>bnCode</td>
			<td>String</td>
			<td>Yes</td>
			<td>Build Notation Code</td>
		</tr>
		<tr>
			<td>custom</td>
			<td>String</td>
			<td>Yes</td>
			<td></td>
		</tr>
		<tr>
			<td>invoiceNumber</td>
			<td>String</td>
			<td>Yes</td>
			<td></td>
		</tr>
		<tr>
			<td>softDescriptor</td>
			<td>String</td>
			<td>Yes</td>
			<td></td>
		</tr>
		<tr>
			<td>shippingAddress</td>
			<td>Object</td>
			<td>Yes</td>
			<td></td>
		</tr>
		<tr>
			<td>paymentDetails</td>
			<td>Object</td>
			<td>Yes</td>
			<td></td>
		</tr>
	</table>
	
	
	<table>
		<tr>
			<th colspan=4>&lt;shippingAddress object&gt;</th>
		</tr>
		<tr>
			<th>Name</th>
			<th>Type</th>
			<th>Optional</th>
			<th>Description</th>
		</tr>
			<tr>
			<td>recipientName</td>
			<td>String</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>line1</td>
			<td>String</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>line2</td>
			<td>String</td>
			<td>Yes</td>
			<td></td>
		</tr>
		<tr>
			<td>postalCode</td>
			<td>String</td>
			<td>Yes</td>
			<td></td>
		</tr>
		<tr>
			<td>city</td>
			<td>String</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>state</td>
			<td>String</td>
			<td>Yes</td>
			<td></td>
		</tr>
			<tr>
			<td>countryCode</td>
			<td>String</td>
			<td>Yes</td>
			<td></td>
		</tr>
	</table>
	
	<table>
		<tr>
			<th colspan=4>&lt;paymentDetails object&gt;</th>
		</tr>
		<tr>
			<th>Name</th>
			<th>Type</th>
			<th>Optional</th>
			<th>Description</th>
		</tr>
			<tr>
			<td>shipping</td>
			<td>Number</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>subtotal</td>
			<td>Number</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>tax</td>
			<td>Number</td>
			<td></td>
			<td></td>
		</tr>
	</table>

3. singlePayment(&lt;params object&gt;, &lt;success callback&gt;, &lt;failure callback&gt;)

	Process a single payment using the sdk. The success callback will return a confirmation object. Sample confirmation object and steps to verify payment can be found [here](https://developer.paypal.com/docs/integration/mobile/verify-mobile-payment/). The failure callback can have one of 2 error codes, Paypal.ERROR_USER_CANCELED or Paypal.ERROR_INVALID_CONFIG (only for android).

	&lt;params object&gt; is the same as defined for isProcessable.

4. logout()

	Deletes all remembered user data (credit cards, paypal account, email, phone). This can be used while logging out a user in the app.
