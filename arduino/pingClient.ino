

#include <SPI.h>
#include <Ethernet.h>

int pirPin = 3;
int ledPin = 12;

// Enter a MAC address for your controller below.
// Newer Ethernet shields have a MAC address printed on a sticker on the shield
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
// if you don't want to use DNS (and reduce your sketch size)
// use the numeric IP instead of the name for the server:
//IPAddress server(74,125,232,128);  // numeric IP for Google (no DNS)
char server[] = "pingy-server.herokuapp.com";    // name address for Google (using DNS)

// Set the static IP address to use if the DHCP fails to assign
IPAddress ip(192, 168, 0, 177);

// Initialize the Ethernet client library
// with the IP address and port of the server
// that you want to connect to (port 80 is default for HTTP):
EthernetClient client;

boolean wasLow = false;

void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  
  pinMode(pirPin, INPUT);
  pinMode(ledPin, OUTPUT);

  // start the Ethernet connection:
  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    // try to congifure using IP address instead of DHCP:
    Ethernet.begin(mac, ip);
  }
  // give the Ethernet shield a second to initialize:
  delay(1000);
}

void loop() {
  
  if(digitalRead(pirPin) == HIGH) {
    Serial.println("high");
    if (wasLow) {
      pingServer();
      wasLow = false;
    }
    digitalWrite(ledPin, HIGH);   //the led visualizes the sensors output pin state

  } else {
    wasLow = true;
    digitalWrite(ledPin, LOW);   //the led visualizes the sensors output pin state
    Serial.println("low");

  }
    
  delay(500);

}

void pingServer() {
  
  Serial.println("connecting...");

  // if you get a connection, report back via serial:
  if (client.connect(server, 80)) {
    Serial.println("connected");
    // Make a HTTP request:
    client.println("POST /ping HTTP/1.1");
    client.println("Host: pingy-server.herokuapp.com");
    client.println("Connection: close");
    client.println();
    
    while (!client.available()) ;
    
    Serial.println("disconnecting...");
    
    client.stop();
    
  } else {
    // if you didn't get a connection to the server:
    Serial.println("connection failed");
  }
  
}
