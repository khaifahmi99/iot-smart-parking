import boto3
import json

def get_parking_info():
    table = boto3.resource('dynamodb').Table('parking_spot')
    response = table.scan()
    parking_spots = response['Items']

    obj = []
    for spot in parking_spots:
        p = {
            'parking_spot_id': spot['parking_spot_id'],
            'parking_status': spot['parking_status'],
            'is_booked': spot['isBooked'],
        }
        obj.append(p)

    parking_info = {
        'title': 'iot-smart-parking',
        'members': ['Simon', 'Ruby', 'Khai'],
        'university': 'Swinburne University of Technology',
        'unit-title': 'IOT Programming',
        'unit-code': 'COS30011',
        'parking_spot': obj
    }

    return json.dumps(parking_info)
