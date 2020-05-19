import boto3

#create a client that allows to interact with the DynamoDB API
dynamo_client = boto3.client('dynamodb')

def get_items():
	return dynamo_client.scan(
		TableName = 'parking_spot'
	)