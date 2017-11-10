import boto3

dynamodb = boto3.resource('dynamodb')
dynamodb_client = boto3.client('dynamodb')


print("{")

response = dynamodb_client.list_tables()
print("'Tables':", response)

table = dynamodb.Table('ability')
response = table.scan()
print(",\n'Abilities':", response)

table = dynamodb.Table('employee')
response = table.scan()
print(",\n'Employees':\n", response)

table = dynamodb.Table('tag')
response = table.scan()
print(",\n'Tags':\n", response)
print("}")

