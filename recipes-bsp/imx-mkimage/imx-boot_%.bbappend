FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"
SOC_DIR = "iMX8M"

do_compile:append () {
    install -d ${DEST}
    sed -i '/@rm -f u-boot.its $(dtb)$/d' iMX8M/soc.mak
    for target in flash_evk print_fit_hab; do
        make SOC=${IMX_BOOT_SOC_TARGET} dtbs=${UBOOT_DTB_NAME} ${target} 2>&1 | tee ${DEST}/${target}.log
        if [ ${target} = "flash_evk" ]; then
            if [ -e "${S}/${SOC_DIR}/flash.bin" ]; then
                cp ${S}/${SOC_DIR}/flash.bin ${DEST}/
            fi
        fi
    done
}
