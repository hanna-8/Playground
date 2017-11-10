import boto3

dynamodb_client = boto3.client('dynamodb')


# Remove table
dynamodb_client.delete_table(TableName='employee')
dynamodb_client.get_waiter('table_not_exists').wait(TableName = 'employee')

dynamodb_client.delete_table(TableName='ability')
dynamodb_client.get_waiter('table_not_exists').wait(TableName = 'ability')

dynamodb_client.delete_table(TableName='employee_ability')
dynamodb_client.get_waiter('table_not_exists').wait(TableName = 'employee_ability')

