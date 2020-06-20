DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"

do_compile_append () {

    install -d ${DEST}
    install -m 644 ${B}/arch/arm/boot/zImage ${DEST}/zImage

    cd ${DEST}/..
    oe_runmake kernel
}

DEPENDS += "cst-keys"
COMPATIBLE_MACHINE = "cl-som-imx7"
