DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"

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
    cd ${DEST}/..
    oe_runmake imx-boot
    cp ${DEST}/signed/u/flash.bin ${S}/${BOOT_CONFIG_MACHINE}-flash_evk
}

DEPENDS += "cst-keys"
COMPATIBLE_MACHINE = "(ucm-imx8m-mini|mcm-imx8m-mini)"
