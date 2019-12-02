#include <ESP8266WiFi.h>
#include "EmonLib.h"

EnergyMonitor energyMonitor; //CRIA UMA INSTÂNCIA
WiFiClient client;

/*
#define WIFI_SSID "LIVE TIM_A1F0_2G"
#define WIFI_PASSWORD "wlan595e0f"

#define WIFI_SSID "NET_2G59BC70"
#define WIFI_PASSWORD "SHa221beBKff"
*/

#define WIFI_SSID "iPhone de Ciro"
#define WIFI_PASSWORD "12345678"

#define SENSOR_TENSAO A0
#define TENSAO_REDE 127
const int lamp[8] = {D0, D1, D2, D3, D4, D5, D6, D7};
WiFiServer server(80);

void setupWifi(){
  WiFi.begin(WIFI_SSID,WIFI_PASSWORD);

  Serial.print("Conectando ao Wifi ");

  while (WiFi.status() != WL_CONNECTED) {
    delay(50);
    Serial.print(".");
  }
  Serial.println("");

  Serial.println("Wifi conectado!");

  server.begin();

  // IP do ESP8266
  Serial.println(WiFi.localIP());
}

void setupPins(){
  for(int i = 0 ; i < 8 ; i++){
      pinMode(lamp[i], OUTPUT);
  }
  pinMode(SENSOR_TENSAO, INPUT);
  
  delay(10);

  for(int i = 0 ; i < 8 ; i++){
      digitalWrite(lamp[i], HIGH);
  }

  energyMonitor.current(SENSOR_TENSAO, 60);

  Serial.println("pinos settados!");
  
}
void setup(){
  Serial.begin(9600);
   
  setupWifi();
  setupPins();  
  
}

void loop(){
  // Checa se o clinete foi conectado
  client = server.available();
  if(!client) return ;

  // Espera após o cliente mandar algum dado
  Serial.println("Novo cliente");
  while(!client.available()) delay(1);

  // Lê a primeira linha da requisição
  String request = client.readStringUntil('\r');
  Serial.println(request);
  client.flush();

  if(request.indexOf("lamp0") != -1){
    digitalWrite(lamp[0], !digitalRead(lamp[0]));
  }
  else if(request.indexOf("lamp1") != -1){
    digitalWrite(lamp[1], !digitalRead(lamp[1]));
  }
  else if(request.indexOf("lamp2") != -1){
    digitalWrite(lamp[2], !digitalRead(lamp[2]));
  }
  else if(request.indexOf("lamp3") != -1){
    digitalWrite(lamp[3], !digitalRead(lamp[3]));
  }
  else if(request.indexOf("lamp4") != -1){
    digitalWrite(lamp[4], !digitalRead(lamp[4]));
  }
  else if(request.indexOf("lamp5") != -1){
    digitalWrite(lamp[5], !digitalRead(lamp[5]));
  }
  else if(request.indexOf("lamp6") != -1){
    digitalWrite(lamp[6], !digitalRead(lamp[6]));
  }
  else if(request.indexOf("lamp7") != -1){
    digitalWrite(lamp[7], !digitalRead(lamp[7]));
  }
  else if(request.indexOf("desligarTodos") != -1){
    for(int i = 0 ; i < 8 ; i++){
      digitalWrite(lamp[i], HIGH);
    }
  }
  else if(request.indexOf("ligarTodos") != -1){
    for(int i = 0 ; i < 8 ; i++){
      digitalWrite(lamp[i], LOW);
    }
  }

  //Calcula a corrente
  double Irms = energyMonitor.calcIrms(1480);
  /*
  //Mostra o valor da corrente no serial monitor
  Serial.print("Corrente : ");
  Serial.print(Irms/100.0, 5); // Irms
  Serial.println(" A");
  */
  //Serial.print("Corrente : ");
  //Serial.println(Irms/100.0); // Irmscalcirm
  
  // Manda a resposta da requisição
  client.println("HTTP/1.1 200 OK");
  client.println("Content-Type: text/html");
  client.println("");

  for(int i = 0 ; i < 8 ; i++){
    if(digitalRead(lamp[i]) == LOW){
      client.print("lamp");
      client.print(i);
      client.print("_on");
    }
    else{
      client.print("lamp");
      client.print(i);
      client.print("_off");
    }
    client.print(",");
  }
  client.print(Irms/100.0,5);
  

  //delay(2000);

}
