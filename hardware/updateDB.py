import boto3
import json
import time
from datetime import datetime

table = boto3.resource('dynamodb').Table('parking_spot')
response = table.scan()

available = 0
occupied = 0
booked = 0
not_booked = 0

ts = datetime.now()
ts = ts.strftime("%d-%m-%Y %H:%M:%S")

parking_spots = response['Items']
for spot in parking_spots:
    if spot['parking_status'] == 'Available':
        available += 1
    elif spot['parking_status'] == 'Occupied':
        occupied += 1

    if spot['isBooked']:
       booked += 1
    else:
       not_booked += 1

parking_overview = {
    'ts': ts,
    'available': available,
    'occupied': occupied,
    'booked': booked,
    'not_booked': not_booked
}

table = boto3.resource('dynamodb').Table('parking_overview')
result = table.update_item(
    Key={
        'parking_area_id': 'level_1',
    },
    UpdateExpression="SET parking_status = list_append(parking_status, :s)",
    ExpressionAttributeValues={
        ':s': [parking_overview],
    },
    ReturnValues="UPDATED_NEW"
)

print(result)
