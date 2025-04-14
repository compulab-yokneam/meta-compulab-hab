SRC_URI:append = " \
    file://security.cfg \
"

SRC_URI:append:compulab-mx8mp = " \
    file://0001-distro-Load-dtb-after-boot-efi-bbinary.patch \
"

DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"

do_compile_hab_binman () {
    install -d ${DEST}/binman
    for item in .config u-boot.itb u-boot.dtb flash.bin u-boot-spl-ddr.bin;do
        cp ${B}/${MACHINE}_defconfig/${item} ${DEST}/binman
    done
}

do_compile_hab () {
    install -d ${DEST}
    for item in .config;do
        cp ${B}/${MACHINE}_defconfig/${item} ${DEST}
    done
}
addtask compile_hab
do_compile_hab[nostamp] = "1"
do_compile_hab[depends] = "u-boot:do_compile"
