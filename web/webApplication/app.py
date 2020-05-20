from flask import Flask, jsonify, render_template, Markup
import aws_controller
from api import get_parking_info
import boto3
import json

app = Flask(__name__)

@app.route('/')
def index():
	#TODO: connect to dynamodb

	return render_template('book_slot.html')

@app.route('/get-items')
def get_items():
	return jsonify(aws_controller.get_items())

@app.route('/api')
def api():
	json_obj = get_parking_info()
	return json_obj

if __name__ == '__main__':
	app.run()


