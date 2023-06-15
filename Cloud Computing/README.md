# Cloud Computing Path

Create a RESTful API and deploy it to Google Cloud Platform using Google App Engine for communication between Machine Learning Recommendation System Model and Mobile Development. Using Flask to deploy the machine learning model and creating a database on Cloud SQL.

## Cloud Architecture
![alt text](https://github.com/zakkutachibana/FoodMoodFinderCapstone/blob/main/Cloud%20Computing/Cloud%20architecture.png?raw=true)

## Installation Flask
To get started with this project using Google App Engine for Flask, follow the steps below:

- Clone the project repository
```git clone https://github.com/your-repo.git ```

- Create a new file called app.yaml in the project directory. Add the following configuration to the app.yaml file:
```
runtime: python39
instance_class: F2

entrypoint: gunicorn -b :$PORT main:app
```

- install requirements.txt and list the dependencies required for your Flask application.
```pip install -r requirements.txt ```


- Deploy the APIs on Google App Engine:
```gcloud app deploy```

This command deploys your Flask application to Google App Engine. Follow the prompts to select your project and choose the region where you want to deploy your application.

# API Endpoints

| Endpoint | Method | Body Sent (JSON) | Method |
| ------ | ------ | ------ | ------ |
| / | GET | None | Testing Server |
| /login | POST | username & password | Authentication for user |
| /register | POST | username & password | Registration for new user |
| /predict | POST | karbohirat,protein,sayur & pengolahan (Int) | HTTP Post Request Prediction Endpoint |
| /random-foods | GET | None| Give the response 10 foods random |
| /food-detail/:nama_makanan | GET | None | Give the response about foods detail |

