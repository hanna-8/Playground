## Summary

A basic .NET Core project written in C#. Acts like a REST service, persists data in a DynamoDB instance.

## Users

Basic:
  * Define a personal list of abilities and certifications.
  * Filter all abilities by specific tags.

Priviledged:
  * List employees and corresponding skills and certifications.
  * Filter employees by specific tags.

Admin:
  * Add ability (with corresponding tags).
  * "Contact" person for new abilities / tags.


## First draft: tagged abilities and employees

Use google.bookmarks or emag - like tags.
+ search functionality.

Ability tags:
  * Job Title:
    * Developer;
    * Senior Tester;
    * DevOps?;
  * Programming language:
    * Java;
    * C#;
  * Certification:
  * Language:
    * English...

Employee tags:
    * Discipline;
  * BU;
  * Current assignment;
  * All skills.

### App resources

* Employees;
* Abilities: certifications & skills.

* All abilities (+ filters);
<!-- * One ability + details (level / experience / last used); -->
* Employee abilities basket;
<!-- * One basket ability + editable details (level / experience / last used); -->
* All employees (+ filters): *priviledged only*;
<!-- * New ability *admin only*. -->


### Resources URI templates

* `/abilities?s=tag:cloud+web+app`
* `/profile`
* `/employees?s=tag:dev+tag:java`


### Rouserces mapping
HTTP methods supported by each resource.

| Method | URI template | Equivalent Operation |
|--------|--------------|----------------------|
| GET    | `/abilities?s=web+tag:aws+tag:db` | View all abilities |
| POST   | `/abilities` | Add new ability |
| POST   | `/abilities/abilityName` | Add ability to profile |
| GET    | `/profile` | View profile |
| POST   | `/profile` | Update profile |
| GET    | `/employees?s=ioana+tag:java` | Search by string or tag |


### Security considerations

* Authenticate users.
* Authorize access to resources / methods.

HMAC?


### Resource Representations


#### Employees

```
{
  'id' : 'djitarasu',
  'job_title' : 'Senior Developer',
  'name' : 'Dan Jitarasu',
  'discipline' : 'Development',
  'BU' : 'ISD',
  'projects' : [ { 'Nets': 100 } ],
  'abilities' : [ 
    {
      'name' : 'Java server & data',
      'level' : 'Expert',
      'experience': 8,
      'last_used': 'present'
    }, 
    {
      'name' : 'Oracle Certified Expert, Java EE 5 Web Services Developer (1Z0-862)',
      'year_rewarded': 2014
    }
  ]
}

```

#### Abilities

```
{
  'name' = 'Java',
  'tags' = [ 'programming-language' ],
  'employees' = [ 'djitarasu', 'amantale' ]
}
```

#### Tags

```
{
  'name' = 'Certification'
  'abilities' = [
    'Oracle Certified Expert, Java EE 5 Web Services Developer (1Z0-862)',
    'Certified Information Systems Security Professional ((ISC)2-01)'
  ]
}
```


### Curl commands

**GET**  
curl http://localhost:5000/api/abilities  
curl http://localhost:5000/api/abilities?query=Cert

**POST**  
Add new:  
curl -X POST -H "Content-Type: application/json" -d "{ \"Name\": \"fsharp\", \"tags\":[\"Programming Language\"],\"employees\":[]}" http://localhost:5000/api/abilities

Update:  
curl -X POST -f 'level=Expert' -f 'lastUsed=present' -f 'experience=8' -f 'employee=djitarasu' http://localhost:5000/api/abilities/fsharp

**DELETE**  
curl -X DELETE http://localhost:5000/api/abilities/fsharp


