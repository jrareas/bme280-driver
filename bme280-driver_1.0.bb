SUMMARY = "BME280 Recipe"
DESCRIPTION = "Recipe for handling bme280"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
FILESEXTRAPATHS:prepend := "${THISDIR}:"
SRC_URI = "file://driver"

S = "${WORKDIR}/driver"

TARGET_CC_ARCH += "${LDFLAGS}"

inherit module

do_configure() {
	oe_runmake clean
}

do_compile() {
	#oe_runmake bme280.c
	oe_runmake -C ${WORKDIR}/../../${PREFERRED_PROVIDER_virtual/kernel}/6.6.78-stm32mp-r2/build/ M=${S} modules
	
}

do_install() {
	install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers
    install -m 0644 ${S}/bme280.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/

#	install -d ${D}${bindir}
#	install -m 0755 bme280 ${D}${bindir}	
}


FILES:${PN} += "/bme280.ko"

MODULE_NAME = "bme280"

module_autoload = "${MODULE_NAME}" 