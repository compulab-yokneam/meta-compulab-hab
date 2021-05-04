FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append += "file://0099-iMX8M_soc_make.patch"

DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"
# ToDo: additional OSC support
SOC_DIR = "iMX8M"

do_compile_append () {
    install -d ${DEST}
    for target in flash_evk print_fit_hab; do
        make SOC=${SOC_TARGET} ${target} 2>&1 | tee ${DEST}/${target}.log
        if [ ${target} = "flash_evk" ]; then
            if [ -e "${S}/${SOC_DIR}/flash.bin" ]; then
                cp ${S}/${SOC_DIR}/flash.bin ${DEST}/
            fi
        fi
    done
}

COMPATIBLE_MACHINE = "(ucm-imx8m-mini|mcm-imx8m-mini)"
