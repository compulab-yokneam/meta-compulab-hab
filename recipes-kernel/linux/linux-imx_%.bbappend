do_compile_append () {

	install -d ${DEPLOY_DIR_IMAGE}/cst-tools/hab
	install -m 644 ${B}/arch/arm64/boot/Image ${DEPLOY_DIR_IMAGE}/cst-tools/hab/Image

}

COMPATIBLE_MACHINE = "(ucm-imx8m-mini|mcm-imx8m-mini)"
