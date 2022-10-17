DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"

do_compile:append () {
    install -d ${DEST}
    install -m 644 ${B}/arch/arm64/boot/Image ${DEST}/Image
    echo DEST=${DEST}
}

COMPATIBLE_MACHINE = "(ucm-imx8m-mini|mcm-imx8m-mini|iot-gate-imx8)"
