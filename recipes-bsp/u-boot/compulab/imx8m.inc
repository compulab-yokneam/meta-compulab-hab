SRC_URI:append = " \
    file://security.cfg \
    file://0001-imxx8mm-spl-Issue-sec_init.patch \
    file://0002-Fix-ofnode_by_compatible-link-issue.patch \
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
