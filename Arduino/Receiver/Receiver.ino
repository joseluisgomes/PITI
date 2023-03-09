// receiver code for ESP32 board
const int inputPin = 4; // set the digital input pin for receiving the character
char receivedChar = 'A'; // initialize a variable to store the received character

void setup() {
  Serial.begin(9600); // initialize serial communication for debugging
}

void loop() {

  if (digitalRead(inputPin) == LOW) { // wait for the start bit

    Serial.println("Receiving character...");
    delay(10);
    for (int i = 0; i < 8; i++) { // iterate over all 8 bits of the character

      if (digitalRead(inputPin) == HIGH) { // check if the input pin is still HIGH
        Serial.print("1");
        bitSet(receivedChar, i); // set the i-th bit of the character to 1

      } else if(digitalRead(inputPin) == LOW) {
        Serial.print("0");
        bitClear(receivedChar, i); // set the i-th bit of the character to 0
        
      }
      delay(10); // wait for 10 ms before reading the next bit
    }
    Serial.println();
    Serial.print("Received character: ");
    Serial.println(receivedChar); // print the received character to the Serial monitor
    delay(1000);
  }
}




