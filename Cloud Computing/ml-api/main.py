from flask import Flask, request, jsonify
import numpy as np
import tensorflow as tf
import pandas as pd
from keras.models import load_model
import random

app = Flask(__name__)
app.config['JSON_SORT_KEYS'] = False

model = load_model('model_new.h5')

ds = pd.read_csv("final_dataset.csv")


@app.route('/predict', methods=['POST'])
def predict():
    if request.content_type == 'application/json':
        data = request.get_json()
    else:
        response = {
            'message': 'Data yang dimasukkan tidak didukung',
            'error': True,
            'status': 400,
        }
        return jsonify(response)

    preference_input = np.array([list(data.values())])
    
    prediction = np.argmax(model.predict(preference_input))

    makanan_sesuai_kelas = ds.loc[ds['Kelas'] == prediction, 'Makanan'].reset_index(drop=True)
    gambar_url = ds.loc[ds['Kelas'] == prediction, 'Gambar'].reset_index(drop=True)
        
    makanan_sesuai_kelas = pd.concat([makanan_sesuai_kelas, gambar_url], axis=1)

    results = []

    for i, (makanan, gambar) in enumerate(makanan_sesuai_kelas.values):
        food = {
            'name': makanan,
            'photo': gambar
        }
        results.append(food)

    if results:
        response = {
            'message': 'Makanan yang sesuai dengan preferensi Anda :',
            'foods': results,
            'error': False,
            'status': 200,
        }
    else:
        response = {
            'message': 'Makanan tidak ada yang sesuai',
            'error': True,
            'status': 404,
        }

    return jsonify(response)

@app.route('/food-detail/<nama_makanan>', methods=['GET'])
def get_food_detail(nama_makanan):
    data_makanan = ds.loc[ds['Makanan'] == nama_makanan]

    if not data_makanan.empty:
        detail_makanan = {
            'nama': data_makanan['Makanan'].values[0],
            'gambar': data_makanan['Gambar'].values[0],
            'karbohidrat': data_makanan['Karbohidrat'].values[0],
            'protein': data_makanan['Protein'].values[0],
            'sayur': data_makanan['Sayur'].values[0],
            'pengolahan':data_makanan['Pengolahan'].values[0]
        }

        response = {
            'message': 'Detail makanan :',
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

@app.route('/random-foods', methods=['GET'])
def get_random_foods():
    random_foods = ds.sample(n=10)  
    list_food = []

    for index, row in random_foods.iterrows():
        list_food.append({
            'name': row['Makanan'],
            'photo': row['Gambar']
        })

    response = {
        'message': 'Rekomendasi Makanan Untukmu',
        'foods': list_food,
        'error': False,
        'status': 200
    }

    return jsonify(response)

if __name__ == '__main__':
    app.run(debug=True)
