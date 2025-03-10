#include <Adafruit_BME680.h>

| # | Service Name | Service UUID | Characteristic Name | Characteristic UUID | Properties | Description |
|---|--------------|--------------|---------------------|---------------------|------------|-------------|
| 1 | Sensor Service | `0000181A-0000-1000-8000-00805f9b34fb` | Photo Transistor Data | `00002AFB-0000-1000-8000-00805f9b34fb` | Read/Notify | Gibt Daten des Fototransistors zurück oder benachrichtigt über neue Daten. |
| 2 | Sensor Service | `0000181A-0000-1000-8000-00805f9b34fb` | BME688 Temperature Data | `00002A6E-0000-1000-8000-00805f9b34fb` | Read/Notify | Gibt Daten des BME688-Temperatursensors zurück oder benachrichtigt über neue Daten. |
| 3 | Sensor Service | `0000181A-0000-1000-8000-00805f9b34fb` | BME688 Pressure Data | `00002A6D-0000-1000-8000-00805f9b34fb` | Read/Notify | Gibt Daten des BME688-Drucksensors zurück oder benachrichtigt über neue Daten. |
| 4 | Sensor Service | `0000181A-0000-1000-8000-00805f9b34fb` | BME688 Humidity Data | `00002A6F-0000-1000-8000-00805f9b34fb` | Read/Notify | Gibt Daten des BME688-Hygrometers zurück oder benachrichtigt über neue Daten. |
| 6 | Sensor Service | `0000181A-0000-1000-8000-00805f9b34fb` | BME688 Altitude Data | `00002AB3-0000-1000-8000-00805f9b34fb` | Read/Notify | Gibt Daten zur Höhe des BME688-Sensors zurück oder benachrichtigt über neue Daten. |
| 5 | Sensor Service | `0000181A-0000-1000-8000-00805f9b34fb` | BME688 Gas Resistance Data | `0000fff1-0000-1000-8000-00805f9b34fb` | Read/Notify | Gibt Daten des BME688-Gaswiderstandsensors zurück oder benachrichtigt über neue Daten. |
| 0 | Sensor Service | `0000181A-0000-1000-8000-00805f9b34fb` | Hygrometer Data | `0000fff2-0000-1000-8000-00805f9b34fb` | Read/Notify | Gibt Daten des Hygrometers zurück oder benachrichtigt über neue Daten. |
| 8 | Sensor Service | `0000181A-0000-1000-8000-00805f9b34fb` | Errorflag | `0000fff3-0000-1000-8000-00805f9b34fb` | Read/write/Notify | Gibt Error true oder false zurück. |
| 9 | Sensor Service | `0000181A-0000-1000-8000-00805f9b34fb` | Errorcode | `0000fff4-0000-1000-8000-00805f9b34fb` | Read/write/Notify | Gibt Errorcode zurück. |
| 7 | Sensor Service | `0000181A-0000-1000-8000-00805f9b34fb` | Information | `0000fff5-0000-1000-8000-00805f9b34fb` | Read/write/Notify | Gibt sonstige Informationen zurück. |

