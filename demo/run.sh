cp -rf ../bin/* .

java -cp ./globalns.jar globalns.NameServiceGlobal &
sleep 3
export GLOBAL_NS_HOST=Malte-Laptop
export GLOBAL_NS_PORT=22334
java -cp bank.jar:bank_access.jar:mware_lib.jar bank.Bank $GLOBAL_NS_HOST $GLOBAL_NS_PORT NDEA -v &
sleep 3
java -cp filiale.jar:bank_access.jar:cash_access.jar:mware_lib.jar filiale.Filiale $GLOBAL_NS_HOST $GLOBAL_NS_PORT NDEA  1 -v &
sleep 3
java -cp geldautomat.jar:cash_access.jar:mware_lib.jar geldautomat.Geldautomat $GLOBAL_NS_HOST $GLOBAL_NS_PORT NDEA-1 -v &
