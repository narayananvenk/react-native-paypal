# Paypal for React Native

##Setup:

1. [Android setup](/docs/android-setup.md)
2. [iOS setup](/docs/ios-setup.md)
3. import Paypal from "react-native-paypal";
 
#####This module supports Paypal version 2.14.4 for both Android and iOS.

##Methods:

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
			<td>Paypal.SANDBOX, Paypal.NO_NETWORK, Paypal.PRODUCTION</td>
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
			<td></td>
			<td>Indicate whether credit card support should be enabled</td>
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
			<td>String</td>
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
	</table>

3. singlePayment(&lt;params object&gt;, &lt;success callback&gt;, &lt;failure callback&gt;)

	Process a single payment using the sdk. The success callback will return a confirmation object. Sample confirmation object and steps to verify payment can be found [here](https://developer.paypal.com/docs/integration/mobile/verify-mobile-payment/). The failure callback can have one of 2 error codes, Paypal.ERROR_USER_CANCELED or Paypal.ERROR_INVALID_CONFIG (only for android).

	&lt;params object&gt; is the same as defined for isProcessable.

4. logout()

	Deletes all remembered user data (credit cards, paypal account, email, phone). This can be used while logging out a user in the app.
