#import "Paypal.h"

@implementation Paypal

NSString * const ERROR_USER_CANCELED = @"ERROR_USER_CANCELED";

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(configure: (NSDictionary *)params) {
	//Run this as a background process
	dispatch_async(dispatch_get_main_queue(), ^{
		self.configuration = [[PayPalConfiguration alloc] init];

		NSString *environment = params[@"environment"];
		NSString *clientId = params[@"clientId"];
		NSString *merchantName = params[@"merchantName"];
		bool acceptCreditCards = (bool) params[@"acceptCreditCards"];

		[PayPalMobile initializeWithClientIdsForEnvironments:@{environment: clientId}];
		[PayPalMobile preconnectWithEnvironment:environment];

		[self.configuration setMerchantName:merchantName];
		[self.configuration setAcceptCreditCards:acceptCreditCards];

		if([params objectForKey:@"defaultUserEmail"]) {
				NSString *defaultUserEmail = params[@"defaultUserEmail"];
				[self.configuration setDefaultUserEmail:defaultUserEmail];
		}

		if([params objectForKey:@"defaultUserPhone"]) {
				NSString *defaultUserPhone = params[@"defaultUserPhone"];
				[self.configuration setDefaultUserPhoneNumber:defaultUserPhone];
		}

		if([params objectForKey:@"defaultUserPhoneCountryCode"]) {
				NSString *defaultUserPhoneCountryCode = params[@"defaultUserPhoneCountryCode"];
				[self.configuration setDefaultUserPhoneCountryCode:defaultUserPhoneCountryCode];
		}

	});
}

- (PayPalPayment *) createPayment:(NSDictionary *)params {
	PayPalPayment *payment = [[PayPalPayment alloc] init];

	NSString *amount = params[@"amount"];
	NSString *currency = params[@"currency"];
	NSString *description = params[@"description"];

	payment.amount = [[NSDecimalNumber alloc] initWithString:amount];
	payment.currencyCode = currency;
	payment.shortDescription = description;

	if([params objectForKey:@"bnCode"]) {
		NSString *bnCode = params[@"bnCode"];
		payment.bnCode = bnCode;
	}

	if([params objectForKey:@"custom"]) {
		NSString *custom = params[@"custom"];
		payment.custom = custom;
	}

	if([params objectForKey:@"invoiceNumber"]) {
		NSString *invoiceNumber = params[@"invoiceNumber"];
		payment.invoiceNumber = invoiceNumber;
	}

	if([params objectForKey:@"softDescriptor"]) {
		NSString *softDescriptor = params[@"softDescriptor"];
		payment.softDescriptor = softDescriptor;
	}

	return payment;
}

RCT_EXPORT_METHOD(isProcessable: (NSDictionary *)params withCallback: (RCTResponseSenderBlock)callback) {
	PayPalPayment *payment = [self createPayment:params];

	callback(@[@(payment.processable)]);
}

RCT_EXPORT_METHOD(singlePayment: (NSDictionary *)params withSuccess: (RCTResponseSenderBlock)successCallback withFailure: (RCTResponseSenderBlock)failureCallback) {
	PayPalPayment *payment = [self createPayment:params];
	self.successCallback = successCallback;
	self.failureCallback = failureCallback;

	PayPalPaymentViewController *paymentVc = [[PayPalPaymentViewController alloc] initWithPayment:payment configuration:self.configuration delegate:self];
	UIViewController *currentVc = [[[UIApplication sharedApplication] keyWindow] rootViewController];

	do {
		if([currentVc isKindOfClass:[UINavigationController class]])
			currentVc = [(UINavigationController *) currentVc visibleViewController];
		else if (currentVc.presentedViewController)
			currentVc = currentVc.presentedViewController;
	} while (currentVc.presentedViewController);

	dispatch_async(dispatch_get_main_queue(), ^{
		[currentVc presentViewController:paymentVc animated:YES completion:nil];
	});
}

RCT_EXPORT_METHOD(logout) {
	[PayPalMobile clearAllUserData];
}

#pragma mark delegates

- (void)payPalPaymentViewController:(PayPalPaymentViewController *)paymentViewController didCompletePayment:(PayPalPayment *)completedPayment {
	NSLog(@"Paypal payment success");
	[paymentViewController.presentingViewController dismissViewControllerAnimated:YES completion:^{
		if(self.successCallback) {
			self.successCallback(@[completedPayment.confirmation]);
		}
	}];
}

- (void)payPalPaymentDidCancel:(PayPalPaymentViewController *)paymentViewController {
	NSLog(@"Paypal payment cancelled");
	[paymentViewController.presentingViewController dismissViewControllerAnimated:YES completion:^{
		if(self.failureCallback) {
			self.failureCallback(@[ERROR_USER_CANCELED]);
		}
	}];
}

#pragma mark constants

- (NSDictionary *)constantsToExport {
	return @{
		@"SANDBOX": PayPalEnvironmentSandbox,
		@"NO_NETWORK": PayPalEnvironmentNoNetwork,
		@"PRODUCTION": PayPalEnvironmentProduction,
		@"ERROR_USER_CANCELED": ERROR_USER_CANCELED
	};
}

@end
