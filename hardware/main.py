import paho.mqtt.client as mqtt
import json
import boto3
from botocore.exceptions import ClientError
import decimal

MQTT_SERVER = "localhost"

# Helper class to convert a DynamoDB item to JSON.
class DecimalEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, decimal.Decimal):
            if o % 1 > 0:
                return float(o)
            else:
                return int(o)
        return super(DecimalEncoder, self).default(o)

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))

    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.subscribe('parking/status')

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    msg.payload = json.loads(msg.payload)

    print('Message Topic: ', msg.topic)
    print('Payload: ', msg.payload)
    print('-------------------------------------------------------')

    # update info to DynamoDB Table
    try:
        if msg.topic == 'parking/status':
            captured_plate_number = 'null'
            table = boto3.resource('dynamodb').Table('parking_spot')
            response = table.get_item(
                Key={'parking_spot_id': str(msg.payload['parking_id'])}
            )
            item = response['Item']
            is_booked = item['isBooked']
            # user_plate_number = item.user['user_carplate_number']
            print(response['Item']['user']['user_carplate_number'])
            if msg.payload['parking_status'] == "Occupied":
                # TODO: trigger camera to capture photo
                print('Capturing Photo')
                # TODO: use ml model to get car plate number
                captured_plate_number = "XYZ123"
                # TODO: compare plate number and publish message to ring buzzer if mismatch
            table = boto3.resource('dynamodb').Table('parking_spot')
            response = table.update_item(
                Key={'parking_spot_id': str(msg.payload['parking_id'])},
                UpdateExpression='set parking_status = :s, captured_plate_number = :n',
                ExpressionAttributeValues={
                    ':s': msg.payload['parking_status'],
                    ':n': captured_plate_number
                },
                ReturnValues="UPDATED_NEW"
            )
            # print(json.dumps(response, indent=4, cls=DecimalEncoder))
    except ClientError as e:
        print(e)
        raise

client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.connect(MQTT_SERVER, 1883, 60)
client.loop_forever()
