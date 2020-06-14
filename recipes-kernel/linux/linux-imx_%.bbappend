DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"

do_compile_append () {

	install -d ${DEST}
	install -m 644 ${B}/arch/arm64/boot/Image ${DEST}/Image

    cd ${DEST}/..
    oe_runmake kernel
}

DEPENDS += "cst-keys"
COMPATIBLE_MACHINE = "(ucm-imx8m-mini|mcm-imx8m-mini)"
