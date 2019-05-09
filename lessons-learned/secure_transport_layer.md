## How to secure the transport layer
*Note*: the example code is using nodejs with express.

### 1. Redirect all `http` to `https`:

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

### 2. 
