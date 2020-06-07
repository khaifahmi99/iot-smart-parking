from flask import *
from flask_cors import CORS
import aws_controller
from api import *

app = Flask(__name__)
CORS(app)
app.config['SECRET_KEY'] = 'smart parking iot'
app.config['CORS_HEADERS'] = 'Content-Type'

@app.route('/')
@app.route('/home')
def home():
	spots = aws_controller.get_spots_info()
	return render_template('book_slot.html', spots=spots)

@app.route('/regInfo')
def regInfo():
	return render_template('booking.html')

@app.route('/payment')
def payment():
	return render_template('payment.html')

@app.route('/validateSlot', methods = ["POST", "GET"])  
def validate_book_slot():
	if request.method == 'POST':
		slots = request.form.getlist('slot')
		if len(slots) > 0:
			return render_template('booking.html', data = request.form)
	return redirect(url_for("home"))
 
@app.route('/validateBooking', methods = ["POST"])  
def validate_booking():
	if request.method == 'POST':
		message = "Success!"
		validData = aws_controller.validate_booking(request.form)
		if validData:
			validData = aws_controller.save_booking(validData)
			return render_template('payment.html', data = validData)
		else:
			error = "Please complete form."
			return render_template('booking.html', data = request.form, error = error)

	return render_template('payment.html', message=message, data=validData)


@app.route('/validatePayment', methods = ["POST"])  
def validate_payment():
	if request.method == "POST":
		message = "Payment successful!"
		validData = aws_controller.validate_payment(request.form)
		if validData:
			validData = aws_controller.update_payment(validData)
		return render_template('receipt.html', message=message, data=validData)


@app.route('/visualisation')
def visualisation():
	return render_template('visualisation.html')

@app.route('/api/current')
def api_current():
	return get_parking_info()

@app.route('/api/overview')
def api_overview():
	return get_overview_info()

@app.route('/api/users')
def api_users():
	return get_user_info()

if __name__ == '__main__':
	app.run()