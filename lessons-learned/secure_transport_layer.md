## How to secure the transport layer

**Issue we want to solve**: prevent Man-In-The-Middle attacks.  
**How we plan to solve it**: encrypt messages as they are sent to/from our service(s).

*Note*: the example code is using node.js with express.

### Step 1. Add support for https to our service(s)
This translates to 'generate and use a cer

1. Use a self-signed certificate:
e.g. openssl
Pros: fast, nothing to install, useful for testing / development purposes.
Cons: http://answers.ssl.com/2899/can-i-create-my-own-ssl-certificate
2. Use a free Certificate Authority (e.g. Let's Encrypt)
Pros: well... free.
Cons: has to be renewed every 3 months. However, "renewal is as easy as running one simple command, which we can assign to a cron"
(https://www.sitepoint.com/how-to-use-ssltls-with-node-js/)
3. Pay a trusted CA. 
Pros: no cons of the above :).
Cons: not free. However, not *extremely* costly either..

We'll take the second approach, so Let's Encrypt!

```
var https = require("https");
var fs = require("fs");

[...]


```

https://timonweb.com/posts/running-expressjs-server-over-https/

Notes: 
1. Heroku does that (wildcard certificate \*.heroku not recommended? https://stackoverflow.com/a/22751658/777833 )
2. Put the certificates in a secure place (e.g. ???)
3. 


### Step 1. Use https between services
* Modify frontend, backend (and all services, e.g. proxy) to call one another only via https.
* Modify the CORS options in the backend to only accept calls from the https frontend.

### Step 2. Redirect all `http` to `https`:

 * By using an already implemented middle layer: [express-force-https](https://www.npmjs.com/package/express-force-https):
   ```
   var secure = require("express-force-https");
   app.use(secure);
   ```

   * Pros: code is cleaner. Would count on the package to be careful to do updates.
   * Cons: actually didn't find a package recent enough or that seems to be kept up to date.

 * By manually implementing a middle layer:
   ```
   app.use(function(req, res, next) {
     // x-forwarded-proto because we are behind a load balancer (heroku, in our case)
     if (!req.secure && req.get("x-forwarded-proto") !== "https") {
       // request was via http, so redirect to https
       res.redirect("https://" + req.headers.host + req.url);
     } else {
       // request was via https, so do no special handling
       next();
     }
   });
   ```

### 3. Use hsts header ?

