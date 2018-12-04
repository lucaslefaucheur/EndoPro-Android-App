from sklearn.externals import joblib
import ast
import flask
from flask import request

app = flask.Flask(__name__)

model = joblib.load('model.joblib')

@app.route('/')
def predict():
    
    data = request.args.get('data')

    feature_array = ast.literal_eval(data)

    prediction = model.predict([feature_array]).tolist()
    
    response = {}
    response['predictions'] = prediction
    
    return flask.jsonify(response)
