import boto3

dynamodb = boto3.resource('dynamodb')
dynamodb_client = boto3.client('dynamodb')

# Create table 'employee'
try:
    table = dynamodb_client.create_table(
        TableName = 'employee',
        KeySchema = [
            {
                'AttributeName': 'id',
                'KeyType': 'HASH'
            },
            {
                'AttributeName': 'job_title',
                'KeyType': 'RANGE'
            }
        ],
        AttributeDefinitions = [
            {
                'AttributeName': 'id',
                'AttributeType': 'S'
            },
            {
                'AttributeName': 'job_title',
                'AttributeType': 'S'
            }
        ],
        ProvisionedThroughput = {
            'ReadCapacityUnits': 5,
            'WriteCapacityUnits': 5
        }
    )
    
    # Wait until the table exists.
    dynamodb_client.get_waiter('table_exists').wait(TableName = 'employee')
    print("Table 'employee' created.")

except dynamodb_client.exceptions.ResourceInUseException:
    print("Table 'employee' already exists.")
    pass


# Create table 'ability'
try:
    table = dynamodb_client.create_table(
        TableName = 'ability',
        KeySchema = [
            {
                'AttributeName': 'name',
                'KeyType': 'HASH'
            }
        ],
        AttributeDefinitions = [
            {
                'AttributeName': 'name',
                'AttributeType': 'S'
            }
        ],
        ProvisionedThroughput = {
            'ReadCapacityUnits': 5,
            'WriteCapacityUnits': 5
        }
    )
    
    # Wait until the table exists.
    dynamodb_client.get_waiter('table_exists').wait(TableName = 'ability')
    print("Table 'ability' created.")

except dynamodb_client.exceptions.ResourceInUseException:
    print("Table 'ability' already exists.")
    pass


# Create table 'tag'
try:
    table = dynamodb_client.create_table(
        TableName = 'tag',
        KeySchema = [
            {
                'AttributeName': 'name',
                'KeyType': 'HASH'
            }
        ],
        AttributeDefinitions = [
            {
                'AttributeName': 'name',
                'AttributeType': 'S'
            }
        ],
        ProvisionedThroughput = {
            'ReadCapacityUnits': 5,
            'WriteCapacityUnits': 5
        }
    )
    
    # Wait until the table exists.
    dynamodb_client.get_waiter('table_exists').wait(TableName = 'tag')
    print("Table 'tag' created.")

except dynamodb_client.exceptions.ResourceInUseException:
    print("Table 'tag' already exists.")
    pass



