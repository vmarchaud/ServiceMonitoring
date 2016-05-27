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

### License
 ```
  MIT License
 
  Copyright (c) 2016 Valentin 'ThisIsMac' Marchaud
 
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
 
  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.
 
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
  ```
