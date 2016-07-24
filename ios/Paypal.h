#import "PayPalMobile.h"
#import "RCTBridgeModule.h"

@interface Paypal : UIViewController <PayPalPaymentDelegate, RCTBridgeModule>

@property PayPalConfiguration *configuration;
@property (copy) RCTResponseSenderBlock successCallback;
@property (copy) RCTResponseSenderBlock failureCallback;

@end
