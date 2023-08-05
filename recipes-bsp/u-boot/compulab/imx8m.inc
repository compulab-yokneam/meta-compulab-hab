FILE_LIST_HAB = " \
	file://security.cfg \
"

do_modify_config() {
	cat ${WORKDIR}/security.cfg >> ${S}/configs/${MACHINE}_defconfig
}

addtask modify_config after do_patch before do_configure

SRC_URI:append:compulab-mx8mm = "${FILE_LIST_HAB}"
SRC_URI:append:compulab-mx8mp = "${FILE_LIST_HAB}"
