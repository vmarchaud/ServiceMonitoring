## ServiceMonitoring

This software aim at people who want to simply monitor any service that they want and receive alerts via pushbullet if they want.
It also have an simple XML configuration, you can customize near everything, like everyday maintenance downtime or the pushbullet you want the alert on.

### Build

- ```git clone``` anywhere you want
- execute ```mvn install```
- get the jar in target/ and run it !

### Use it

You must setup your pushbullet api key :

- Go to ```https://www.pushbullet.com/#settings/account```
- Navigate to ``` Access Tokens ```
- Click ``` Create Access Token ```

And after you only need to configure your service that the software will monitor, run it anywhere and enjoy !
