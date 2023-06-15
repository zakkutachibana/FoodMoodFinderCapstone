import time
from flask import Flask, request, jsonify
import numpy as np
from sklearn.discriminant_analysis import StandardScaler
import tensorflow as tf
import pandas as pd
from keras.models import load_model

app = Flask(__name__)
app.config['JSON_SORT_KEYS'] = False

model = load_model('model_new.h5')

ds = pd.read_csv("final_dataset.csv")


@app.route('/predict', methods=['POST'])
def predict():
    if request.content_type == 'application/json':
        data = request.json
    elif request.content_type == 'application/x-www-form-urlencoded':
        data = request.form.to_dict()
    else:
        data = request.form.to_dict()

    if data is None:
        response = {
            'message': 'Unsupported content type',
            'error': True,
            'status': 400,
        }
        return jsonify(response)

    input_data = np.array([list(data.values())])
    
    predictions = np.argmax(model.predict(input_data))

    makanan_sesuai_kelas = ds.loc[ds['Kelas'] == predictions, 'Makanan'].reset_index(drop=True)
    image_urls = ds.loc[ds['Kelas'] == predictions, 'Gambar'].reset_index(drop=True)
        
    makanan_sesuai_kelas = pd.concat([makanan_sesuai_kelas, image_urls], axis=1)

    print(makanan_sesuai_kelas)

    foods = []

    for i, (makanan, url) in enumerate(makanan_sesuai_kelas.values):
        foods.append({
            i: {
                "name": makanan,
                "photo": url
            }
        })

    if foods:
        response = {
            'message': 'Makanan yang sesuai dengan pilihan Anda',
            'foods': foods,
            'error': False,
            'status': 200,
        }
    else:
        response = {
            'message': 'Mohon maaf, makanan tidak tersedia.',
            'error': True,
            'status': 404,
        }

    return jsonify(response)

@app.route('/food/<name>', methods=['GET'])
def get_food_details(name):
    makanan_data = ds.loc[ds['Makanan'] == name]

    if not makanan_data.empty:
        detail_makanan = {
            'nama': makanan_data['Makanan'].values[0],
            'gambar': makanan_data['Gambar'].values[0],
            'karbohidrat': makanan_data['Karbohidrat'].values[0],
            'protein': makanan_data['Protein'].values[0],
            'sayur': makanan_data['Sayur'].values[0],
            'pengolahan': makanan_data['Pengolahan'].values[0]
        }

        response = {
            'message': 'Detail makanan',
            'makanan': detail_makanan,
            'error': False,
            'status': 200
        }
    else:
        response = {
            'message': 'Makanan tidak ditemukan.',
            'error': True,
            'status': 404
        }

    return jsonify(response)

if __name__ == '__main__':
    app.run(debug=True)