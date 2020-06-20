LICENSE = "Unknown"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI = "git://github.com/compulab-yokneam/cst-tools.git;protocol=https"

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "9ff49cd22e91ec426390636df307a14c496e4004"

DEPENDS = "openssl-native"

S = "${WORKDIR}/git"

# NOTE: no Makefile found, unable to determine what needs to be done

do_configure () {
	tar -C ${S} -xf ${S}/nxp/cst-3.3.0.tgz --strip-components=1
	cp ${S}/imx8/Makefile.in ${S}/Makefile
	cp -a ${S}/imx8/tools ${S}/
	cp -a ${S}/imx8/hab ${S}/
}

do_compile () {
	# Specify compilation commands here
	#oe_runmake fuse
	:
}

do_install () {
	# Specify install commands here
	:
}

do_sign () {
    cd ${DEPLOY_DIR_IMAGE}/cst-tools
    oe_runmake fuse
}

do_deploy () {
    install -d ${DEPLOY_DIR_IMAGE}/cst-tools
    for d in Makefile hab crts keys tools ca linux64;do
    if [ ! -e ${DEPLOY_DIR_IMAGE}/cst-tools/${d} ];then
        cp -a ${S}/${d} ${DEPLOY_DIR_IMAGE}/cst-tools/
    fi
    done
    cp -a ${S}/keys/hab4_pki_tree.sh ${DEPLOY_DIR_IMAGE}/cst-tools/tools
    do_sign
}

addtask deploy before do_install after do_compile

PROVIDES = "cst-keys"
PACKAGE_ARCH = "${MACHINE_ARCH}"
