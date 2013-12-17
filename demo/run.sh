cp -rf ../bin/* .
java -cp .:bank.jar bank.Bank Malte-Laptop 22334 NDEA -v &
java -cp .:filiale.jar filiale.Filiale Malte-Laptop 22334 NDEA  1 -v &
java -cp .:geldautomat.jar geldautomat.Geldautomat Malte-Laptop 22334 NDEA-1 -v &
