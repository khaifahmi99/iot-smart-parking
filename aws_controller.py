import boto3
from boto3.dynamodb.conditions import Key, Attr
from random import randrange
import datetime
from datetime import datetime
import uuid
import time

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
	booking_time = data.get('booking_time')
	exit_time = data.get('exit_time')

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
	elif booking_time == '':
		return 0
	elif exit_time == '':
		return 0

	return data

def save_booking(data):
	rate = 7
	n = 10
	name = data.get('full_name')
	phonenum = data.get('phonenum')
	plateNum = data.get('plateNum')
	license = data.get('license')
	booking_time = data.get('booking_time')
	exit_time = data.get('exit_time')
	UUID = uuid.uuid1()
	user_id = str(UUID.int)
	p_user_id = user_id[:n-6]

	payment_created = datetime.now()
	ts_payment = payment_created.strftime("%Y-%m-%d %H:%M:%S")
	book_created = datetime.now()
	exit_created = datetime.now()
	ts_booking = book_created.strftime("%Y-%m-%d")
	booking = booking_time.split(':')
	exit = exit_time.split(':')
	book_data = booking[0] + "." + booking[1]
	exit_data = exit[0] + "." + exit[1]
	book_hours = round(float(book_data))
	exit_hours = round(float(exit_data))
	price = str((exit_hours - book_hours) * rate)
	fee = price.strip("-")
	setattr(data, 'price', fee)

	users_table = dynamodb.Table('users')

	users_table.put_item(
		Item={
			'user_id' : int(p_user_id),
			'ts_payment' : ts_payment,
			'user_carplate_number' : plateNum,
			'user_fee' : int(fee),
			'user_hasMadePayment' : True,
			'user_name' : name,
			'user_phone' : phonenum
		}
	)

	parking_spot_table = dynamodb.Table('parking_spot') 

	parking_spot_table.update_item(
		Key={
			'parking_spot_id' : data.get('slot')
		},
		UpdateExpression = "set p_user.ts_payment = :a, p_user.user_carplate_number = :b, p_user.user_hasMadePayment = :c, p_user.user_id = :d, p_user.user_name = :e, p_user.user_phone = :f, isBooked = :g, ts_booking = :h", 
		ExpressionAttributeValues={
			':a' : "null",
			':b' : plateNum,
			':c' : False,
			':d' : int(p_user_id),
			':e' : name,
			':f' : phonenum,
			':g' : True,
			':h' : ts_booking + " " + booking_time
		}
	)

	return data


def validate_payment(data):
	spot_id = data.get('slot')

	if spot_id == '':
		return 0

	return data


def update_payment(data):
	payment_created = datetime.now()
	ts_payment = payment_created.strftime("%Y-%m-%d %H:%M:%S")

	parking_spot_table = dynamodb.Table('parking_spot') 

	parking_spot_table.update_item(
		Key={
			'parking_spot_id' : data.get('slot')
		},
		UpdateExpression = "set p_user.ts_payment = :a, p_user.user_hasMadePayment = :b", 
		ExpressionAttributeValues={
			':a' : ts_payment,
			':b' : True
		}
	)

	return data 