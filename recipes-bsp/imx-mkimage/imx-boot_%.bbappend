FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"
# ToDo: additional OSC support
SOC_DIR = "iMX8M"

SOC_TARGET:mx8mm-nxp-bsp = "iMX8MM"
SOC_TARGET:mx8mq-nxp-bsp = "iMX8MQ"
SOC_TARGET:mx8mp-nxp-bsp = "iMX8MP"

do_compile:append () {
    install -d ${DEST}
    sed -i '/@rm -f u-boot.its $(dtb)$/d' iMX8M/soc.mak
    for target in flash_evk print_fit_hab; do
        make SOC=${SOC_TARGET} ${target} 2>&1 | tee ${DEST}/${target}.log
        if [ ${target} = "flash_evk" ]; then
            if [ -e "${S}/${SOC_DIR}/flash.bin" ]; then
                cp ${S}/${SOC_DIR}/flash.bin ${DEST}/
            fi
        fi
    done
}

COMPATIBLE_MACHINE = "(ucm-imx8m-mini|mcm-imx8m-mini|iot-gate-imx8)"
