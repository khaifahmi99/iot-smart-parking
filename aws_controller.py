import boto3
from boto3.dynamodb.conditions import Key, Attr

dynamodb = boto3.resource('dynamodb', region_name='ap-southeast-2')


def get_spots_info():

	#call the table
	table = dynamodb.Table('parking_spot') 
	response = table.scan()

	return response['Items']

def validate_slots(data):
	#table = dynamodb.Table('parking_spot') 
	#response = table.scan(
	#	FilterExpression=Attr('parking_status').eq('Available') & Attr('parking_spot_id').IN("+Object.keys(data).toString()+ ")", ExpressionAttributeValues : titleObject
    #)
	return data

def validate_booking(data):
	name = data.get('full_name')
	phonenum = data.get('phonenum')
	email = data.get('emailAdd')
	license = data.get('license')
	plateNum = data.get('plateNum')
	ts = data.get('ts')

	if  name == '':
		return 0
	elif phonenum == '':
		return 0
	elif email == '':
		return 0
	elif license == '':
		return 0
	elif plateNum == '':
		return 0
	elif ts == '':
		return 0

	return data

def save_booking(data):
	rate = 10
	ts = data.get('ts')
	price = int(ts) * rate
	setattr(data, 'price', price)
	return data