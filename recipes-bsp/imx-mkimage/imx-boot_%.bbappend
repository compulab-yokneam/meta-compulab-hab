FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"
SOC_DIR = "iMX8M"

do_compile_hab () {
    install -d ${DEST}
    cd ${B}
    sed -i '/@rm -f u-boot.its $(dtb)$/d' iMX8M/soc.mak
    for target in flash_evk print_fit_hab; do
        make SOC=${IMX_BOOT_SOC_TARGET} dtbs=${UBOOT_DTB_NAME} ${target} 2>&1 | tee ${DEST}/${target}.log
        if [ ${target} = "flash_evk" ]; then
            cp ${S}/${SOC_DIR}/${UBOOT_DTB_NAME} ${S}/${SOC_DIR}/u-boot.dtb
            if [ -e "${S}/${SOC_DIR}/flash.bin" ]; then
		for item in u-boot.bin u-boot.itb u-boot.its u-boot-nodtb.bin u-boot-spl.bin u-boot-spl-ddr.bin tee.bin bl31.bin flash.bin print_fit_hab.sh;do
                    cp ${S}/${SOC_DIR}/${item} ${DEST}
		done
            fi
        fi
    done
}

addtask compile_hab
do_compile_hab[nostamp] = "1"
do_compile_hab[depends] = "imx-boot:do_compile"
