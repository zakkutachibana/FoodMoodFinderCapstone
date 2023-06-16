# Machine Learning 

Machine learning collects datasets from kaggle and other sources from google, we collect these datasets into the "datasets" folder. Once the dataset is collected we label the food names so that they can be grouped into the appropriate classes. Based on the food names, we provide four question points as follows: 

Karbohidrat :
* 0 = Beras dan olahannya
* 1 = Lainnya
* 2 = Tepung, Mie, dan Pasta
* 3 = Tidak ada

Protein :
* 0 = Ayam dan daging
* 1 = Ikan dan Seafood
* 2 = Lainnya
* 3 = Tahu, tempe, dan telur
* 4 = Tidak ada

Sayur :
* 0 = Tidak
* 1 = Ya

Pengolahan :
* 0 = Goreng atau tumis
* 1 = Lainnya
* 2 = Panggang atau bakar
* 3 = Rebus atau kukus

The next stage is modeling. Modeling is done by dividing the data into training, validation, and testing data. The model used is multiclass classification with Tensorflow. After the model is formed with high accuracy with a limit of 99.5% and the model is said to be good, the next step is the prediction process. If the prediction process has been completed, the model is ready to be deployed in the form of .h5 for further processing by cloud computing.
