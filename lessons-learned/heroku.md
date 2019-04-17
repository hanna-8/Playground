### Most basic commands to deploy service to heroku

`git init`  
`git add [stuff]`  
`git commit`  
`heroku login` - not actually sure this is needed  
`heroku create [name-of-app]`  
`heroku git:remote -a [name-of-app]`  
`git push heroku master`  
`heroku open` - or simply go to the app url

### Environment variables

`heroku config` - lists all env variables  
`heroku config:set NAME_OF_VAR=value_of_var`

### Logs

`heroku logs [-a AppName] --tail`

### Don't forget to clean things up

`heroku apps:destroy --app=[name-of-app]`
