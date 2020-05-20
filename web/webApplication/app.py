from flask import *
import aws_controller
from api import get_parking_info
import boto3
import json

app = Flask(__name__)

@app.route('/home')
def home():
	return render_template('book_slot.html')

@app.route('/regInfo')
def regInfo():
	return render_template('booking.html')

@app.route('/payment')
def payment():
	return render_template('payment.html')


@app.route('/validateSlot', methods = ["POST"])  
def validate():
	message = ''
	if request.method == 'POST':
		return redirect(url_for("regInfo"))  
	return redirect(url_for("home"))


@app.route('/paySlot', methods = ["POST"])  
def paySlot():  
    if request.method == 'POST':  
        return redirect(url_for("payment"))  
    return redirect(url_for("regInfo"))  

@app.route('/api')
def api():
	json_obj = get_parking_info()
	return json_obj

if __name__ == '__main__':
	app.run()


