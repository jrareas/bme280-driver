# BME280 Yocto Recipe

A Linux device driver for the Bosch BME280 temperature, pressure, humidity sensor using the I2C protocol, written in C and packed in a yocto recipe.

 This recipe base itself on a bme280 c driver found at https://github.com/leungjch/bme280-driver. Along with the recipe, there were also some bug fixes on the library to properly read the address 0xA1 from the sensor. 
 
 Also some debug instructions were added for my own sake.
 
 
I tested this recipe using yocto current version and the stm32mp157-dk2. I will push branchs accordingly in the future. Use the same branch as your poky branch. If there is a branch here that matches, that means I tested it.


## Setup on STM32MP157-DK2

First you will need to enable i2c bus 1 in your stm32 yocto build. I will not go over that process as Shawn Hymel said it all on his digikey videos on the subject. I would recommend you to follow the whole series. But if you are not into that yet, just follow:

https://www.youtube.com/watch?v=srM6u8e4tyw&list=PLEBQazB0HUyTpoJoZecRK6PpDG31Y7RPB&index=5

There you will also find how to get the tools to test the i2c device once it is connected

### Veryfing your hardware

Considering you have enabled i2c5 in your STM32MP157-DK2, you will need to hook up your module as per image below:

<img width="50%" height="50%" alt="stm32mp157-dk2-bme280 001" src="https://github.com/user-attachments/assets/2b9f14cc-e5e8-475a-ba3a-3b5d1c868c86" />

As you can see, I am using the Arduino connectors. Feel free to use the Raspeberry PI ones. But make sure you find the righ PIN for SDA and SCL. You would be able to find it in the STM documentation below:

https://www.st.com/resource/en/user_manual/um2637-discovery-kits-with-increasedfrequency-800-mhz-stm32mp157-mpus-stmicroelectronics.pdf

This is how I have mine:

<img width="50%" height="50%" alt="image2" src="https://github.com/user-attachments/assets/0a98df64-3a4a-4cf9-9192-061eb7888b9e" />

<img width="578" height="580" alt="image1" src="https://github.com/user-attachments/assets/4a00c405-e5dc-47aa-b528-8207d71c8add" />


Now lets run `i2cdetect` to verify that the module is available to interface:

```
i2cdetect -y 1
```

The output of `i2cdetect` should be something like

```
     0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f
00:                         -- -- -- -- -- -- -- -- 
10: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
20: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
30: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
40: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
50: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
60: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
70: -- -- -- -- -- -- 76 --                         
```

Which indicates that the sensor is available at 0x76. 

Talking about Yocto now, from here, you should be able to build the recipe and the module will be shipped with your distribution. I should mention this recipe should be part of layer in your build and that layer should be added to your `local.conf` file. The ways to achieve that is beyond the scope here.

## Building and reading data

You can also build the drive standalone if you have the proper SDKs installed in your system.

Build the driver:
```
cd driver && make
```

Upon successfully building, the kernel module `bme280.ko` will be created.

To initialize the driver, run:
```
sudo insmod bme280.ko
```

A device file should now be created in `/dev/bme280`. You can read continuous measurements
as a stream by reading the device file: 
```
chmod 666 /dev/bme280 # enable read access
cat < /dev/bme280
```

To remove the driver, run 
```
sudo rmmod bme280
```

## Data format
The data format of a single reading will be given as a single line in order of temperature, pressure, and
humidity:
```
T2920P24874300H27597
```

To interpret this line:
- Temperature is in Celsius and is accurate to two decimal places: e.g.
  `2920->29.20C`
- Pressure is in Pa and is obtained by dividing the value by 256, accurate to
  two decimal places, e.g. `24874300/256=97165.23 Pa`
- Humidity is in percentage and is obtained by dividng the value by 1024, e.g.
  `27597/1024=25.9%`

Enjoy!
