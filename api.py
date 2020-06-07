import boto3
import json
import decimal
from flask import jsonify

class DecimalEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, decimal.Decimal):
            return float(o)
        return super(DecimalEncoder, self).default(o)

def get_parking_info():
    table = boto3.resource('dynamodb', region_name='ap-southeast-2').Table('parking_spot')
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
    return jsonify(parking_info)

def get_overview_info():
    table = boto3.resource('dynamodb', region_name='ap-southeast-2').Table('parking_overview')
    response = table.scan()
    areas = response['Items']
    area_array = []
    for area in areas:
        obj = {}
        obj['area'] = area['parking_area_id']
        obj['stats'] = []
        for ob in area['parking_status']:
            t_ob = {
                'ts': ob['ts'],
                'available': ob['available'],
                'occupied': ob['occupied'],
                'booked': ob['booked'],
                'not booked': ob['not_booked'],
            }
            obj['stats'].append(t_ob)
        area_array.append(obj)

    parking_info = {
        'title': 'iot-smart-parking',
        'members': ['Simon', 'Ruby', 'Khai'],
        'university': 'Swinburne University of Technology',
        'unit-title': 'IOT Programming',
        'unit-code': 'COS30011',
        'areas': area_array
    }
    return jsonify(parking_info)

def get_user_info():
    table = boto3.resource('dynamodb', region_name='ap-southeast-2').Table('users')
    response = table.scan()
    users = response['Items']
    obj = []
    for user in users:
        u = {
            'user_id': user['user_id'],
            'ts_payment': user['ts_payment'],
            'user_fee': user['user_fee']
        }
        obj.append(u)

    user_info = {
        'title': 'iot-smart-parking',
        'members': ['Simon', 'Ruby', 'Khai'],
        'university': 'Swinburne University of Technology',
        'unit-title': 'IOT Programming',
        'unit-code': 'COS30011',
        'users': obj
    }
    return jsonify(user_info)