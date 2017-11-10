import boto3

dynamodb = boto3.resource('dynamodb')
dynamodb_client = boto3.client('dynamodb')



# Add table entries: employees
table = dynamodb.Table('employee')
table.put_item(
    Item = {
        'id': 'noname',
        'job_title': 'Senior Developer',
        'name': 'Mr. Nobody',
        'discipline': 'Development',
        'BU': 'ISD',
        'projects': [ { 'Nets': 100 } ],
        'abilities': [ 
            {
                'name': 'Java',
                'level': 'Expert',
                'experience': 8,
                'last_used': 'present'
            }, 
            {
                'name': 'Oracle Certified Expert, Java EE 5 Web Services Developer (1Z0-862)',
                'year_rewarded': 2014
            }
        ]
    }
)

# Add table entries: abilities
table = dynamodb.Table('ability')
table.put_item(
    Item = {
        'name': 'Java',
        'tags': [ 'programming-language' ],
        'employees': [ 'noname' ]
    }
)
table.put_item(
    Item = {
        'name': 'Oracle Certified Expert, Java EE 5 Web Services Developer (1Z0-862)',
        'tags': [ 'Certification' ],
        'employees': [ 'noname' ]
    }
)

# Add table entries: tags
table = dynamodb.Table('tag')
table.put_item(
    Item = {
        'name': 'programming_language',
        'abilities': [ 'Java', 'C++' ]
    }
)
table.put_item(
    Item = {
        'name': 'certification',
        'abilities': [
            'Oracle Certified Expert, Java EE 5 Web Services Developer (1Z0-862)',
            'Certified Information Systems Security Professional ((ISC)2-01)'
        ]
    }
)


