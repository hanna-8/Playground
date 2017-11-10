### Small "serverless" web app created with AWS 

* Add / remove / update / list entries from a table stored in Amazon DynamoDB through a web interface;
* Store static content in Amazon S3;
* Access dynamic content through Amazon API Gateway -> Amazon Lambda.

#### Used tutorials
* [Building a Serverless Website with AWS](http://blog.nyl.io/hosting-a-serverless-website-with-aws/).
* [Build a Serverless Web Application](https://aws.amazon.com/getting-started/serverless-web-app/) - AWS official doc. Very cool examples :D.

#### Lessons learned

* Had *A LOT* of trouble because of CORS! 
  * Careful to set the following headers both programatically, when building the response, and in the API Gateway resource.  
    `'Access-Control-Allow-Origin': '*'` and  
    `'Access-Control-Allow-Methods': 'GET,POST,PUT,DELETE,OPTIONS'`
* Good to test changes in this order:
  * Lambda;
  * API Gateway;
  * Whole app.

* Very very useful the Network tab in the Chrome Inspect page view.

#### Files found here

* HTML code for the static page;
* Javascript code for the Lambda function.

#### TODO

* Use CloudFront in front of S3;
* Try out Route53 if included in the free tier;
* Authentication?

