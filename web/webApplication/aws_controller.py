import boto3
from boto3.dynamodb.conditions import Key, Attr
from random import randrange
import datetime
from datetime import datetime

dynamodb = boto3.resource('dynamodb', region_name='ap-southeast-2')


def get_spots_info():
	#call the table
	table = dynamodb.Table('parking_spot') 
	response = table.scan()

	return response['Items']

def validate_slots(data):
	
	return data

def validate_booking(data):
	name = data.get('full_name')
	phonenum = data.get('phonenum')
	email = data.get('emailAdd')
	license = data.get('license')
	plateNum = data.get('plateNum')
	ts = data.get('time_booking')

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
	rate = 7
	name = data.get('full_name')
	phonenum = data.get('phonenum')
	plateNum = data.get('plateNum')
	license = data.get('license')
	time_booking = data.get('time_booking')

	payment_created = datetime.now()
	ts_payment = payment_created.strftime("%Y-%m-%d %H:%M:%S")
	book_created = datetime.now()
	ts_booking = book_created.strftime("%Y-%m-%d")
	payment_time = payment_created.strftime("%H")
	booking = time_booking.split(':')
	pay_hours = int(float(payment_time))
	book_hours = int(booking[0])
	price = (pay_hours - book_hours) * rate
	setattr(data, 'price', price)

	users_table = dynamodb.Table('users') 

	users_table.put_item(
		Item={
			'user_id' : int(license),
			'ts_payment' : ts_payment,
			'user_carplate_number' : plateNum,
			'user_fee' : price,
			'user_hasMadePayment' : True,
			'user_name' : name,
			'user_phone' : phonenum
		}
	)
	return data