FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
    file://0001-cl-som-imx7-Add-u-boot.imx-target.patch \
"

do_install_append_cl-som-imx7 () {

	install -d ${D}/boot/hab
	install -m 644 ${B}/u-boot-ivt.img ${D}/boot/hab/u-boot-ivt.img
	install -m 644 ${B}/u-boot-ivt.img.log ${D}/boot/hab/u-boot-ivt.img.log
	install -m 644 ${B}/SPL ${D}/boot/hab/SPL
	install -m 644 ${B}/SPL.log ${D}/boot/hab/SPL.log
}

do_deploy_append_cl-som-imx7 () {

	install -d ${DEPLOY_DIR_IMAGE}/cst-tools/hab
	install -m 644 ${B}/u-boot-ivt.img ${DEPLOY_DIR_IMAGE}/cst-tools/hab/u-boot-ivt.img
	install -m 644 ${B}/u-boot-ivt.img.log ${DEPLOY_DIR_IMAGE}/cst-tools/hab/u-boot-ivt.img.log
	install -m 644 ${B}/SPL ${DEPLOY_DIR_IMAGE}/cst-tools/hab/SPL
	install -m 644 ${B}/SPL.log ${DEPLOY_DIR_IMAGE}/cst-tools/hab/SPL.log
}

COMPATIBLE_MACHINE_cl-som-imx7 = "cl-som-imx7"
