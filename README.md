sms-searcher
============

A home-brewed replacement for GoogleSMS Search (R.I.P.)   -- of course we assume you have a spare smartfone and an underutilized text plan.


This Android app should run on any Gingerbread (2.3.3) or higher device. It registers a BroadcastReceiver to get incoming SMS messages, and hands those messages to a Service. The Service uses a GooglePlaces search utility to assemble a return SMS message, which the Service then dispatches to the original sender.
What good is that?  Why not just do a google search from your Internet-connected device?  Well, sometimes you don't have a smartphone, but you have a texting phone. Or sometimes your service is too spotty for TCP/IP, but just fine for a short message. Or perhaps you're roaming and the local ISP charges mondo $$ per kb on the data side. Or...

How do you use it? 
 1.  Get a Google Places API key:  https://code.google.com/apis/console
 2.  Stick that key in ResultsFetcher.java
 3.  Build the app.
 4.  Install it on your spare phone (you'll have to use USB debugging)
 5.  Connect your spare phone to power, and hook it to the WiFi at your home or office.
 6.  When you're out and about and need some establishment info, send a SMS message to your spare phone. It works well to put some kind of locator text in your message, like a zip code or city name. 
 7.  In a few moments, you should get a reply with the top three results from Google Places.
 
Is there input error checking? Not much.
Is there runtime catching? Not much. 
You have suggestions to improve it? We're all ears.

https://github.com/marinusd/sms-places-searcher/
