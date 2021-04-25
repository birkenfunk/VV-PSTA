# Table of Contents

1. [General Info](#general-info)
2. [Technologies](#technologies)
3. [Installation](#installation)
4. [Collaboration](#collaboration)
5. [FAQs](#faqs)

# General-info

This is a simple server application witch can receive a Json String with a measurement Object

## How To talk to the Server:

| Send                                                                   | Recive                                      |
|:---------------------------------------------------------------------- | ------------------------------------------- |
| `{"type":"Sensor_Hello","payload":{}}`                                 | `{"type":"STATION_HELLO","payload":{}}`     |
| `{"type":"Acknowledge","payload":{}}`                                  | `{"type":"STATION_READY","payload":{}}`     |
| `{"type":"Measurement","payload":{`[Measurement](#Mearuement json)`}}` | `{"type":"STATION_READY","payload":{}}`     |
| You can send more Measurement jsons                                    |                                             |
| `{"type":"Terminate","payload":{}}`                                    | `{"type":"TERMINATE_STATION","payload":{}}` |

## Mearuement json

A measurement has to contain:

- A Value whitch is a int

- A [type](#type)

- A [unit](#unit)

- A Timestamp

In the end it sould look like this

```json
{"value" : 10, "unit" : "CELSIUS", "type" : "TEMPERATURE", "timestamp" : "2021-04-17 18:09:10.189349"}
```

### Type

The different Tpes you can use:

- TEMPERATURE  

- HUMIDITY

- PRESSURE

- COUNT

- FLOW_RATE

- ENERGY

### Unit

The different Units you can use:

- CELSIUS

- KELVIN

- PERCENT

- FAHRENHEIT

- HECTOPASCAL

- UNITS

- CMS2

- KWH3

## Digaramms

The connection automat (Warning: Symbols are a bit different!)
![Connection automat](Automate.png)
A simple connection between the server and the station (Warning: Messages look different!)
![Connection Example](Connection.png)

# Technologies
