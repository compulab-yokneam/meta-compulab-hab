FILE_LIST_HAB = " \
	file://security.cfg \
"

do_modify_config() {
	cat ${WORKDIR}/security.cfg >> ${S}/configs/${MACHINE}_defconfig
}

addtask modify_config after do_patch before do_configure

SRC_URI_append_mcm-imx8m-mini += "${FILE_LIST_HAB}"
SRC_URI_append_ucm-imx8m-mini += "${FILE_LIST_HAB}"
SRC_URI_append_iot-gate-imx8 += "${FILE_LIST_HAB}"
