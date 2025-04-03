FILE_LIST_HAB = " \
	file://security.cfg \
"

SRC_URI:append:compulab-mx8mm = "${FILE_LIST_HAB}"
SRC_URI:append:compulab-mx8mp = "${FILE_LIST_HAB}"

DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"

do_compile_hab () {
        install -d ${DEST}/binman
	for item in .config u-boot.itb u-boot.dtb flash.bin u-boot-spl-ddr.bin;do
            cp ${B}/${MACHINE}_defconfig/${item} ${DEST}/binman
        done
}
addtask compile_hab
do_compile_hab[nostamp] = "1"
do_compile_hab[depends] = "u-boot:do_compile"
