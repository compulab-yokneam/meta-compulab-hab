LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

SRC_URI = "git://github.com/compulab-yokneam/cst-tools.git;protocol=https"

PV = "1.0+git${SRCPV}"
SRCREV = "3035e0584c4bb14b2f9b2ea13b8b78075e8a3acd"

DEPENDS = "openssl-native"

S = "${WORKDIR}/git"

do_configure () {
	tar -C ${S} -xf ${S}/nxp/cst-3.3.0.tgz --strip-components=1
	cp ${S}/imx7/Makefile.in ${S}/Makefile
	cp -a ${S}/imx7/tools ${S}/
	cp -a ${S}/imx7/hab ${S}/
}

do_compile () {
	:
}

do_install () {
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
