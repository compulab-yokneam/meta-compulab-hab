LICENSE = "Unknown"
LIC_FILES_CHKSUM = "file://LICENSE.bsd3;md5=1fbcd66ae51447aa94da10cbf6271530"

SRC_URI = "file://cst-3.3.0.tgz \
    file://csf_fit.in \
    file://csf_spl.in \
    file://csf_additional_images.in \
    file://csf.u \
    file://csf.k \
    file://csf.f \
    file://gen_keys.sh \
    file://gen_srk.sh \
    file://makefile \
"

DEPENDS = "openssl-native imx-boot linux-imx"

S = "${WORKDIR}/release"

do_configure () {
    install -d ${S}/tools
    cp ${WORKDIR}/csf.* ${S}/tools
    cp ${WORKDIR}/gen_* ${S}/tools
    chmod a+x ${S}/tools/*
    cp ${WORKDIR}/makefile ${S}/
    cd ${S}
}

do_compile () {
    :
}

do_install () {
    install -d ${D}/boot/signed/
    for d in u k;do
        install -d ${D}/boot/signed/${d}
        target=$(readlink ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/${d}/signed)
        install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/${d}/${target} ${D}/boot/signed/${d}/${target}
        ln -sf signed/${d}/${target} ${D}/boot/${target}-signed
    done
    install -d ${D}/boot/signed/f
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/f/fuse.out ${D}/boot/signed/f/fuse.out
}

do_copy_signed() {
    for d in u k;do
        target=$(readlink ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/${d}/signed)
        cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/${d}/signed ${DEPLOY_DIR_IMAGE}/${target}
    done
}

do_sign () {
    cd ${DEPLOY_DIR_IMAGE}/cst-tools
    oe_runmake kernel imx-boot fuse
}

do_deploy () {
    install -d ${DEPLOY_DIR_IMAGE}/cst-tools
    for d in makefile crts keys tools ca linux64;do
    if [ ! -e ${DEPLOY_DIR_IMAGE}/cst-tools/${d} ];then
        cp -a ${S}/${d} ${DEPLOY_DIR_IMAGE}/cst-tools/
    fi
    done
    cp -a ${S}/keys/hab4_pki_tree.sh ${DEPLOY_DIR_IMAGE}/cst-tools/tools

    install -d ${DEPLOY_DIR_IMAGE}/cst-tools/hab
    cp ${WORKDIR}/csf_*.in ${DEPLOY_DIR_IMAGE}/cst-tools/hab

    do_sign
    do_copy_signed
}

addtask deploy before do_install after do_compile

PROVIDES += "cst-tools"

FILES_${PN} = " \
    /boot/* \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"
