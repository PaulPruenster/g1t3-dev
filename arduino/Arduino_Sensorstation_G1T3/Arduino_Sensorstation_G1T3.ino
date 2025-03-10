#include <Adafruit_BME680.h>
#include <Adafruit_Sensor.h>
#include <ArduinoBLE.h>
#include <Wire.h>

// define variables
#define IN_DEVELOPMENT 0 // 1 for debuging and prinnting in Serial.Monitor
// define defaults
#define SEALEVELPRESSURE_HPA (1013.25)
#define SOIL_MAP_LOW 895 // low and high raw values of the hygrometer for %-mapping
#define SOIL_MAP_HIGH 449
#define ERROR_BLINK_TIME 250
#define ERROR_BLINK_NUMBER 6
#define PAIRING_TIME_SECONDS 120
#define DEFINED_COLORS 11     // 0 to 11
#define BLACKOUT_TIME 3000000 // 5min for connect after blackout
// define the in- and output pins
#define PIN_BUTTON_ACK A0       // Button SPST (1825910-6)
#define PIN_BUTTON_PAIRING A1   // Button SPST (1825910-6)
#define PIN_SOIL_MOISTURE A2    // KeeYees Kapazitive Analoger Hygrometer
#define PIN_PHOTO_TRANSISTOR A3 // Light transistor (TEPT4400), as Pull-Down
#define PIN_PIEZO_BUZZER A6     // Piezo Buzzer (PS1240P02CT3)

#define PIN_LED_RED D10
#define PIN_LED_BLUE D11
#define PIN_LED_GREEN D12

char BLE_DEVICE_NAME[9] = "G1T3_XXX";

// Pins for the DIP-Switch THT DIP-Switch (ADE0804)
const int PIN_DIP_SWITCH[] = {2, 3, 4, 5, 6, 7, 8, 9};

// error codes
const int ERROR_CODES[] = {1000, 1011, 1091, 1012, 1092, 1013, 1093, 1014,
                           1094, 1015, 1095, 2010, 2090, 3010, 3090, 9999};

// global variables
// define variables for measured sensor data
float bmeAirTemperature = 0.0;
float bmeAirHumidity = 0.0;
float bmeAirPressure = 0.0;
float bmeAirGasResistance = 0.0;
float bmeAltitude = 0.0;
float soilMoisture = 0.0;
float soilMoisturePercentage = 0.0;
bool errorFlag = false;
int errorCode = 0;
long int previous_millis = 0;

int dipswitchState = 0; // Variable to store the state of the DIP switch
float photoTransistorValue = 0.0;
const float RESISTOR_VALUE = 2000.0; // 10k resistor
const float SUPPLY_VOLTAGE = 3.3;    // 3.3V supply voltage
/**
  CALIBRATION_FACTOR is used to get the desired amount of lux,
  using a Led Lenser P7.2 Flashlight which has 450 Lumen.
  Lux is calculated with the online calculator
  https://www.wirsindheller.de/Lumen-zu-Lux-Umrechnung.128.0.html#wshberechnung "Lumen der Leuchte"
  = 450 "Abstrahlwinkel der Leuchte" = 17° "Entfernung der Leuchte" = ca. 1m
*/
const float CALIBRATION_FACTOR = 0.09;

// define errorflag
bool errorCheck = false;
bool ledON = false;

Adafruit_BME680 bme; // create BME688 object

// define BLE service and characteristics
BLEService sensorService("0000181A-0000-1000-8000-00805f9b34fb"); // create service
BLEFloatCharacteristic photoTransistorCharacteristic("00002AFB-0000-1000-8000-00805f9b34fb", BLERead | BLENotify); // create characteristic for Light Transistor
BLEFloatCharacteristic bmeAirTemperatureCharacteristic("00002A6E-0000-1000-8000-00805f9b34fb", BLERead | BLENotify); // create characteristic for BME 688 Air Temperature
BLEFloatCharacteristic bmeAirPressureCharacteristic("00002A6D-0000-1000-8000-00805f9b34fb", BLERead | BLENotify); // create characteristic for BME 688 Air Pressure
BLEFloatCharacteristic bmeAirHumidityCharacteristic("00002A6F-0000-1000-8000-00805f9b34fb", BLERead | BLENotify); // create characteristic for BME 688 Air Humidity
BLEFloatCharacteristic bmeAltitudeCharacteristic("00002AB3-0000-1000-8000-00805f9b34fb", BLERead | BLENotify); // create characteristic for BME 688 Altitude
BLEFloatCharacteristic bmeAirGasResistanceCharacteristic("0000fff1-0000-1000-8000-00805f9b34fb", BLERead | BLENotify); // create characteristic for BME 688 Air Gas Resistance
BLEFloatCharacteristic hygrometerCharacteristic("0000fff2-0000-1000-8000-00805f9b34fb", BLERead | BLENotify); // create characteristic for Soil Humidity Hygrometer
BLEBoolCharacteristic errorFlagCharacteristic("0000fff3-0000-1000-8000-00805f9b34fb", BLERead | BLEWrite | BLENotify); // create characteristic for error acknowledgement
BLEIntCharacteristic errorCodeCharacteristic("0000fff4-0000-1000-8000-00805f9b34fb", BLERead | BLEWrite | BLENotify); // create characteristic for transmitting an errortext for identifying the error

// functions
void initializePinModes();
void initializeDipSwitchCode();
void initializeBle();
void initializeBme();

void startPairing(bool pairingBtn = true);
void buttonRead();
void millisDelay(long int delayTime);

void getSensorData();
void getPhotoTransistorData();
void setBleSensorData();

int* getLEDColors(int colorCode);
void setRGB(int red, int green, int blue);
void blinkLightHandler(int colorCode1, int colorCode2, int blinkDelayMs, int numBlinks,
                       bool twoColors);
void showColors();
void toggleLed(int pin);

void getBleErrorData();
void errorHandler(int testCode = 1);
void errorOutput();
void playConnectionSuccessful();
void playConnectionFailed();

void blePeripheralConnectHandler(BLEDevice central);
void blePeripheralDisconnectHandler(BLEDevice central);
void debugDataRead();

// main function
void setup() {

	if(IN_DEVELOPMENT == 1) {
		Serial.begin(9600);
		while(!Serial)
			;
	} else {
		Serial.begin(9600);
	}

	initializeDipSwitchCode();
	millisDelay(150);

	initializePinModes();
	millisDelay(150);

	initializeBle();
	millisDelay(150);

	startPairing(false);
	millisDelay(150);

	initializeBme();
}

// loop function
void loop() {

	buttonRead();

	if(ledON) {
		digitalWrite(PIN_LED_BLUE, LOW);
		ledON = false;
	}

	BLEDevice central = BLE.central();

	if(central && IN_DEVELOPMENT == 1) {
		// if a central is connected to peripheral:
		Serial.print("Connected to central: ");
		// print the central's MAC address:
		Serial.println(central.address());
	}

	getSensorData();
	setBleSensorData();
	buttonRead();
	getBleErrorData();

	if(errorFlag) {
		debugDataRead();
		errorHandler();
	}
	millisDelay(200);
}

/**
  Initializes the pin modes for the various components used in the project.
  The acknowledgement and pairing pins are set as inputs with pull-up resistors,
  the light transistor pin is set as input, the piezo buzzer pin is set as output,
  and the RGB LED pins are set as outputs. Additionally, the dipswitch pins are set
  as inputs with pull-up resistors.

  @param none
  @return none
*/
void initializePinModes() {
	pinMode(PIN_BUTTON_ACK, INPUT_PULLUP); // Set acknowledgement pin as input with pull-up resistor
	pinMode(PIN_BUTTON_PAIRING, INPUT_PULLUP); // Set pairing pin as input with pull-up resistor
	pinMode(PIN_PHOTO_TRANSISTOR, INPUT);      // Set phototransistor pin as input
	pinMode(PIN_PIEZO_BUZZER, OUTPUT);         // Set piezo speaker pin as output

	pinMode(PIN_LED_RED, OUTPUT);
	pinMode(PIN_LED_BLUE, OUTPUT);
	pinMode(PIN_LED_GREEN, OUTPUT);

	for(int i = 0; i < 8; i++) {
		pinMode(PIN_DIP_SWITCH[i], INPUT_PULLUP);
	}
}

/**
  Initialize the dip switch code.
  Reads the state of each switch pin (like 8 bit) and combines as decimal them into a single
  integer. The result is stored in the variable dipswitchState.

  @param none
  @return none
*/
void initializeDipSwitchCode() {
	int Sum = 0;

	if(digitalRead(PIN_DIP_SWITCH[7]) == LOW)
		Sum += 1;
	if(digitalRead(PIN_DIP_SWITCH[6]) == LOW)
		Sum += 2;
	if(digitalRead(PIN_DIP_SWITCH[5]) == LOW)
		Sum += 4;
	if(digitalRead(PIN_DIP_SWITCH[4]) == LOW)
		Sum += 8;
	if(digitalRead(PIN_DIP_SWITCH[3]) == LOW)
		Sum += 16;
	if(digitalRead(PIN_DIP_SWITCH[2]) == LOW)
		Sum += 32;
	if(digitalRead(PIN_DIP_SWITCH[1]) == LOW)
		Sum += 64;
	if(digitalRead(PIN_DIP_SWITCH[0]) == LOW)
		Sum += 128;
	dipswitchState = Sum;
}

/**
  Initializes the Bluetooth Low Energy (BLE) module and sets up the advertising service and
  characteristics for the sensor data. If the BLE module fails to initialize, the function will
  enter an infinite loop until the issue is resolved. The function sets the local name and device
  name of the BLE module to "SensorStation G1T3" and configures the advertising service to use the
  "sensorService" object. The "tempCharacteristic", "humCharacteristic", "presCharacteristic", and
  "airCharacteristic" objects are added to the "sensorService" object to transmit the corresponding
  sensor data. Finally, the "sensorService" object is added to the BLE module and the function
  starts the pairing process. The function will wait in a loop until a device is connected to the
  BLE module.

  @param none
  @return none
*/
void initializeBle() {
	if(!BLE.begin()) {
		Serial.println("Failed to initialize BLE.");
		errorFlag = true;
		errorCodeCharacteristic.writeValue(9999);
		while(1)
			if(errorFlag) {
				errorHandler();
			};
	}

	snprintf(BLE_DEVICE_NAME, 9, "G1T3_%03d", dipswitchState);
	BLE.setLocalName(BLE_DEVICE_NAME);
	BLE.setDeviceName(BLE_DEVICE_NAME);
	BLE.setAdvertisedService(sensorService);
	BLE.setManufacturerData((const uint8_t*)&dipswitchState, sizeof(dipswitchState));
	/*
	tempCharacteristic.descriptor(tempDescriptor);
	humCharacteristic.descriptor(humDescriptor);
	presCharacteristic.descriptor(presDescriptor);
	airCharacteristic.descriptor(airDescriptor);
	*/
	sensorService.addCharacteristic(hygrometerCharacteristic);
	sensorService.addCharacteristic(photoTransistorCharacteristic);
	sensorService.addCharacteristic(bmeAirTemperatureCharacteristic);
	sensorService.addCharacteristic(bmeAirPressureCharacteristic);
	sensorService.addCharacteristic(bmeAirHumidityCharacteristic);
	sensorService.addCharacteristic(bmeAirGasResistanceCharacteristic);
	sensorService.addCharacteristic(bmeAltitudeCharacteristic);
	sensorService.addCharacteristic(errorFlagCharacteristic);
	sensorService.addCharacteristic(errorCodeCharacteristic);
	BLE.addService(sensorService);

	// initializing the values in the characteristics
	setBleSensorData();

	// set handlers for BLEConnected and BLEDisconnected events
	BLE.setEventHandler(BLEConnected, blePeripheralConnectHandler);
	BLE.setEventHandler(BLEDisconnected, blePeripheralDisconnectHandler);

	BLE.advertise();
	Serial.println("BLE service started.");
}

/**
  This function initializes the BME688 sensor by setting up oversampling and filter initialization.
  If the sensor cannot be found, it prints an error message to the Serial monitor, sets an error
  flag, and sends the error via BLE to the access point.

  @param none
  @return none
*/
void initializeBme() {
	if(!bme.begin()) {
		Serial.println("Error - BME688 sensor, check wiring!");
		errorFlag = true;
		errorCodeCharacteristic.writeValue(9999);
		while(1)
			if(errorFlag) {
				errorHandler();
			};
	}
	// Set up oversampling and filter initialization
	bme.setTemperatureOversampling(BME680_OS_8X);
	bme.setHumidityOversampling(BME680_OS_2X);
	bme.setPressureOversampling(BME680_OS_4X);
	bme.setIIRFilterSize(BME680_FILTER_SIZE_3);
	bme.setGasHeater(320, 150); // 320*C for 150 ms
}

/**
  This function starts the pairing mode by disconnecting from any currently connected device,
  and advertising the BLE connection for the specified PAIRING_TIME_SECONDS,
  during which it toggles the blue LED and checks for a new connection.
  If a connection was successful or not is signaled with a melody.

  @param none
  @return none
*/
void startPairing(bool pairingBtn) {

	BLE.pairable();

	Serial.println("Pairing mode started");
	unsigned long startTime = millis();
	while(millis() - startTime < (PAIRING_TIME_SECONDS * 1000)) {

		if(BLE.connected() && pairingBtn) {
			if(IN_DEVELOPMENT == 1) {
				Serial.println("before disconnect! ");
			}
			BLE.disconnect();
			// Serial.println("Disconnected!");
			BLE.advertise();
			if(IN_DEVELOPMENT == 1) {
				Serial.print("Pairingmode ... ");
				Serial.println(PAIRING_TIME_SECONDS - (millis() - startTime) / 1000);
			}
			toggleLed(PIN_LED_BLUE);
			millisDelay(500);

		} else {
			if(IN_DEVELOPMENT == 1) {
				Serial.println("before poll! ");
			}

			BLE.poll();
			// BLE.advertise();
			toggleLed(PIN_LED_BLUE);
			millisDelay(500);
		}

		if(BLE.connected()) {
			millisDelay(120);
			break;
		}
	}
	Serial.println("Pairing mode ended");
	millisDelay(150);
	BLE.stopAdvertise();
}

/**
  Reads the state of buttons and performs corresponding actions.
  This function checks the state of two buttons (ACK button and Pairing button) and performs actions
  based on their states. If the ACK button is pressed (PIN_BUTTON_ACK is LOW) and the Pairing button
  is not pressed (PIN_BUTTON_PAIRING is HIGH), it prints a message indicating that the
  acknowledgement button is pressed, sets the errorFlag to false, and updates the
  errorFlagCharacteristic. If the Pairing button is pressed (PIN_BUTTON_PAIRING is LOW) and the ACK
  button is not pressed (PIN_BUTTON_ACK is HIGH), it prints a message indicating that the pairing
  button was pressed and calls the startPairing() function. If both the Pairing button and the ACK
  button are pressed (PIN_BUTTON_PAIRING and PIN_BUTTON_ACK are LOW), it plays a sequence of audio
  and visual feedback to indicate a connection failure, and then plays a connection successful
  sound. This function does not return any value.

  @param none
  @return none
*/
void buttonRead() {
	// Read button state and do something if it is pressed
	if(digitalRead(PIN_BUTTON_ACK) == LOW && digitalRead(PIN_BUTTON_PAIRING) == HIGH) {
		// Handles acknowledgement button press
		Serial.println("acknowledgement Button is pressed");
		errorFlag = false;
		errorFlagCharacteristic.setValue(false);
		errorCodeCharacteristic.writeValue(0);
	} else if(digitalRead(PIN_BUTTON_PAIRING) == LOW && digitalRead(PIN_BUTTON_ACK) == HIGH) {
		// Handles pairing button press
		Serial.println("Pairing Button was pressed");
		startPairing(true);
	} else if(digitalRead(PIN_BUTTON_PAIRING) == LOW && digitalRead(PIN_BUTTON_ACK) == LOW) {
		// Handles both button press
		playConnectionFailed();
		millisDelay(500);
		showColors();
		millisDelay(500);
		errorOutput();
		millisDelay(500);
		playConnectionSuccessful();
		millisDelay(500);
	}
}

/**
  Delays the program for a specified amount of time, in milliseconds.

  @param delayTime The delay time, in milliseconds.
  @return none
*/
void millisDelay(long int delayTime) {
	long int start_time = millis();
	while(millis() - start_time < delayTime)
		;
}

/**
  This function handles sensor data and updates the global variables that store the sensor readings.
  The function reads data from the BME680 sensor (temperature, humidity, pressure, air quality)
  and also reads soil moisture from an analog input pin.
  Preconditions:
  The BME680 sensor must be connected to the Arduino board and the appropriate library must be
  included. The soil moisture sensor must be connected to an analog input pin on the Arduino board.
  Postconditions:
  The global variables temperature, humidity, pressure, airQuality, soilMoisture, and
  soilMoisturePercentage are updated. If the BME680 sensor reading fails, the function will print an
  error message to the Serial Monitor and return without updating any values.

  @param none
  @return none
*/
void getSensorData() {
	if(!bme.performReading()) {
		Serial.println("Failed to perform reading from BME :(");
		errorFlag = true;
		errorCodeCharacteristic.writeValue(9999);
		return;
	}
	int tempSoilMoisturePercentage;

	bmeAirTemperature = bme.temperature;               // in degree C
	bmeAirHumidity = bme.humidity;                     // in percent
	bmeAirPressure = bme.pressure / 100.0;             // convert from Pa to hPa
	bmeAirGasResistance = bme.gas_resistance / 1000.0; // convert from Ohms to kOhms
	bmeAltitude = bme.readAltitude(SEALEVELPRESSURE_HPA);
	soilMoisture = analogRead(PIN_SOIL_MOISTURE); // arduino analog input
	tempSoilMoisturePercentage =
	    map(soilMoisture, SOIL_MAP_LOW, SOIL_MAP_HIGH, 0, 100); // calculated Hygrometer to %
	soilMoisturePercentage = (float)tempSoilMoisturePercentage;
	getPhotoTransistorData();
}

/**
  Reads the analog input from the phototransistor connected to PIN_PHOTO_TRANSISTOR
  and calculates the illuminance in lux based on the voltage
  and SENSITIVITY of the Fototransistor (TEPT4400).

  @param none
  @return none
*/
void getPhotoTransistorData() {
	// this should be the correct code to read lux with the given photo-transistor TEPT4400
	//    but this does not what it should!!!!
	int tempPhotoTransistorValue = analogRead(PIN_PHOTO_TRANSISTOR);
	millisDelay(200);
	float voltage = tempPhotoTransistorValue * (SUPPLY_VOLTAGE / 1023.0);
	float fototransistorVoltage = voltage * (RESISTOR_VALUE / (SUPPLY_VOLTAGE - voltage));
	float illuminance = fototransistorVoltage / CALIBRATION_FACTOR;
	photoTransistorValue = illuminance;
}

/**
  This function is used to handle the BLE data that is sent or received from the connected device.
  It sends the data for soil moisture percentage, light transistor value, air temperature, air
  pressure, air humidity, air gas resistance, and altitude to the corresponding BLE characteristics.

  @param none
  @return none
*/
void setBleSensorData() {
	hygrometerCharacteristic.writeValue(soilMoisturePercentage);
	photoTransistorCharacteristic.writeValue(photoTransistorValue);
	bmeAirTemperatureCharacteristic.writeValue(bmeAirTemperature);
	bmeAirPressureCharacteristic.writeValue(bmeAirPressure);
	bmeAirHumidityCharacteristic.writeValue(bmeAirHumidity);
	bmeAirGasResistanceCharacteristic.writeValue(bmeAirGasResistance);
	bmeAltitudeCharacteristic.writeValue(bmeAltitude);
}

/**
  This function takes an integer color code as input and returns an array of three integers that
  represent the RGB values of the corresponding LED color. The function dynamically allocates memory
  for the array, so it is the responsibility of the calling function to deallocate the memory to
  prevent memory leaks. If the color code is not defined, the function does not assign any values to
  the RGB array.

  @param colorCode: an integer that represents a specific color code, ranging from 0 to 6, as
  follows:
  - 0: red
  - 1: green
  - 2: blue
  - 3: yellow
  - 4: magenta
  - 5: cyan
  - 6: white
  - 7: Orange
  - 8: Lightblue
  - 9: Lightgreen
  - 10: Lightred
  - 11: Purple
  @return An integer pointer that points to an array of three integers (in the order of red, green,
  and blue) representing the RGB values of the corresponding LED color. If the color code is not
  defined, the function returns a null pointer.
*/
int* getLEDColors(int colorCode) {
	int* ledColors = new int[3];
	int redValue, greenValue, blueValue;
	switch(colorCode) {
	case 0:
		// Red
		redValue = 255;
		greenValue = 0;
		blueValue = 0;
		break;
	case 1:
		// Green
		redValue = 0;
		greenValue = 255;
		blueValue = 0;
		break;
	case 2:
		// Blue
		redValue = 0;
		greenValue = 0;
		blueValue = 255;
		break;
	case 3:
		// yellow
		redValue = 247;
		greenValue = 210;
		blueValue = 12;
		break;
	case 4:
		// magenta
		redValue = 255;
		greenValue = 0;
		blueValue = 255;
		break;
	case 5:
		// cyan
		redValue = 0;
		greenValue = 255;
		blueValue = 255;
		break;
	case 6:
		// white
		redValue = 255;
		greenValue = 255;
		blueValue = 255;
		break;
	case 7:
		// Orange
		redValue = 245;
		greenValue = 125;
		blueValue = 12;
		break;
	case 8:
		// Lightblue
		redValue = 0;
		greenValue = 138;
		blueValue = 255;
		break;
	case 9:
		// Lightgreen
		redValue = 0;
		greenValue = 255;
		blueValue = 111;
		break;
	case 10:
		// Lightred
		redValue = 255;
		greenValue = 95;
		blueValue = 95;
		break;
	case 11:
		// Purple
		redValue = 255;
		greenValue = 95;
		blueValue = 95;
		break;
	default:
		// color code not defined
		break;
	}
	ledColors[0] = redValue;
	ledColors[1] = greenValue;
	ledColors[2] = blueValue;

	return ledColors;
}

/**
  Set the RGB color of an LED using PWM.

  @param red The brightness of the red LED, ranging from 0 (off) to 255 (full brightness).
  @param green The brightness of the green LED, ranging from 0 (off) to 255 (full brightness).
  @param blue The brightness of the blue LED, ranging from 0 (off) to 255 (full brightness).
 */
void setRGB(int red, int green, int blue) {
	analogWrite(PIN_LED_RED, red);
	analogWrite(PIN_LED_GREEN, green);
	analogWrite(PIN_LED_BLUE, blue);
}

/**
  This function blinks an RGB LED with one to two specified colors, delaytime and number of flashes.
  @param colorCode1 An integer value between 0 and 6 representing the first color to use for
  blinking.
  @param colorCode2 An integer value between 0 and 6 representing the second color to use for
  blinking. This is used only if the "twoColors" parameter is set to true.
  @param blinkDelayMs The time to wait (in milliseconds) between each blink of the LED.
  @param numBlinks The number of times to blink the LED.
  @param twoColors A boolean value indicating whether to alternate between two colors or just use
  one color. If true, the LED will alternate between the two colors specified by colorCode1 and
  colorCode2. If false, the LED will blink using only colorCode1. The function first calls the
  getLEDColors function to retrieve the RGB values for the specified color codes. Then it enters a
  loop where it toggles the LED on and off at the specified delay. The function uses the PWM pins to
  control the intensity of each color. After the specified number of blinks, the LED is turned off
  and memory used for storing the LED colors is deallocated.
  @return none
*/
void blinkLightHandler(int colorCode1, int colorCode2, int blinkDelayMs, int numBlinks,
                       bool twoColors) {
	// colorCode has to be an int between 0 and 6
	int* ledColors1 = getLEDColors(colorCode1);
	int* ledColors2 = getLEDColors(colorCode2);

	unsigned long previousMillis = 0;
	unsigned int blinkCount = 0;
	bool isLedOn = false;
	while(blinkCount < numBlinks) {
		unsigned long currentMillis = millis();
		if(currentMillis - previousMillis >= blinkDelayMs) {
			// Toggle LED state
			if(!twoColors && isLedOn) {
				setRGB(0, 0, 0);
				isLedOn = false;
			} else if(!twoColors && !isLedOn) {
				setRGB(ledColors1[0], ledColors1[1], ledColors1[2]);
				isLedOn = true;
			} else if(twoColors && isLedOn) {
				setRGB(ledColors1[0], ledColors1[1], ledColors1[2]);
				isLedOn = false;
			} else if(twoColors && !isLedOn) {
				setRGB(ledColors2[0], ledColors2[1], ledColors2[2]);
				isLedOn = true;
			}
			previousMillis = currentMillis;
			blinkCount++;
		}
	}
	setRGB(0, 0, 0);
	delete[] ledColors1;
	delete[] ledColors2;
}

/**
  This function is called to check and light all predefined colors.

  @param none
  @return none
*/
void showColors() {
	for(int i = 0; i <= DEFINED_COLORS; i++) {
		int* ledColors1 = getLEDColors(i);
		setRGB(ledColors1[0], ledColors1[1], ledColors1[2]);
		millisDelay(500);
		delete[] ledColors1;
	}
	setRGB(0, 0, 0);
}

/**
  Toggles the state of a pin connected to an LED.

  @param pin the pin number to toggle
  @return none
 */
void toggleLed(int ledPin) {
	if(!ledON) {
		digitalWrite(ledPin, HIGH);
		ledON = true;
	} else {
		digitalWrite(ledPin, LOW);
		ledON = false;
	}
}

/**
  This function is called when new data is received on the error flag
  and error code characteristics of the BLE device,
  to write it in a variable for further usage.

  @param none
  @return none
*/
void getBleErrorData() {
	int16_t tempErrorCode = 0;
	errorCodeCharacteristic.readValue(&tempErrorCode, 2);
	errorCode = (int)tempErrorCode;
	uint8_t tempErrorFlag = 0;
	errorFlagCharacteristic.readValue(&tempErrorFlag, 1);
	errorFlag = (bool)tempErrorFlag;
}
/*
write in int:
int16_t intValue;
 intValueCharacteristic.readValue( &intValue, 2 ); // 2 byte?

write in float:
float floatValue;
 floatValueCharacteristic.readValue( &floatValue, 4 ); // 4 byte?
*/

/**
  The errorHandler function checks if an error flag is set and if so,
  checks if the transmitted error code matches any of the codes in the ERROR_CODES array.
  If a match is found, the appropriate LED colors are passed to the blinkLightHandler
  function to indicate the error.
  If no match is found, the blinkLightHandler function is called to blink a red LED to indicate an
  unknown error code.
  - 0: red
  - 1: green
  - 2: blue
  - 3: yellow
  - 4: magenta
  - 5: cyan
  - 6: white
  - 7: Orange
  - 8: Lightblue
  - 9: Lightgreen
  - 10: Lightred
  - 11: Purple

  @param testCode is optional errorcode for testing
  @return none
*/
void errorHandler(int testCode) {
	if(!errorFlag && !errorCheck) {
		return;
	}

	int numErrorCodes = sizeof(ERROR_CODES) / sizeof(ERROR_CODES[0]);
	bool errorCodeFound = false;
	int transmittedErrorCode;

	if(errorCheck) {
		transmittedErrorCode = testCode;
	} else {
		transmittedErrorCode = errorCode;
	}

	for(int i = 0; i < numErrorCodes; i++) {
		if(transmittedErrorCode == ERROR_CODES[i]) {
			errorCodeFound = true;
			break;
		}
	}

	if(errorCodeFound) {
		switch(transmittedErrorCode) {
		case 1000:
			blinkLightHandler(0, 2, ERROR_BLINK_TIME, ERROR_BLINK_NUMBER * 2, true);
			break;
		case 1011:
			blinkLightHandler(0, 1, ERROR_BLINK_TIME, ERROR_BLINK_NUMBER * 2, true);
			break;
		case 1091:
			blinkLightHandler(0, 1, ERROR_BLINK_TIME / 2, ERROR_BLINK_NUMBER * 4, true);
			break;
		case 1012:
			blinkLightHandler(0, 3, ERROR_BLINK_TIME, ERROR_BLINK_NUMBER * 2, true);
			break;
		case 1092:
			blinkLightHandler(0, 3, ERROR_BLINK_TIME / 2, ERROR_BLINK_NUMBER * 4, true);
			break;
		case 1013:
			blinkLightHandler(0, 4, ERROR_BLINK_TIME, ERROR_BLINK_NUMBER * 2, true);
			break;
		case 1093:
			blinkLightHandler(0, 4, ERROR_BLINK_TIME / 2, ERROR_BLINK_NUMBER * 4, true);
			break;
		case 1014:
			blinkLightHandler(0, 5, ERROR_BLINK_TIME, ERROR_BLINK_NUMBER * 2, true);
			break;
		case 1094:
			blinkLightHandler(0, 5, ERROR_BLINK_TIME / 2, ERROR_BLINK_NUMBER * 4, true);
			break;
		case 1015:
			blinkLightHandler(0, 6, ERROR_BLINK_TIME, ERROR_BLINK_NUMBER * 2, true);
			break;
		case 1095:
			blinkLightHandler(0, 6, ERROR_BLINK_TIME / 2, ERROR_BLINK_NUMBER * 4, true);
			break;
		case 2010:
			blinkLightHandler(0, 7, ERROR_BLINK_TIME, ERROR_BLINK_NUMBER * 2, true);
			break;
		case 2090:
			blinkLightHandler(0, 7, ERROR_BLINK_TIME / 2, ERROR_BLINK_NUMBER * 4, true);
			break;
		case 3010:
			blinkLightHandler(0, 11, ERROR_BLINK_TIME, ERROR_BLINK_NUMBER * 2, true);
			break;
		case 3090:
			blinkLightHandler(0, 11, ERROR_BLINK_TIME / 2, ERROR_BLINK_NUMBER * 4, true);
			break;
		case 9999:
			blinkLightHandler(0, 0, ERROR_BLINK_TIME, ERROR_BLINK_NUMBER * 2, false);
			blinkLightHandler(2, 2, ERROR_BLINK_TIME, ERROR_BLINK_NUMBER * 2, false);
			break;
		default:
			// default is optional
			break;
		}
	} else {
		blinkLightHandler(0, 0, ERROR_BLINK_TIME / 2, ERROR_BLINK_NUMBER * 2, false);
	}
}

/**
  This function is called to check and light all flashing error-codes colors.

  @param none
  @return none
*/
void errorOutput() {
	errorCheck = true;
	for(auto i : ERROR_CODES) {
		errorHandler(i);
	}
	setRGB(0, 0, 0);
	errorCheck = false;
}

/**
  This function plays sound if the BLE-Connection was successful.

  @param none
  @return none
*/
void playConnectionSuccessful() {
	int melody[] = {0, 659, 0, 523, 659, 0, 784, 659};              // Frequencies for melody
	int noteDurations[] = {150, 150, 150, 150, 150, 150, 500, 500}; // Durations for each note

	for(int i = 0; i < sizeof(melody) / sizeof(int); i++) {
		tone(PIN_PIEZO_BUZZER, melody[i], noteDurations[i]); // Play the note for its duration
		millisDelay(noteDurations[i] / 2);                   // Add a small delay between notes
		noTone(PIN_PIEZO_BUZZER);                            // Stop playing the note
	}
}

/**
  This function plays sound if the BLE-Connection failed.

  @param none
  @return none
*/
void playConnectionFailed() {
	int melody[] = {523, 392, 330, 262, 196};        // Frequencies for melody
	int noteDurations[] = {150, 150, 150, 150, 500}; // Durations for each note

	for(int i = 0; i < sizeof(melody) / sizeof(int); i++) {
		tone(PIN_PIEZO_BUZZER, melody[i], noteDurations[i]); // Play the note for its duration
		millisDelay(noteDurations[i] / 2);                   // Add a small delay between notes
		noTone(PIN_PIEZO_BUZZER);                            // Stop playing the note
	}
}

void blePeripheralConnectHandler(BLEDevice central) {
	if(IN_DEVELOPMENT == 1) {
		Serial.println("Connected event, central: ");
		Serial.println(central.address());
	}
	playConnectionSuccessful();
	if(ledON) {
		digitalWrite(PIN_LED_BLUE, LOW);
		ledON = false;
	}
	millisDelay(250);
	BLE.stopAdvertise();
}

void blePeripheralDisconnectHandler(BLEDevice central) {
	if(IN_DEVELOPMENT == 1) {
		Serial.println("Disconnected event, central: ");
		Serial.println(central.address());
	}
	if(ledON) {
		digitalWrite(PIN_LED_BLUE, LOW);
		ledON = false;
	}
	playConnectionFailed();
}

/**
  Prints the debug data including sensor readings and other variables.

  @param none
  @return none
*/
void debugDataRead() {
	Serial.print("dipSwitchState = ");
	Serial.print(dipswitchState);
	Serial.println(" .");

	Serial.print("bmeAirTemperature = ");
	Serial.print(bmeAirTemperature);
	Serial.println(" *C");

	Serial.print("bmeAirHumidity = ");
	Serial.print(bmeAirHumidity);
	Serial.println(" %");

	Serial.print("bmeAirPressure = ");
	Serial.print(bmeAirPressure);
	Serial.println(" hPa");

	Serial.print("bmeAirGasResistance = ");
	Serial.print(bmeAirGasResistance);
	Serial.println(" kOhms");

	Serial.print(F("Approx. bmeAltitude = "));
	Serial.print(bmeAltitude);
	Serial.println(F(" m"));

	Serial.print("soilMoisture: ");
	Serial.print(soilMoisture);
	Serial.println(" raw");

	Serial.print("soilMoisturePercentage: ");
	Serial.print(soilMoisturePercentage);
	Serial.println(" %");

	Serial.print("getPhotoTransistorData: ");
	Serial.print(photoTransistorValue);
	Serial.println(" lux");

	Serial.print("errorFlag: ");
	Serial.print(errorFlag);
	Serial.println(" bool");

	Serial.print("errorCode: ");
	Serial.print(errorCode);
	Serial.println(".");
}
